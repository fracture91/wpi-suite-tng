#!/usr/bin/python3
import argparse
import os
import sys
import subprocess


ap = argparse.ArgumentParser(description="Add EPL license header to .java files in given dirs with\
                                          correct contributors if it doesn't already exist")
ap.add_argument("directories", nargs='*', help="Any number of directories containing .java files")
ap.add_argument("--files", nargs='+', help="One or more specific files to modify", default=[])

epl_comment = """\
/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
%CONTRIBUTORS%
 ******************************************************************************/

"""

log_command = "git log --pretty=format:%an -s --follow -p".split()

def probably_contains_header(text):
	for line in text.splitlines():
		if line.strip() == "":
			continue
		# if the first non-blank line looks like the header
		if line.startswith("/*****"):
			return True;
	return False


def add_header(path):
	original_text = "";
	with open(path, "r") as f:
		original_text = f.read();
	if probably_contains_header(original_text):
		print("Already contains header", path)
		return

	# this gives us authors of all commits touching this file, most recent on top
	output = subprocess.check_output(log_command + [path], universal_newlines=True);
	seen_contributors = set()
	contrib_lines = ""
	# want original authors listed first under Contributors
	for author in reversed(output.splitlines()):
		if author not in seen_contributors:
			if len(seen_contributors) > 0:
				contrib_lines += "\n"
			contrib_lines += " *    " + author
			seen_contributors.add(author)
	header = epl_comment.replace("%CONTRIBUTORS%", contrib_lines)

	print("Adding license header to", path)
	print("Contributors:", contrib_lines, sep="\n")
	with open(path, "w") as f:
		f.write(header + original_text)


if __name__ == "__main__":
	args = ap.parse_args()
	if len(args.directories + args.files) == 0:
		print("No directories or files given", file=sys.stderr)
		exit()

	for path in args.files:
		if not os.path.isfile(path):
			raise Exception(path + " is not a file")
		add_header(path);

	for directory in args.directories:
		for root, dirs, files in os.walk(directory):
			for path in files:
				if path.endswith(".java"):
					add_header(os.path.join(root, path))


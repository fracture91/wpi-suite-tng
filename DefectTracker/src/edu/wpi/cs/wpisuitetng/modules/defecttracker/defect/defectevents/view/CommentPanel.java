/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.defectevents.view;

import java.text.SimpleDateFormat;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;

@SuppressWarnings("serial")
public class CommentPanel extends DefectEventPanel {

	public CommentPanel(Comment comment) {
		setTitle("<html><font size=4><b>" + comment.getUser().getName() + "<font size=.25></b> commented on " + new SimpleDateFormat("MM/dd/yy hh:mm a").format(comment.getDate()) + "</html>");
		setContent("<html><i>" + comment.getBody() + "</i></html>");
	}
}

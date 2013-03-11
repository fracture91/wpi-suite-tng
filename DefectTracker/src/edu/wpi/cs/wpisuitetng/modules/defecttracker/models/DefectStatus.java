/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

/**
 * Possible values for the status of a defect.
 */
public enum DefectStatus {
	NEW,       // new, untriaged defect, possibly missing details or steps to reproduce
	CONFIRMED, // exact defect has been pinned down and can be reproduced by others
	INVALID,   // not actually a defect, impossible to reproduce, or decided not to fix
	RESOLVED   // the defect has been fixed
	// note that there is no "ASSIGNED" status - this would be redundant since we have an assignee field
}

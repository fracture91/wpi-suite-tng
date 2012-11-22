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

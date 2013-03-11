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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.models.validators;

/**
 * Represents an error in model validation. 
 */
public class ValidationIssue {
	
	private String message;
	private String fieldName;
	
	/**
	 * Create a ValidationIssue caused by a particular field in the model.
	 * 
	 * @param message An error message ("Must be 5-100 characters")
	 * @param fieldName The relevant field name ("title")
	 */
	public ValidationIssue(String message, String fieldName) {
		this.message = message;
		this.fieldName = fieldName;
	}
	
	/**
	 * Create a generic ValidationIssue with no specific field at fault
	 * @param message An error message ("You are not allowed to edit defects")
	 */
	public ValidationIssue(String message) {
		this(message, null);
	}

	/**
	 * @return the message associated with this error
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the fieldName causing this error
	 */
	public String getFieldName() {
		return fieldName;
	}
	
	/**
	 * @return true if this error has a fieldName
	 */
	public boolean hasFieldName() {
		return fieldName != null;
	}
	
}

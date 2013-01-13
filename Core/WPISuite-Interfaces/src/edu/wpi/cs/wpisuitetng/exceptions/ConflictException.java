/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mpdelladonna
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.exceptions;

import javax.servlet.http.HttpServletResponse;

/**
 * Exception thrown when attempting to store an object already in the database
 * @author mpdelladonna
 *
 */
public class ConflictException extends WPISuiteException {

	public ConflictException(String message) {
		super(message);
	}

	@Override
	public int getStatus() {
		return HttpServletResponse.SC_CONFLICT; //409
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7823907873323480290L;

}

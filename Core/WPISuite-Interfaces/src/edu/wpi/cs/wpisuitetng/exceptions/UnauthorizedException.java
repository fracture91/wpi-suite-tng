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

public class UnauthorizedException extends WPISuiteException {

	/**
	 * Exception thrown when attempting an action not allowed for that user's permission
	 * level
	 */
	private static final long serialVersionUID = 9127615601542990581L;
	
	public UnauthorizedException()
	{
	}
	
	public UnauthorizedException(String message)
	{
		super(message);
	}

	@Override
	public int getStatus() {
		return HttpServletResponse.SC_UNAUTHORIZED; //401
	}

}

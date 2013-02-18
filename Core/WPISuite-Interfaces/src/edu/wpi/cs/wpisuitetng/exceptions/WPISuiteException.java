/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    twack
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.exceptions;

import javax.servlet.http.HttpServletResponse;

/**
 * Base WPI Suite Exception class. 
 * @author twack
 *
 */
public class WPISuiteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5271354512939175980L;
	
	/**
	 * 
	 * @param message details the cause of the error and any other pertinant information
	 */
	public WPISuiteException(String message)
	{
		super(message);
	}
	
	/**
	 * Blank Constructor
	 */
	public WPISuiteException()
	{
		
	}

	/**
	 * Holds the HTTP response code related to the cause of the exception
	 * @return
	 */
	public int getStatus()
	{
		return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	}
}

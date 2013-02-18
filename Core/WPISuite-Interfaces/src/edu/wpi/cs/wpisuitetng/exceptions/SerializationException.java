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
 * Thrown for serialization errors in Models and EntityManagers.
 * @author twack
 *
 */
public class SerializationException extends WPISuiteException {

	/**`
	 * 
	 */
	private static final long serialVersionUID = 3607525121952037935L;
	
	public SerializationException(String message)
	{
		super(message);
	}

	@Override
	public int getStatus() {
		return HttpServletResponse.SC_INTERNAL_SERVER_ERROR; // 500
	}
}

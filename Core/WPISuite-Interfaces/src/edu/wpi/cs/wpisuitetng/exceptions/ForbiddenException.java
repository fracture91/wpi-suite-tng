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

public class ForbiddenException extends WPISuiteException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -859475732000910990L;

	@Override
	public int getStatus() {
		return HttpServletResponse.SC_FORBIDDEN; //403
	}
}

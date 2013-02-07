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

package edu.wpi.cs.wpisuitetng;

import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;

/**
 * A JSON implementation of the ResponseFormatter interface.
 * 	Fields: statusCode (number), message (String)
 * 
 * @author twack
 */
public class JsonErrorResponseFormatter implements ErrorResponseFormatter {

	@Override
	public String formatContent(WPISuiteException e) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		
		json.append("statusCode : " + e.getStatus());
		json.append(", ");
		
		json.append("message : \"" + e.getMessage() + "\"");
		
		json.append("}");
		
		return json.toString();
	}

}

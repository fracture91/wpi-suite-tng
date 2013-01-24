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
 * HTML implementation of the ErrorResponseFormatter. Handles the format of the
 * 	error content body
 * @author twack
 *
 */
public class HtmlErrorResponseFormatter implements
		ErrorResponseFormatter {

	private String header = "<html> <head> <title> %d </title> </head>";
	private String body = " <body> <h1> Error %d </h1> <h2> Info </h2> <p> %s </p> </body> </html>";
	
	@Override
	public String formatContent(WPISuiteException e) {
		String content = "";
		
		// format header
		content += String.format(this.header, e.getStatus());
		
		// format body
		String message = e.getMessage();
		if(message == null)
		{
			message = "";
		}
		
		content += String.format(this.body, e.getStatus(), message); // TODO: add stack trace
		
		return content;
	}

}

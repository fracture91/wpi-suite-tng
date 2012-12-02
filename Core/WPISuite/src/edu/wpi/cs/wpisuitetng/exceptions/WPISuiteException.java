package edu.wpi.cs.wpisuitetng.exceptions;

import javax.servlet.http.HttpServletResponse;

/**
 * Base WPI Suite Exception class.
 * @author twack
 *
 */
public class WPISuiteException extends Exception {

	
	
	public int getStatus()
	{
		return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	}
}
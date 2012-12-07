package edu.wpi.cs.wpisuitetng.exceptions;

import javax.servlet.http.HttpServletResponse;

/**
 * Exception thrown when attempting to store an object already in the database
 * @author mpdelladonna
 *
 */
public class ConflictException extends WPISuiteException {

	@Override
	public int getStatus() {
		return HttpServletResponse.SC_CONFLICT; //409
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7823907873323480290L;

}

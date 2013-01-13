package edu.wpi.cs.wpisuitetng.exceptions;

import javax.servlet.http.HttpServletResponse;

/**
 * Exception thrown when json is malformed.  
 * @author mpdelladonna
 *
 */
public class BadRequestException extends WPISuiteException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7362821691240580782L;

	public BadRequestException(String message) {
		super(message);
	}

	@Override
	public int getStatus() {
		return HttpServletResponse.SC_BAD_REQUEST; //409
	}

}

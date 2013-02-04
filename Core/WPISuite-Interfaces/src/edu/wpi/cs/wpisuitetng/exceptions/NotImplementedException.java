package edu.wpi.cs.wpisuitetng.exceptions;

import javax.servlet.http.HttpServletResponse;

/**
 * Exception thrown when json is malformed.  
 * @author mpdelladonna
 *
 */
public class NotImplementedException extends WPISuiteException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7362821691240580782L;

	@Override
	public int getStatus() {
		return HttpServletResponse.SC_NOT_IMPLEMENTED; //501
	}

}

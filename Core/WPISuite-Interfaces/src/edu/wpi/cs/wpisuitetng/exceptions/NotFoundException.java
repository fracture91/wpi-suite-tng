package edu.wpi.cs.wpisuitetng.exceptions;

import javax.servlet.http.HttpServletResponse;

public class NotFoundException extends WPISuiteException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2573827722743846840L;

	@Override
	public int getStatus() {
		return HttpServletResponse.SC_NOT_FOUND; //404
	}

}

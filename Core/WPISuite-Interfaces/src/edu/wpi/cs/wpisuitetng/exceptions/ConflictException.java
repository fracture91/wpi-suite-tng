package edu.wpi.cs.wpisuitetng.exceptions;

import javax.servlet.http.HttpServletResponse;

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

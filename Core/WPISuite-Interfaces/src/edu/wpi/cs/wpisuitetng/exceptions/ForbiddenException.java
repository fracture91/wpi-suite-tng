package edu.wpi.cs.wpisuitetng.exceptions;

import javax.servlet.http.HttpServletResponse;

public class ForbiddenException extends WPISuiteException {

	
	private static final long serialVersionUID = -859475732000910990L;

	@Override
	public int getStatus() {
		return HttpServletResponse.SC_FORBIDDEN; //403
	}
}

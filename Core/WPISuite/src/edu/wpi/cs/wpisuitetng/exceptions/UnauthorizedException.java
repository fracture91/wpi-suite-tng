package edu.wpi.cs.wpisuitetng.exceptions;

import javax.servlet.http.HttpServletResponse;

public class UnauthorizedException extends WPISuiteException {

	@Override
	public int getStatus() {
		return HttpServletResponse.SC_UNAUTHORIZED;
	}

}

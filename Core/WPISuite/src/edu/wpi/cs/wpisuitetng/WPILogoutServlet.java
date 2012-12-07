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

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WPILogoutServlet. Handles the Logout service given GET requests.
 * 
 * @author twack
 */
@WebServlet("/logout")
public class WPILogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Authenticator auth;
       
    /**
     * 	Defines the Authorization type used for logout.
     * @see HttpServlet#HttpServlet()
     */
    public WPILogoutServlet() {
        super();
        this.auth = new BasicAuth(); // define Authorization implementation	
    }

	/**
	 * 	Performs the Logout service. The request must contain a valid Session cookie.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// if there are no cookies, then this is an invalid logout request
		if(request.getCookies() == null)
		{
			response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 - Forbidden, no session cookie posted to log off with
		}
		else
		{
			// find the WPISUITE cookie in the request
			Cookie[] cookies = request.getCookies();
			int cookieIndex = 0;
			boolean found = false;
			while(found != true)
			{
				// if found, then logout the user with the given username.
				if(cookies[cookieIndex].getName().startsWith("WPISUITE-"))
				{
					this.auth.logout(cookies[cookieIndex].getValue()); // logs out the user with the given session cookie.
					response.setStatus(HttpServletResponse.SC_CONTINUE); // logout successful
					found = true;
				}
			}
		}
	}

}

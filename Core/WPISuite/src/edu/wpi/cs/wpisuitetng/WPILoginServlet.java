/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mpdelladonna
 *    twack
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wpi.cs.wpisuitetng.exceptions.*;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Servlet implementation class WPILoginServlet
 * 
 * @author mpdelladonna, twack
 */
@WebServlet("/login")
public class WPILoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Authenticator auth;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WPILoginServlet() {
        super();
        this.auth = new BasicAuth(); // define Authorization implementation
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// parse the POST body into a String
		BufferedReader in = request.getReader();
		String postString = new String();
		String line = in.readLine();
		while(line != null)
		{
			postString.concat(line);
			line = in.readLine();
		}
		
		// Authentication
		try 
		{	
			Session ses = this.auth.login(postString);
			
			// post back the Session Cookie.
			Cookie userCookie = ses.toCookie();
			response.addCookie(userCookie);
			response.setStatus(HttpServletResponse.SC_CONTINUE);  //100 - Client can continue
		}
		catch(AuthenticationException e) // Authentication Failed.
		{
			//TODO: log error
			response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 - Forbidden, Authentication Failed.
		}
	}

	@Override
	/**
	 * Implements the User Logout functionality. Mapped to '/logout'
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// if there are no cookies, then this is an invalid logout request
		if(req.getCookies() == null)
		{
			resp.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 - Forbidden, no session cookie posted to log off with
		}
		else
		{
			// find the WPISUITE cookie in the request
			Cookie[] cookies = req.getCookies();
			int cookieIndex = 0;
			boolean found = false;
			while(found != true)
			{
				// if found, then logout the user with the given username.
				if(cookies[cookieIndex].getName().startsWith("WPISUITE-"))
				{
					this.auth.logout(cookies[cookieIndex].getValue()); // logs out the user with the given session cookie.
					resp.setStatus(HttpServletResponse.SC_CONTINUE); // logout successful
					found = true;
				}
			}
		}
	}	
}

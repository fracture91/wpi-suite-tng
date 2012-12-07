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

/**
 * Servlet implementation class WPILoginServlet. Handles the login service given POST requests.
 * 
 * @author mpdelladonna, twack
 */
@WebServlet("/login")
public class WPILoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Authenticator auth;
       
    /**
     * Defines the Authorization type used for login.
     * 
     * @see HttpServlet#HttpServlet()
     */
    public WPILoginServlet() {
        super();
        this.auth = new BasicAuth(); // define Authorization implementation
    }

	/**
	 * Performs login service. Request is validated by the Authenticator, so the POST body
	 * 	must be a valid authorization token for the Authenticator implementation defined by the constructor. 
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
}

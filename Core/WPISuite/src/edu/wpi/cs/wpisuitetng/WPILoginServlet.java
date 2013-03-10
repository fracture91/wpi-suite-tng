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
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wpi.cs.wpisuitetng.authentication.Authenticator;
import edu.wpi.cs.wpisuitetng.authentication.BasicAuth;
import edu.wpi.cs.wpisuitetng.exceptions.*;

/**
 * Servlet implementation class WPILoginServlet
 * 
 * @author mpdelladonna, twack
 */
@WebServlet("/login")
public class WPILoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Authenticator auth;
	private ErrorResponseFormatter responseFormatter;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WPILoginServlet() {
        super();
        this.auth = new BasicAuth(); // define Authorization implementation
        this.responseFormatter = new JsonErrorResponseFormatter(); // define Response content body format
    }
    
    /**
     * Perform project switching action. Given a project name in the PUT Body, switches the Session to an instance for a project.
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	Cookie[] cook = request.getCookies();
    	String ssid = null;
		for(Cookie c : cook)
		{
			if(c.getName().startsWith("WPISUITE-"))
				ssid = c.getValue();
		}
		
		if(ssid != null)
		{			
			try
			{				
				// find the project ID
				BufferedReader putBody = request.getReader();
				String projectName = putBody.readLine();
				
				// swap out the Sessions and add the project.
				ManagerLayer man = ManagerLayer.getInstance();
				SessionManager sessions = man.getSessions();
				String newSsid = sessions.switchToProject(ssid, projectName);
				
				// attach the new cookie to give back to the user.
				Session projectSession = sessions.getSession(newSsid);
				Cookie switchedCookie = projectSession.toCookie();
				response.addCookie(switchedCookie);
				
				response.setStatus(HttpServletResponse.SC_OK);
			}
			catch(WPISuiteException e)
			{
				response.setStatus(e.getStatus());
				String contentBody = this.responseFormatter.formatContent(e);
				
				try {
					PrintWriter contentWriter = response.getWriter();
					contentWriter.write(contentBody);
					contentWriter.flush();
					contentWriter.close();
				}
				catch(IOException writerException)
				{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
				}
			}
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 - no cookie given
		}
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// parse the POST header into a String
		String postString = request.getHeader("Authorization");
		
		// Authentication
		try 
		{	
			Session ses = this.auth.login(postString);
			
			// post back the Session Cookie.
			Cookie userCookie = ses.toCookie();
			response.addCookie(userCookie);
			response.setStatus(HttpServletResponse.SC_OK);  //200 - Success
			System.out.println("DEBUG: response set");
		}
		catch(AuthenticationException e) // Authentication Failed.
		{			
			// Set the response
			response.setStatus(e.getStatus()); // 403 - Forbidden, Authentication Failed.
			
			String contentBody = this.responseFormatter.formatContent(e);
			
			// write the string to the body
			try {
				PrintWriter contentWriter = response.getWriter();
				contentWriter.write(contentBody);
				contentWriter.flush();
				contentWriter.close();	
			} 
			catch (IOException writerException) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
			}
		} catch (WPISuiteException e) {
			// Set the response
			response.setStatus(e.getStatus()); // 403 - Forbidden, Authentication Failed.
			
			String contentBody = this.responseFormatter.formatContent(e);
			
			// write the string to the body
			try {
				PrintWriter contentWriter = response.getWriter();
				contentWriter.write(contentBody);
				contentWriter.flush();
				contentWriter.close();	
			} 
			catch (IOException writerException) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
			}
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

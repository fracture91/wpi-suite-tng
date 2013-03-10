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


package edu.wpi.cs.wpisuitetng.authentication;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs.wpisuitetng.ManagerLayer;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.SessionManager;
import edu.wpi.cs.wpisuitetng.exceptions.AuthenticationException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Authenticator abstract class. Defines the login function with an abstract parsePost() function.
 * 	Subclasses implement the decoding of the user credentials in the POST string.
 * @author twack
 *
 */
public abstract class Authenticator {
	
	private String authType;
	private PasswordCryptographer passwordHash;
	private static final Logger logger = Logger.getLogger(Authenticator.class.getName());
	
	/**
	 * Default constructor with a type definition parameter
	 * @param type	Authorization Type (e.g. Basic)
	 */
	public Authenticator(String type)
	{
		this.authType = type;  
		this.passwordHash = new Sha256Password();
	}
	
	public String getAuthType()
	{
		return this.authType;
	}
	
	/**
	 * Logs a user out given their sessionToken
	 * @param sessionToken	a user's serialized Cookie
	 */
	public void logout(String sessionToken)
	{
		ManagerLayer manager = ManagerLayer.getInstance();
		manager.getSessions().removeSession(sessionToken);
		logger.log(Level.INFO, "Session <" + sessionToken + "> logged out");
	}
	
	/**
	 * 	Logs the user in. Assumes the output of parsePost(str) will be
	 * 		a string array of the format: [<username>, <password>].
	 * @param postString	the POST body from the request object.
	 * @return	a Session for the authenticated user, if login is successful.
	 * @throws AuthenticationException	When the user's credentials are invalid or do not parse.
	 * @throws WPISuiteException if an error occurs in getEntity
	 */
	public Session login(String postString) throws AuthenticationException, WPISuiteException
	{
		// parse the post string for credentials
		logger.log(Level.INFO, "Begin POST body parsing <" + postString + ">");
		String[] credentials = parsePost(postString); // [0] - username, [1] - password
		logger.log(Level.INFO, "End POST body parsing <" + postString + ">");
		
		// attempt to retrieve the User from the Manager layer
		ManagerLayer manager = ManagerLayer.getInstance();
		User[] u;
		try {
			System.out.println("DEBUG: Retrieve Login User");
			logger.log(Level.INFO, "Attempting to retrieve User...");
			u = manager.getUsers().getEntity(credentials[0]);
		} catch (NotFoundException e) {
			logger.log(Level.WARNING, "Login attempted with non-existant user <" + credentials[0] + ">");
			throw new AuthenticationException("The user \"" + credentials[0] + "\" could not be found. Please check if the username was spelled correctly.");
		}

		User user = u[0];
		
		// check password
		System.out.println("DEBUG: Authenticate Password");
		// password security
		logger.log(Level.INFO, "Authenticating password for User <" + credentials[0] + ">...");
		String hashedPassword = this.passwordHash.generateHash(credentials[1]);
		if(!user.matchPassword(hashedPassword))
		{
			logger.log(Level.WARNING, "Login attempted with bad password for User <" + credentials[0] + ">");
			throw new AuthenticationException("An invalid password was given. Please check the password and try again.");
		}
		logger.log(Level.INFO, "Password authentication Success! <" + credentials[0] + ">");
		
		// create a Session mapping in the ManagerLayer
		SessionManager sessions = manager.getSessions();
		String ssid = sessions.createSession(user);
		Session userSession = sessions.getSession(ssid);
		
		System.out.println("DEBUG: Create Session");
		
		logger.log(Level.INFO, "Login Success. <" + credentials[0] + ">");
		return userSession;
	}
	
	/**
	 * Implements encoding-specific POST string parsing logic.
	 * @param post	the POST string
	 * @return	a String array with the user credentials in the format [username, password]
	 * @throws AuthenticationException	for any parsing error, invalid format, etc.
	 */
	protected abstract String[] parsePost(String post) throws AuthenticationException;
}

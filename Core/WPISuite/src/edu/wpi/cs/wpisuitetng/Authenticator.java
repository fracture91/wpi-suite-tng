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

import edu.wpi.cs.wpisuitetng.exceptions.AuthenticationException;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Authenticator abstract class. Defines the login function with an abstract parsePost() function.
 * 	Subclasses implement the decoding of the user credentials in the POST string.
 * @author twack
 *
 */
public abstract class Authenticator {
	
	private String authType;
	
	/**
	 * Default constructor with a type definition parameter
	 * @param type	Authorization Type (e.g. Basic)
	 */
	public Authenticator(String type)
	{
		this.authType = type;  
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
	}
	
	/**
	 * 	Logs the user in. Assumes the output of parsePost(str) will be
	 * 		a string array of the format: [<username>, <password>].
	 * @param postString	the POST body from the request object.
	 * @return	a Session for the authenticated user, if login is successful.
	 * @throws AuthenticationException	When the user's credentials are invalid or do not parse.
	 */
	public Session login(String postString) throws AuthenticationException
	{
		// parse the post string for credentials
		String[] credentials = parsePost(postString); // [0] - username, [1] - password	
		
		// attempt to retrieve the User from the Manager layer
		ManagerLayer manager = ManagerLayer.getInstance();
		User[] u = manager.getUsers().getEntity(credentials[0]);
		
		if(u[0] == null)
		{
			throw new AuthenticationException();	//"No user with the given username found");
		}

		User user = u[0];
		
		// check password
		if(!user.matchPassword(credentials[1]))
		{
			throw new AuthenticationException();
		}
		
		// create a Session mapping in the ManagerLayer
		Session userSession = manager.getSessions().createSession(user);
		
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

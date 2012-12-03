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
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

import org.apache.catalina.util.Base64;

/**
 * @author twack
 *
 */
public class BasicAuth implements Authenticator {

	/**
	 * @see edu.wpi.cs.wpisuitetng.Authenticator#login(java.lang.String)
	 */
	@Override
	public Session login(String postCredentials) throws AuthenticationException
	{
		String[] creds = this.parsePost(postCredentials);
		
		ManagerLayer manager = ManagerLayer.getInstance();
		User[] u = manager.getUsers().getEntity(creds[0]);
		
		if(u.length == 0)
		{
			throw new AuthenticationException();	//"No user with the given username found");
		}

		User user = u[0];
		
		if(!user.matchPassword(creds[1]))
		{
			throw new AuthenticationException();
		}
		
		// create a Session mapping in the ManagerLayer
		Session ses = manager.getSessions().createSession(user);
				
		return ses;
	}
	
	/**
	 * Parses the POST body to retrieve the username and password credentials.
	 * 		Basic Auth format: "Authorization: Basic [Base64 encoded : <Username:Password>]"
	 * @param rawCredentials	the raw POST body
	 * @return	a length 2 string array, where index 0 is the username and index 1 is the password.
	 */
	protected String[] parsePost(String rawCredentials) throws AuthenticationException
	{
		String[] parts = rawCredentials.split(" ");
		
		// decode the authentication string
		//String decoded = Base64.decode(String., arg1)
		
		return null;
		
	}

}

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

import org.apache.commons.codec.binary.Base64;

/**
 * BasicAuth implementation of the Authenticator.
 * 	BasicAuth: "Authorization: Basic [Base64:]username:password"
 * @author twack
 *
 */
public class BasicAuth extends Authenticator {
	
	public BasicAuth()
	{
		super("BasicAuth");
	}
	
	@Override
	protected String[] parsePost(String post) throws AuthenticationException 
	{		
		// format: ["Authorization:", "Basic", Base64-encoded credentials]
		String[] parts = post.split(" ");
		
		if(!isValidBasicAuth(parts))
		{
			throw new AuthenticationException();
		}
		
		byte[] decoded = Base64.decodeBase64(parts[2]);
		
		String[] credentials = (new String(decoded)).split(":"); // split decoded token username:password
		
		// check if the credential array has space for username and password elements.
		if(credentials.length != 2)
		{
			throw new AuthenticationException();
		}
		
		return credentials;
	}
	
	/**
	 * Inspects the authString and determines if it is a valid BasicAuth string.
	 * 	Checks if it has all 3 parts, then checks the validity of the parts.
	 * @param authString	the authorization string to be validated
	 * @return	true if valid, false otherwise.
	 */
	private boolean isValidBasicAuth(String[] authParts)
	{
		// check if the post string is in the correct format
		if((authParts.length != 3) || (!authParts[0].equals("Authorization:")) 
									|| (!authParts[1].equalsIgnoreCase("Basic")))
		{
			return false;
		}
		
		// check if the credential section is encoded properly
		if(!Base64.isBase64(authParts[2]))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Static utility for generating a BasicAuth token.
	 * 		Format: "Authorization: Basic " + [Base64Encoded]username:password
	 * @param username
	 * @param pass
	 * @return	a String containing a BasicAuth token for the given parameters.
	 */
	public static String generateBasicAuth(String username, String pass)
	{
		String authToken = "Authorization: Basic ";
		String credentials = username + ":" + pass;
		
		authToken += Base64.encodeBase64String(credentials.getBytes());
		
		return authToken;
	}

}

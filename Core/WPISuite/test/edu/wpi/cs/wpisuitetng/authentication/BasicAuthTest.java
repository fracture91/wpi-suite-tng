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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import edu.wpi.cs.wpisuitetng.authentication.BasicAuth;
import edu.wpi.cs.wpisuitetng.exceptions.AuthenticationException;

import org.apache.commons.codec.binary.Base64;

public class BasicAuthTest {
	String header = "Authorization: Basic "; // the static BasicAuth header
	BasicAuth basic;
	
	@Before
	public void setUp()
	{
		this.basic = new BasicAuth();
	}
	
	
	@Test(expected=AuthenticationException.class)
	/**
	 * When given an improperly formatted POST-string (not in 3 parts)
	 * 	then expect an exception
	 * @throws AuthenticationException
	 */
	public void testInvalidTokenFormat() throws AuthenticationException
	{
		this.basic.parsePost("bahston");
	}
	
	@Test(expected=AuthenticationException.class)
	/**
	 * Generates a valid BasicAuth token with invalidly formatted
	 * 	credentials. Decoding should work properly. An exception should
	 * 	raise because the credential string is not in the  "String:String" format.
	 */
	public void testInvalidCredentialFormat() throws AuthenticationException
	{
		String authToken = "Basic ";
		String credentials = "abcdefg123456";
		authToken += Base64.encodeBase64String(credentials.getBytes());
		
		this.basic.parsePost(authToken);
	}
	
	@Test
	/**
	 * Tests a situation when a valid BasicAuth token is given
	 * 	to the POST string parser.
	 * @throws AuthenticationException
	 */
	public void testValidFormat() throws AuthenticationException
	{
		String username = "twack";
		String pass = "12345";
		
		String basicAuthToken = BasicAuth.generateBasicAuth(username, pass);
		String[] credentials = this.basic.parsePost(basicAuthToken);
		
		assertEquals(credentials.length, 2);
		assertTrue(credentials[0].equals(username));
		assertTrue(credentials[1].equals(pass));
	}
	
	
}

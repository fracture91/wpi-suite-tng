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

import edu.wpi.cs.wpisuitetng.exceptions.AuthenticationException;

// decoding libraries
import org.apache.catalina.util.Base64;
import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.buf.CharChunk;

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
	protected String[] parsePost(String post) throws AuthenticationException {
		try 
		{
			String[] parts = post.split(" "); // split apart to get credentials in [2]
			
			// load the credentials into a ByteChunk
			ByteChunk encoded = new ByteChunk();
			byte[] postBytes = parts[2].getBytes(); // TODO: define charset
			encoded.append(postBytes, 0, postBytes.length);
			
			// decode the base64 credential bytes
			CharChunk decoded = new CharChunk();
			Base64.decode(encoded, decoded);
			
			String[] credentials = decoded.toString().split(":"); // split decoded token username:password
			
			return credentials;
		} 
		catch (IOException e) 
		{
			throw new AuthenticationException(); // decoding error
		}
	}

}

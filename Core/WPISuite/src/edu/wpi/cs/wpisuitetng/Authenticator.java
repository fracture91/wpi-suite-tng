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

/**
 * Provides a unified interface for encoded login credential authentication.
 * 
 * @author twack
 *
 */
public interface Authenticator {
	/**
	 * 	parses the credentials in POST to determine if the user has provided valid credentials.
	 * 		If user is found & password is correct, registers and returns a Session for the user.
	 * 		If the user is not found or the password is invalid, throws an AuthenticationException.
	 * @param postCredentials	the string in the POST-body of the request.
	 * @return	a Session for the given user
	 * @throws AuthenticationException	when the user is not found or provides a bad password.
	 */
	public Session login(String postCredentials) throws AuthenticationException;
}

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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A SHA-256 algorithm implementation of PasswordCryptographer.
 * 
 * @author twack
 *
 */
public class Sha256Password implements PasswordCryptographer {

	private MessageDigest hashDigest;
	
	/**
	 * 
	 * @throws NoSuchAlgorithmException	when the MessageDigest's SHA-256 implementation cannot be found.
	 */
	public Sha256Password()
	{
		try {
			this.hashDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO: this should be a fatal error, but I'm not sure how to handle it.
		}
	}
	
	@Override
	public String generateHash(String password) {
		// TODO: salt the password
		
		this.hashDigest.reset(); // flush the digest buffer before use
		try {
			this.hashDigest.update(password.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		byte[] hashedPassword = this.hashDigest.digest();
		
		return new String(hashedPassword);
	}

	@Override
	public boolean validate(String password, String hash) {
		String hashedPassword = this.generateHash(password);
		
		return hashedPassword.equals(hash);
	}

}

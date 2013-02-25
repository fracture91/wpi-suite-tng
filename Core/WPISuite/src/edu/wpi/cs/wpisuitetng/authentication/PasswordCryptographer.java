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

/**
 * Interface for password hashing and cryptography mechanism. This system has two 
 * 	use-cases:	(1) UserManager during User creation (to hash the desired password).
 * 				(2) Authentication system during password checking phase.
 * 				
 * 	Implementations of the interface determine the hash mechanism to use (e.g. sha-256, md5) and how
 * 		the salt is generated.
 * @author twack
 *
 */
public interface PasswordCryptographer {

	/**
	 * Generate a hashed string from the given password.
	 * 
	 * @param password	the user's desired password (plaintext)
	 * @return	a hashed & salted password to store in the database
	 */
	public String generateHash(String password);
	
	/**
	 * Validates if the given password matches the given hash.
	 * 
	 * @param password	the password credential given
	 * @param hashed	a hash string to compare against the password
	 * @return	true if the password matches once hashed.
	 */
	public boolean validate(String password, String hash);
}

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

package edu.wpi.cs.wpisuitetng.modules.core.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Unit test suite on the User class
 * @author twack
 *
 */
public class UserTest {

	User u1;
	User u2;
	User u3;
	User u4;
	
	@Before
	public void setUp()
	{
		u1 = new User("James Bond", "jbond", null, 7);
		u2 = new User("Money Penny", "mpenny", null, 2);
		u3 = new User("Q", "q", "secret", 1);
		u4 = new User("M", "m", null, 0);
	}
	
	@Test
	public void testEquals()
	{
		User dup3 = new User("Q", "q", "secret", 1);
		
		assertTrue(u3.equals(dup3));
		assertFalse(u3.equals(u1));
	}
	
	@Test
	/**
	 * User.getProject() should return null because Projects contain Users but 
	 * 	Users can be contained by many Projects.
	 */
	public void testGetProjectNull()
	{
		assertTrue(null == u1.getProject());
	}
	
	@Test
	public void testGetRole()
	{
		assertTrue(u1.getRole().equals(Role.USER));
	}
	
	@Test
	public void testSetRole()
	{
		assertTrue(u2.getRole().equals(Role.USER));
		
		u2.setRole(Role.ADMIN);
		
		assertTrue(u2.getRole().equals(Role.ADMIN));
	}
	
	@Test
	public void testMatchPassword()
	{
		String password = "secret";
		
		assertTrue(u3.matchPassword(password));
		
		assertFalse(u1.matchPassword(null));
		assertFalse(u1.matchPassword(password));
	}
	
	@Test
	public void testSetIdNum()
	{
		int newId = 5;
		
		assertEquals(7, u1.getIdNum());
		
		u1.setIdNum(newId);
		
		assertEquals(newId, u1.getIdNum());
	}
	
	@Test
	public void testSetName()
	{
		String newName = "Bond, James";
		
		assertTrue(u1.getName().equals("James Bond"));
		
		u1.setName(newName);
		
		assertTrue(u1.getName().equals(newName));
	}
	
	@Test
	public void testSetUsername()
	{
		String newUser = "queue";
		
		assertTrue(u3.getUsername().equals("q"));
		
		u3.setUserName(newUser);
		
		assertTrue(u3.getUsername().equals(newUser));
	}
	
	@Test
	public void testSetPassword()
	{
		String newPass = "password";
		
		assertFalse(u1.matchPassword(null));
		assertTrue(u3.matchPassword("secret"));
		
		u1.setPassword(newPass);
		u3.setPassword(newPass);
		
		assertTrue(u1.matchPassword(newPass));
		assertTrue(u3.matchPassword(newPass));
	}
	
	@Test
	public void testSerialize()
	{
		String serialized = u3.toJSON();
		
		assertTrue(serialized.startsWith("{"));
		assertTrue(serialized.endsWith("}"));
		
		assertFalse(serialized.contains("password"));
		assertTrue(serialized.contains("idNum"));
		assertTrue(serialized.contains("1"));
		assertTrue(serialized.contains("username"));
		assertTrue(serialized.contains("q"));
		assertTrue(serialized.contains("name"));
		assertTrue(serialized.contains("Q"));
	}
}

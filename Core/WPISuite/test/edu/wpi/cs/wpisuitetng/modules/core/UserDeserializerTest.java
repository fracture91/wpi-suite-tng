/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 			twack
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.core.models.UserDeserializer;

/**
 * Tests for the UserDeserializer class.
 * 	parsePassword() Tests: evaluating the flexibility of the function to handle JSON formatting.
 * @author twack
 *
 */
public class UserDeserializerTest {
	
	GsonBuilder gson;
	
	@Before
	public void setUp()
	{
		this.gson = new GsonBuilder();
		this.gson.registerTypeAdapter(User.class, new UserDeserializer());
	}
	
	@Test
	public void parsePasswordTest1()
	{
		String jsonUser = "{\"name\":\"Tyler\", \"password\":\"abc34;.\", \"username\":\"twack\"}";
		
		String parsedPassword = UserDeserializer.parsePassword(jsonUser);
		
		assertTrue(parsedPassword.equals("abc34;."));
	}
	
	@Test
	public void parsePasswordTest2()
	{
		String jsonUser = "{\"name\":\"Tyler\", \"password\": \"abc34;.\", \"username\":\"twack\"}";
		
		String parsedPassword = UserDeserializer.parsePassword(jsonUser);
		
		assertTrue(parsedPassword.equals("abc34;."));
	}
	
	@Test
	public void parsePasswordTest3()
	{
		String jsonUser = "{\"name\":\"Tyler\", \"password\":\"abc34;.\", \"username\":\"twack\"}";
		
		String parsedPassword = UserDeserializer.parsePassword(jsonUser);
		
		assertTrue(parsedPassword.equals("abc34;."));
	}
	
	@Test(expected=JsonParseException.class)
	public void parsePasswordNoPasswordTest()
	{
		String jsonUser ="{\"name\":\"Tyler\", \"username\":\"twack\"}";
		
		String parsedPassword = UserDeserializer.parsePassword(jsonUser); // expect Exception
	}
	
	@Test
	/**
	 * Tests deserializing when the given JSON string has all attributes.
	 */
	public void deserializeUserFull()
	{
		String jsonUser ="{\"name\":\"Tyler\", \"username\":\"twack\", \"idNum\":2, password:\"abcde\", \"role\":\"ADMIN\"}";
		Gson deserializer = this.gson.create();
		
		User inflated = deserializer.fromJson(jsonUser, User.class);
		
		assertEquals(2, inflated.getIdNum());
		assertTrue(inflated.getName().equals("Tyler"));
		assertTrue(inflated.getUsername().equals("twack"));
		assertTrue(inflated.getRole().equals(Role.ADMIN));
		
		assertTrue(inflated.matchPassword("abcde"));
		assertFalse(inflated.matchPassword(null));
	}
	
	@Test
	/**
	 * Tests User deserialization when the given string is missing fields (but has the unique identifier idNum)
	 */
	public void deserializeUserMissingFields()
	{
		String jsonUser ="{\"username\":\"Tyler\", \"idNum\":2}";
		Gson deserializer = this.gson.create();
		
		User inflated = deserializer.fromJson(jsonUser, User.class);
		
		assertEquals(2, inflated.getIdNum());
		assertTrue(inflated.getUsername().equals("Tyler"));
		
		assertFalse(inflated.matchPassword(null));
	}
	
	@Test(expected=JsonParseException.class)
	/**
	 * Tests error handling in the deserializer -- an exception should be thrown if
	 * 	the user fails to include the Unique Identifier field (User->username)
	 */
	public void deserializeUserMissingId()
	{
		String jsonUser ="{\"name\":\"Tyler\", \"idNum\":2, \"password\":\"abcde\"}";
		Gson deserializer = this.gson.create();
		
		User inflated = deserializer.fromJson(jsonUser, User.class); // exception expected.
		
		fail("exception not thrown");
	}
}

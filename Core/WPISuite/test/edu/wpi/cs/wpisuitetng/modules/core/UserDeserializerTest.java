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
import org.junit.Test;

import com.google.gson.JsonParseException;

import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserDeserializer;

/**
 * Tests for the UserDeserializer class.
 * 	parsePassword() Tests: evaluating the flexibility of the function to handle JSON formatting.
 * @author twack
 *
 */
public class UserDeserializerTest {
	
	@Test
	public void parsePasswordTest1()
	{
		String jsonUser = "{name:\"Tyler\", password:\"abc34;.\", username:\"twack\"}";
		
		String parsedPassword = UserDeserializer.parsePassword(jsonUser);
		
		assertTrue(parsedPassword.equals("abc34;."));
	}
	
	@Test
	public void parsePasswordTest2()
	{
		String jsonUser = "{name:\"Tyler\", password: \"abc34;.\", username:\"twack\"}";
		
		String parsedPassword = UserDeserializer.parsePassword(jsonUser);
		
		assertTrue(parsedPassword.equals("abc34;."));
	}
	
	@Test
	public void parsePasswordTest3()
	{
		String jsonUser = "{name:\"Tyler\", password:\"abc34;.\", username:\"twack\"}";
		
		String parsedPassword = UserDeserializer.parsePassword(jsonUser);
		
		assertTrue(parsedPassword.equals("abc34;."));
	}
	
	@Test(expected=JsonParseException.class)
	public void parsePasswordNoPasswordTest()
	{
		String jsonUser ="{name:\"Tyler\", username:\"twack\"}";
		
		String parsedPassword = UserDeserializer.parsePassword(jsonUser); // expect Exception
	}
}

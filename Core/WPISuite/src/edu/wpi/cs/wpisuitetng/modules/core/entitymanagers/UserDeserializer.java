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

package edu.wpi.cs.wpisuitetng.modules.core.entitymanagers;

import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * A custom deserializer class for the GSON JSON library.
 * 	The password field should not be exposed after inflation, so it is set to null.
 * @author twack
 *
 */
public class UserDeserializer implements JsonDeserializer<User> {

	
	@Override
	public User deserialize(JsonElement userElement, Type userType,
			JsonDeserializationContext context) throws JsonParseException {
		 JsonObject deflated = userElement.getAsJsonObject();
		 
		 // check for the unique identifier <idNum> field.
		 if(!deflated.has("idNum"))
		 {
			 throw new JsonParseException("The serialized User did not contain the required idNum field.");
		 }
		 
		 // for all other attributes: instantiate as null, fill in if given.
		 
		 int idNum = deflated.getAsInt();
		 String username = null;
		 String name = null;
		 
		 if(deflated.has("username"))
		 {
			 username = deflated.get("username").getAsString();
		 }
		 
		 if(deflated.has("name"))
		 {
			 name = deflated.get("name").getAsString();
		 }
		 
		 return new User(name, username, null, idNum);
	}
	
	/**
	 * This static utility method takes a JSON string and attempts to
	 * 	retrieve a password field from it.
	 * @param serializedUser	a JSON string containing a password
	 * @return	the password field parsed.
	 */
	public static String parsePassword(String serializedUser)
	{
		if(!serializedUser.contains("password"))
		{
			throw new JsonParseException("The given JSON string did not contain a password field.");
		}
		
		int fieldStartIndex = serializedUser.indexOf("password");
		int startIndex = serializedUser.indexOf('"', fieldStartIndex) + 1;
		int endIndex = serializedUser.indexOf('"', startIndex);
		
		String password = serializedUser.substring(startIndex, endIndex);
		
		return password;
	}

}

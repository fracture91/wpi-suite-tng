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

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class UserSerializer implements JsonSerializer<User> {

	@Override
	public JsonElement serialize(User u, Type t,
			JsonSerializationContext context) {
		JsonObject deflated = new JsonObject();
		
		deflated.addProperty("idNum", u.getIdNum());
		deflated.addProperty("username", u.getUsername());
		deflated.addProperty("name", u.getName());
		deflated.addProperty("role", u.getRole().toString());
		
		return deflated;
	}

}

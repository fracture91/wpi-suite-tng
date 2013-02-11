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

import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

/**
 * A custom deserializer class for the GSON JSON library.
 * @author bgaffey
 *
 */
public class ProjectDeserializer implements JsonDeserializer<Project> {

	
	@Override
	public Project deserialize(JsonElement projectElement, Type projectType,
			JsonDeserializationContext context) throws JsonParseException {
		 JsonObject deflated = projectElement.getAsJsonObject();
		 
		 // check for the unique identifier <idNum> field.
		 if(!deflated.has("idNum"))
		 {
			 throw new JsonParseException("The serialized Project did not contain the required idNum field.");
		 }
		 
		 // for all other attributes: instantiate as null, fill in if given.
		 
		 //int idNum = deflated.get("idNum").getAsInt();
		 String idNum = deflated.get("idNum").getAsString();
		 String name = null;
		 
		 if(deflated.has("name"))
		 {
			 name = deflated.get("name").getAsString();
		 }
		 
		 Project inflated = new Project(name, idNum);
		 
		 return inflated;
	}

}

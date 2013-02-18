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
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


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
		 User owner = null;
		
		 String ownerString = null;
		 User[] team = null;
		 String[] supportedModules = null;
		 
		 
		 if(deflated.has("name"))
		 {
			 name = deflated.get("name").getAsString();
		 }
		 
		 if(deflated.has("owner"))
		 {
			 owner = User.fromJSON(deflated.get("owner").getAsString());
		 }
		 
		 if(deflated.has("supportedModules"))
		 {
			 ArrayList<String> tempList = new ArrayList<String>();
			 JsonArray tempMods = deflated.get("supportedModules").getAsJsonArray();
			 for(JsonElement mod : tempMods)
			 {
				 tempList.add(mod.getAsString());
			 }
			 String[] tempSM = new String[1];
			 supportedModules = tempList.toArray(tempSM);
		 }
		 
		 Project inflated = new Project(name, idNum, owner, team, supportedModules);
		 
		 if(deflated.has("team"))
		 {
			 JsonArray tempTeam = deflated.get("team").getAsJsonArray();
			 for(JsonElement member : tempTeam)
			 {
				 inflated.addTeamMember(User.fromJSON(member.getAsString()));
			 }
			 
		 }
		 
		 return inflated;
	}

}

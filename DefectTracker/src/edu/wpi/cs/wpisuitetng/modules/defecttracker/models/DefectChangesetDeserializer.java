/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Custom JSON deserializer for the DefectChangeset class
 */
public class DefectChangesetDeserializer implements JsonDeserializer<DefectChangeset> {

	@Override
	public DefectChangeset deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		
		// hash map to hold the deserialized FieldChange objects
		HashMap<String, FieldChange<?>> changesMap = new HashMap<String, FieldChange<?>>();
		
		JsonObject changeSet = json.getAsJsonObject();
		if (changeSet.has("changes")) {
			JsonObject changes = changeSet.get("changes").getAsJsonObject();
			if (changes.has("title")) {
				JsonObject titleObj = changes.get("title").getAsJsonObject();
				String oldTitle = context.deserialize(titleObj.get("oldValue"), String.class);
				String newTitle = context.deserialize(titleObj.get("newValue"), String.class);
				changesMap.put("title", new FieldChange<String>(oldTitle, newTitle));
			}
			if (changes.has("description")) {
				JsonObject descriptionObj = changes.get("description").getAsJsonObject();
				String oldDesc = context.deserialize(descriptionObj.get("oldValue"), String.class);
				String newDesc = context.deserialize(descriptionObj.get("newValue"), String.class);
				changesMap.put("description", new FieldChange<String>(oldDesc, newDesc));
			}
			if (changes.has("assignee")) {
				JsonObject assigneeObj = changes.get("assignee").getAsJsonObject();
				User oldUser = context.deserialize(assigneeObj.get("oldValue"), User.class);
				User newUser = context.deserialize(assigneeObj.get("newValue"), User.class);
				changesMap.put("assignee", new FieldChange<User>(oldUser, newUser));
			}
			if (changes.has("tags")) {
				JsonObject tagsObj = changes.get("tags").getAsJsonObject();
				Tag[] oldTags = context.deserialize(tagsObj.get("oldValue"), Tag[].class);
				Tag[] newTags = context.deserialize(tagsObj.get("newValue"), Tag[].class);
				changesMap.put("tags", new FieldChange<Set<Tag>>(new HashSet<Tag>(new ArrayList<Tag>(Arrays.asList(oldTags))), new HashSet<Tag>(new ArrayList<Tag>(Arrays.asList(newTags)))));
			}
			if (changes.has("status")) {
				JsonObject statusObj = changes.get("status").getAsJsonObject();
				DefectStatus oldStatus = context.deserialize(statusObj.get("oldValue"), DefectStatus.class);
				DefectStatus newStatus = context.deserialize(statusObj.get("newValue"), DefectStatus.class);
				changesMap.put("status", new FieldChange<DefectStatus>(oldStatus, newStatus));
			}
			
			// reconstruct the DefectChangeset
			DefectChangeset retVal = new DefectChangeset();
			retVal.setChanges(changesMap);
			retVal.setDate((Date)(context.deserialize(changeSet.get("date"), Date.class)));
			retVal.setUser((User)(context.deserialize(changeSet.get("user"), User.class)));
			
			// return the DefectChangeset
			return retVal;
		}
		else {
			throw new JsonParseException("DefectChangeset type is unrecognized");
		}
	}
}

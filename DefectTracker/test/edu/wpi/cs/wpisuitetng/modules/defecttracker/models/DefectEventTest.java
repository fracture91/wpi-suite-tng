/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DefectEventTest {

	Gson gson;
	DefectEvent changeset;
	DefectEvent comment;
	GsonBuilder builder;
	
	@Before
	public void setUp() {
		gson = new Gson();
		changeset = new DefectChangeset();
		comment = new Comment();
		builder = new GsonBuilder();
		DefectEvent.addGsonDependencies(builder);
	}
	
	@Test
	public void testDeserialization() {
		Date date = new Date(1000);
		changeset.setDate(date);
		comment.setDate(date);
		
		String changesetJson = gson.toJson(changeset);
		String commentJson = gson.toJson(comment);
		
		gson = builder.create();
		
		DefectEvent newEvent = gson.fromJson(changesetJson, DefectEvent.class);
		assertTrue(newEvent instanceof DefectChangeset);
		assertEquals(date, newEvent.getDate());
		
		newEvent = gson.fromJson(commentJson, DefectEvent.class);
		assertTrue(newEvent instanceof Comment);
		assertEquals(date, newEvent.getDate());
	}
	
	@Test(expected=JsonParseException.class)
	public void testNoType() {
		changeset.type = null; // null fields don't show up in json at all
		String json = gson.toJson(changeset);
		gson = builder.create();
		changeset = gson.fromJson(json, DefectEvent.class);
	}
	
	@Test(expected=JsonParseException.class)
	public void testBadType() {
		JsonElement element = gson.toJsonTree(changeset);
		element.getAsJsonObject().addProperty("type", "what");
		String json = gson.toJson(element);
		gson = builder.create();
		changeset = gson.fromJson(json, DefectEvent.class);
	}
	
	@Test(expected=JsonParseException.class)
	public void testNullType() {
		gson = (new GsonBuilder()).serializeNulls().create();
		changeset.type = null;
		String json = gson.toJson(changeset, DefectEvent.class);
		gson = builder.create();
		changeset = gson.fromJson(json, DefectEvent.class);
	}

}

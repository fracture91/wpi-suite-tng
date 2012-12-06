/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    twack - update tests
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.mockobjects.MockDataStore;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.ProjectManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

public class ProjectManagerTest {

	ProjectManager test;
	Project temp;
	Gson json;
	
	@Before
	public void setUp()
	{
		test = new ProjectManager(MockDataStore.getMockDataStore());
		temp = new Project("0", "proj0");
		json = new Gson();
	}
	
	@Test
	/**
	 * Tests if the update() function properly maps the JSON string then applies
	 * 	the changes to the given User.
	 * @throws WPISuiteException
	 */
	public void testUpdate() throws WPISuiteException
	{
		Session ses = null;
		String updateString = "{ \"idNum\": \"2\", \"name\": \"proj2\" }";
		Project oldTemp = this.temp;
		
		Project newTemp = this.test.update(ses, temp, updateString);
		
		// TODO: find a way to retrieve the User from storage to run assertions on.
		
		assertTrue(newTemp.getIdNum().equals("2"));
		assertTrue(newTemp.getName().equals("proj2"));
	}
	
	@Test(expected = WPISuiteException.class)
	/**
	 * Tests failure in update's ObjectMapper. 
	 * @throws WPISuiteException	on success
	 */
	public void testUpdateFailure() throws WPISuiteException
	{
		Session ses = null;
		String updateString = "{ \"idNum\": \"2\", \"name\": \"proj2\",,,,,,,,,,, }"; // extra commas cause problems in ObjectMapper
		
		Project newTemp = this.test.update(ses, temp, updateString);	// EXCEPTION SHOULD THROW HERE
		
		fail("Exception should have been thrown");
	}
}

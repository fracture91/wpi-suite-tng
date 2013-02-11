/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    rchamer
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class Db4oDatabaseTest {
	

	@Test
	public void testSaveandRetrieve() throws WPISuiteException {
		Data db = DataStore.getDataStore();
		User[] arr = new User[1];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		db.save(firstUser);
		User me = db.retrieve(User.class, "username", "rchamer").toArray(arr)[0];
		assertEquals(me, firstUser);
		db.delete(me);
	}
	
	@Test
	public void testDelete() throws WPISuiteException{
		Data db = DataStore.getDataStore();
		User[] arr = new User[1];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		db.save(firstUser);
		db.delete(firstUser);
		User me = db.retrieve(User.class, "username", "rchamer").toArray(arr)[0];
		assertEquals(me, null);
	}
	
	@Test
	public void testUpdate() throws WPISuiteException{
		Data db = DataStore.getDataStore();
		User[] arr = new User[2];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		db.save(firstUser);
		db.update(User.class, "username", "rchamer", "name", "Mjolnir");
		User Mjolnir = db.retrieve(User.class, "username", "rchamer").toArray(arr)[0];
		assertEquals(firstUser, Mjolnir);
		db.delete(Mjolnir);
		
		
	}
	
	@Test
	public void testRetrieveAll(){
		Data db = DataStore.getDataStore();
		User firstUser = new User("Brian", "bgaffey", "password", 0);
		db.save(firstUser);
		User secondUser = new User("Gaffey", "gafftron", "password", 0);
		db.save(secondUser);
		List<User> retrievedList = db.retrieveAll(firstUser);
		
		int initCount = retrievedList.size();
		assertTrue(retrievedList.contains(firstUser));
		assertTrue(retrievedList.contains(secondUser));
		
		db.delete(firstUser);
		retrievedList = db.retrieveAll(firstUser);
		
		assertEquals(initCount - 1, retrievedList.size());
		assertTrue(retrievedList.contains(secondUser));
		assertFalse(retrievedList.contains(firstUser));
		
		db.deleteAll(firstUser);
	}
	
	@Test
	public void testDeleteAll() throws WPISuiteException{
		Data db = DataStore.getDataStore();
		User[] arr = new User[2];
		User firstUser = new User("Brian", "bgaffey", "password", 0);
		db.save(firstUser);
		User secondUser = new User("Gaffey", "gafftron", "password", 0);
		db.save(secondUser);
		List<User> retrievedList = db.retrieveAll(firstUser);
		
		int initCount = retrievedList.size();
		assertTrue(retrievedList.contains(firstUser));
		assertTrue(retrievedList.contains(secondUser));
		
		retrievedList = db.deleteAll(firstUser);
		User me1 = db.retrieve(User.class, "username", "bgaffey").toArray(arr)[0];
		User me2 = db.retrieve(User.class, "username", "gafftron").toArray(arr)[0];
		assertEquals(initCount, retrievedList.size());
		assertEquals(me1, null);
		assertEquals(me2, null);
	}
	
	@Test
	public void testRetrieveWithProjects() throws WPISuiteException{
		Data db = DataStore.getDataStore();
		User[] arr = new User[2];
		User firstUser = new User("Brian", "bgaffey", "password", 0);
		User secondUser = new User("Alex", "alex", "password1", 1);
		Project myProject = new Project("myProject", "0");
		Project notMyProject = new Project("notMyProject", "1");
		db.save(firstUser);
		db.save(myProject);
//		User result = db.retrieve(User.class, "username", "bgaffey", myProject).toArray(arr)[0];
	//	assertEquals(firstUser, result);
		db.deleteAll(firstUser);
		db.deleteAll(myProject);	
	}

}

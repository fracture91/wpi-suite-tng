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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectChangeset;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectStatus;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.FieldChange;

public class Db4oDatabaseTest {
	

	@Test
	public void testSaveandRetrieve() throws WPISuiteException {
		Data db = DataStore.getTestDataStore();
		User[] arr = new User[1];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		db.save(firstUser);
		User me = db.retrieve(User.class, "username", "rchamer").toArray(arr)[0];
		assertEquals(me, firstUser);
		db.delete(me);
	}
	
	@Test
	public void testDelete() throws WPISuiteException{
		Data db = DataStore.getTestDataStore();
		User[] arr = new User[1];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		db.save(firstUser);
		db.delete(firstUser);
		User me = db.retrieve(User.class, "username", "rchamer").toArray(arr)[0];
		assertEquals(me, null);
	}
	
	@Test
	public void testUpdate() throws WPISuiteException{
		Data db = DataStore.getTestDataStore();
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
		Data db = DataStore.getTestDataStore();
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
		Data db = DataStore.getTestDataStore();
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
		Data db = DataStore.getTestDataStore();
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
	
	@Test
	public void testOrRetrieve() throws WPISuiteException, IllegalAccessException, InvocationTargetException{
		Data db = DataStore.getTestDataStore();
		
		User[] arr = new User[2];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		User secondUser = new User("Bryan", "bgaffey", "pword", 1);
		List<User> both = new ArrayList<User>();
		both.add(firstUser);
		both.add(secondUser);
		db.deleteAll(firstUser);
		db.save(firstUser);
		db.save(secondUser);
		String[] list = new String[2];
		list[0] = "Username";
		list[1] = "Name";
		List<Object> objlist = new ArrayList<Object>();
		objlist.add("rchamer");
		objlist.add("Bryan");
		List<Model> me = db.orRetrieve(firstUser.getClass(), list, objlist);
		assertEquals(me, both);
	}
	
	@Test
	public void testAndRetrieve() throws WPISuiteException, IllegalAccessException, InvocationTargetException{ 
	
		Data db = DataStore.getTestDataStore();
		
		User[] arr = new User[2];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		User secondUser = new User("Bryan", "rchamer", "pword", 1);
		List<User> first = new ArrayList<User>();
		first.add(firstUser);
		db.deleteAll(firstUser);
		db.save(firstUser);
		db.save(secondUser);
		String[] list = new String[2];
		list[0] = "Username";
		list[1] = "Name";
		List<Object> objlist = new ArrayList<Object>();
		objlist.add("rchamer");
		objlist.add("Ryan");
		List<Model> me = db.andRetrieve(firstUser.getClass(), list, objlist);
		assertEquals(me, first);
	}
	
	@Test
	public void testComplexRetrieve() throws WPISuiteException, IllegalAccessException, InvocationTargetException{
Data db = DataStore.getTestDataStore();
		
		
		User[] arr = new User[2];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		User secondUser = new User("Bryan", "rchamer", "pword", 1);
		User thirdUser = new User("Tyler", "twack", "word", 2);
		List<User> first = new ArrayList<User>();
		db.deleteAll(firstUser);
		first.add(firstUser);
		first.add(thirdUser);
		db.deleteAll(firstUser);
		db.save(firstUser);
		db.save(secondUser);
		db.save(thirdUser);
		String[] list = new String[2];
		list[0] = "Username";
		list[1] = "Name";
		List<Object> objlist = new ArrayList<Object>();
		objlist.add("rchamer");
		objlist.add("Ryan");
		
		String[] orList = new String[2];
		orList[0] = "idNum";
		orList[1] = "Name";
		List<Object> orObjList = new ArrayList<Object>();
		orObjList.add(0);
		orObjList.add("Tyler");
		
		List<Model> me = db.complexRetrieve(firstUser.getClass(), list, objlist, firstUser.getClass(), orList, orObjList);
		assertEquals(me, first);
	}
	
	public class Person{
		public String name;
		public Person boss;
		public int id;
		
		public Person(String aName, Person aBoss, int anId){
			name = aName;
			boss = aBoss;
			id = anId;
			
		}
		
		public String getName(){
			return name;
			
		}
		
		public Person getBoss(){
			return boss;
		}
		
		public int getId(){
			return id;
		}
		
	}
	
	@Test
	public void testSaveAndRetrieveDbDepth() throws WPISuiteException{
		Data db = DataStore.getTestDataStore();
		
		/*
		Person per1 = new Person("One", null,1);
		Person per2 = new Person("Two", null,2);
		Person per3 = new Person("Three", null,3);
		Person per4 = new Person("Four", null,4);
		Person per5 = new Person("Five", null,5);
		Person per6 = new Person("Six", null,6);
		Person per7 = new Person("Seven", null,7);
		
		per1.boss = per2;
		per2.boss = per3;
		per3.boss = per4;
		per4.boss = per5;
		per5.boss = per6;
		per6.boss = per7;
		
		db.deleteAll(per1);
		db.save(per1);
		db.retrieve(per1.getClass(), "id", 2);
		return;
		*/
		
		Defect def1 = new Defect();
		db.deleteAll(def1);
		List<DefectEvent> cs1 = new ArrayList<DefectEvent>();
		DefectChangeset ev1 = new DefectChangeset();
		ev1.setDate(new Date());
		User Joe = new User("Joe", "j", "pword", 0);
		FieldChange<Integer> fc = new FieldChange<Integer>(-1, 5);
		FieldChange<Integer> fc2 = new FieldChange<Integer>(5, 10);
		FieldChange<Integer> fc3 = new FieldChange<Integer>(10, 15);
		FieldChange<Integer> fc4 = new FieldChange<Integer>(15, 20);
		FieldChange<Integer> fc5 = new FieldChange<Integer>(20, 25);
		FieldChange<Integer> fc6 = new FieldChange<Integer>(25, 30);
		FieldChange<Date> fc8 = new FieldChange<Date>(def1.getCreationDate(), new Date());
		FieldChange<User> fc9 = new FieldChange<User>(def1.getCreator(), Joe);
		FieldChange<String> fc10 = new FieldChange<String>(def1.getTitle(), "New one");
		FieldChange<String> fc11 = new FieldChange<String>(def1.getDescription(), "New desc");
		DefectStatus df1 = DefectStatus.CONFIRMED;
		DefectStatus df2 = DefectStatus.NEW;
		FieldChange<DefectStatus> fc7 = new FieldChange<DefectStatus>(df2, df1);
		HashMap<String, FieldChange<?>> changes = new HashMap<String, FieldChange<?>>();
		changes.put("id", fc);
		changes.put("id", fc2);
		changes.put("id", fc3);
		changes.put("id", fc4);
		changes.put("id", fc5);
		changes.put("id", fc6);
		changes.put("Status", fc7);
		changes.put("lastModifiedDate", fc8);
		changes.put("creationDate", fc8);
		changes.put("creator", fc9);
		changes.put("assignee", fc9);
		changes.put("title", fc10);
		changes.put("description", fc11);
		ev1.setChanges(changes);
		cs1.add(ev1);
		def1.setDescription("Test");
		def1.setEvents(cs1);
		db.save(def1);
		db.retrieve(def1.getClass(), "id", -1);
		
	}

}

/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mpdelladonna
 *    twack - update test
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.core;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.mockobjects.MockDataStore;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class UserManagerTest {

	UserManager test;
	UserManager testWithRealDB;
	User temp;
	User admin;
	User secondUser;
	User conflict;
	Gson json;
	Session tempSession;
	Session adminSession;
	String mockSsid = "abc123";
	
	@Before
	public void setUp()
	{
		test = new UserManager(MockDataStore.getMockDataStore());
		testWithRealDB = new UserManager(DataStore.getDataStore());
		temp = new User("test","test","test",0);
		secondUser = new User ("Sam", "sammy","trouty", 1);
		conflict = new User("steve", "steve",null, 0);
		tempSession = new Session(temp, mockSsid);
		admin = new User("adam","adam","password",4);
		admin.setRole(Role.ADMIN);
		adminSession = new Session(admin, mockSsid);
		json = new Gson();
	}
	
	
	
	@Test
	public void testMakeEntity() {
		User u = null;
		
		String jsonUser = temp.toJSON();
		jsonUser = jsonUser.substring(0, jsonUser.length() - 1);
		jsonUser += ", \"password\":\"abcde\"}";
		System.out.println(jsonUser);
		
		try {
			u = test.makeEntity(new Session(temp, mockSsid), jsonUser);
		} catch (WPISuiteException e) {
			fail("unexpected exception");
		}
		
		assertTrue(u.equals(temp));
	}
	
	@Test(expected = ConflictException.class)
	public void testMakeEntityExists() throws WPISuiteException {
		test.makeEntity(tempSession, json.toJson(conflict, User.class));
	}
	
	@Test(expected = BadRequestException.class)
	public void testMakeEntityBadJson() throws WPISuiteException {
		test.makeEntity(tempSession, "Garbage");
	}

	@Ignore
	public void testGetEntitySessionString() {
		fail("GetAll is not yet implemented");
	}

	@Test(expected = NotFoundException.class)
	public void testGetEntityStringEmptyString() throws NotFoundException, WPISuiteException {
		test.getEntity("");
	}
	
	@Test
	public void testGetEntityStringUserExists()throws WPISuiteException {
		User[] u = null;
		try {
			u = test.getEntity("steve");
		} catch (NotFoundException e) {
			fail("unexpected exception");
		}
		assertEquals(conflict, u[0]);
	}
	
	@Test(expected = NotFoundException.class)
	public void testGetEntityStringUserDNE() throws NotFoundException, WPISuiteException {
		test.getEntity("jefferythegiraffe");
	}

	@Test
	@Ignore
	public void testGetAll() throws WPISuiteException {
		User[] initList = testWithRealDB.getAll(new Session(temp, mockSsid));
		int initCount = initList.length;
		
		testWithRealDB.save(tempSession, temp);
		testWithRealDB.save(tempSession, secondUser);
		User[] myList = testWithRealDB.getAll(new Session(temp, mockSsid));
		assertEquals(initCount + 2, myList.length);
		testWithRealDB.deleteAll(new Session(temp, mockSsid));
	}

	@Test(expected = WPISuiteException.class)
	public void testSaveFail() throws WPISuiteException {
		new UserManager(new Data(){

			@Override
			public <T> boolean save(T aModel) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public <T> boolean save(T aModel, Project aProject) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public List<Model> retrieve(Class anObjectQueried,
					String aFieldName, Object theGivenValue)
					throws WPISuiteException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<Model> retrieve(Class anObjectQueried,
					String aFieldName, Object theGivenValue, Project theProject)
					throws WPISuiteException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T> T delete(T aTNG) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void update(Class anObjectToBeModified, String fieldName,
					Object uniqueID, String changeField, Object changeValue)
					throws WPISuiteException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public <T> List<T> retrieveAll(T aSample) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T> List<Model> retrieveAll(T aSample, Project aProject) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T> List<T> deleteAll(T aSample) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T> List<Model> deleteAll(T aSample, Project aProject) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<Model> andRetrieve(Class anObjectQueried,
					String[] aFieldNameList, List<Object> theGivenValueList)
					throws WPISuiteException, IllegalArgumentException,
					IllegalAccessException, InvocationTargetException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<Model> orRetrieve(Class anObjectQueried,
					String[] aFieldNameList, List<Object> theGivenValueList)
					throws WPISuiteException, IllegalAccessException,
					InvocationTargetException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<Model> complexRetrieve(Class andanObjectQueried,
					String[] andFieldNameList, List<Object> andGivenValueList,
					Class orAnObjectQueried, String[] orFieldNameList,
					List<Object> orGivenValueList) throws WPISuiteException,
					IllegalArgumentException, IllegalAccessException,
					InvocationTargetException {
				// TODO Auto-generated method stub
				return null;
			}}
		).save(null, null);
	}

	@Test
	public void testDeleteEntityFail() throws WPISuiteException {
		new UserManager(new Data(){
			@Override
			public <T> boolean save(T aTNG) {return false;}
			@Override
			public List<Model> retrieve(Class anObjectQueried,String aFieldName, Object theGivenValue) {
				List<Model> a = new ArrayList<Model>();
				a.add(temp);
				return a;
				}
			@Override
			public <T> T delete(T aTNG) {return null;}
			@Override
			public void update(Class anObjectToBeModified, String fieldName,Object uniqueID, String changeField, Object changeValue) {}
			@Override
			public <T> List<T> retrieveAll(T arg0) {
				return null;
			}
			@Override
			public <T> List<T> deleteAll(T aSample) {
				return null;
			}
			@Override

			public List<Model> retrieve(Class anObjectQueried,
					String aFieldName, Object theGivenValue, Project theProject)
					throws WPISuiteException {
				// TODO Auto-generated method stub
				return null;
			}
			public List<Model> notRetrieve(Class anObjectQueried,
					String aFieldName, Object theGivenValue) {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public <T> boolean save(T aModel, Project aProject) {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			public <T> List<Model> retrieveAll(T aSample, Project aProject) {
				// TODO Auto-generated method stub
				return null;
			}
			public List<Model> andRetrieve(Class anObjectQueried,
					String[] aFieldNameList, List<Object> theGivenValueList)
					throws WPISuiteException, IllegalArgumentException,
					IllegalAccessException, InvocationTargetException {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public List<Model> orRetrieve(Class anObjectQueried,
					String[] aFieldNameList, List<Object> theGivenValueList)
					throws WPISuiteException, IllegalAccessException,
					InvocationTargetException {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public <T> List<Model> deleteAll(T aSample, Project aProject) {
				// TODO Auto-generated method stub
				return null;
			}
			public List<Model> complexRetrieve(Class andanObjectQueried,
					String[] andFieldNameList, List<Object> andGivenValueList,
					Class orAnObjectQueried, String[] orFieldNameList,
					List<Object> orGivenValueList) throws WPISuiteException,
					IllegalArgumentException, IllegalAccessException,
					InvocationTargetException {
				// TODO Auto-generated method stub
				return null;
			}
			}
		).deleteEntity(adminSession, temp.getUsername());
	}
	
	@Test
	public void testDeleteEntity() throws WPISuiteException
	{
		new UserManager(new Data(){
			@Override
			public <T> boolean save(T aTNG) {return false;}
			@Override
			public List<Model> retrieve(Class anObjectQueried,String aFieldName, Object theGivenValue) {
				List<Model> a = new ArrayList<Model>();
				a.add(temp);
				return a;}
			@Override
			public <T> T delete(T aTNG) {return aTNG;}
			@Override
			public void update(Class anObjectToBeModified, String fieldName,Object uniqueID, String changeField, Object changeValue) {}
			@Override
			public <T> List<T> retrieveAll(T arg0) {
				return null;
			}
			@Override
			public <T> List<T> deleteAll(T aSample) {
				return null;
			}
			@Override
			public List<Model> retrieve(Class anObjectQueried,
					String aFieldName, Object theGivenValue, Project theProject)
					throws WPISuiteException {
				// TODO Auto-generated method stub
				return null;
			}
			public List<Model> notRetrieve(Class anObjectQueried,
					String aFieldName, Object theGivenValue) {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public <T> boolean save(T aModel, Project aProject) {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			public <T> List<Model> retrieveAll(T aSample, Project aProject) {
				// TODO Auto-generated method stub
				return null;
			}
			public List<Model> andRetrieve(Class anObjectQueried,
					String[] aFieldNameList, List<Object> theGivenValueList)
					throws WPISuiteException, IllegalArgumentException,
					IllegalAccessException, InvocationTargetException {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public List<Model> orRetrieve(Class anObjectQueried,
					String[] aFieldNameList, List<Object> theGivenValueList)
					throws WPISuiteException, IllegalAccessException,
					InvocationTargetException {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public <T> List<Model> deleteAll(T aSample, Project aProject) {
				// TODO Auto-generated method stub
				return null;
			}
			public List<Model> complexRetrieve(Class andanObjectQueried,
					String[] andFieldNameList, List<Object> andGivenValueList,
					Class orAnObjectQueried, String[] orFieldNameList,
					List<Object> orGivenValueList) throws WPISuiteException,
					IllegalArgumentException, IllegalAccessException,
					InvocationTargetException {
				// TODO Auto-generated method stub
				return null;
			}
			}
		).deleteEntity(adminSession, temp.getUsername());
	}

	@Test
	public void testDeleteAll() throws WPISuiteException {
		testWithRealDB.save(tempSession, temp);
		testWithRealDB.save(tempSession, secondUser);
		User[] myList = testWithRealDB.getAll(new Session(temp, mockSsid));
		testWithRealDB.deleteAll(new Session(temp, mockSsid));
		
		myList = testWithRealDB.getAll(new Session(temp, mockSsid));
		assertEquals(1, myList.length);
		assertEquals(myList[0], null);
	}

	@Ignore
	public void testCount() {
		fail("Not yet implemented");
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
		String updateString = "{ \"idNum\": 99, \"role\":\"ADMIN\",  \"username\": \"zach\", \"name\": \"zach\" }";
		User newTemp = this.test.update(adminSession, temp, updateString);
		
		// TODO: find a way to retrieve the User from storage to run assertions on.
		
		assertEquals(99, newTemp.getIdNum());
		assertEquals(newTemp.getRole(), Role.ADMIN);
		assertTrue(newTemp.getName().equals("zach"));
	}
	
	@Test(expected = WPISuiteException.class)
	/**
	 * Tests failure in update's ObjectMapper. 
	 * @throws WPISuiteException	on success
	 */
	public void testUpdateFailure() throws WPISuiteException
	{
		Session ses = null;
		String updateString = "{ \"idNum\": 99,  \"username\": \"updated\", \"role\": \"ADMIN\",  \"name\": \"zach\",,,,,,,,,,, }"; // extra commas cause problems in ObjectMapper
		
		this.test.update(ses, temp, updateString);
		
		fail("Exception should have been thrown");
	}

}

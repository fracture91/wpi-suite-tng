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
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.exceptions.AuthenticationException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.mockobjects.MockDataStore;
import edu.wpi.cs.wpisuitetng.mockobjects.MockSessionManager;
import edu.wpi.cs.wpisuitetng.mockobjects.MockUserManager;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * @author Mike
 *
 */
public class ManagerLayerTest {

	public String[] testUserArgs = {"core","user",""};
	public String[] testUserArgsFake = {"core","user","fake"};
	public String[] testUserArgsFakeDNE = {"core","user","steve"};
	@SuppressWarnings("rawtypes")
	public Map<String, EntityManager> testMap = new HashMap<String, EntityManager>();
	public User fake, uniqueFake;
	public User[] fakeList;
	public User[] doubleFakeList;
	public MockSessionManager sesMan;
	public ManagerLayer testManagerLayer;
	public Cookie[] testCookies = new Cookie[1]; 
	public Gson gson = new Gson();
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		fake = new User("fake","fake","fake", 0);
		uniqueFake = new User("asdf","asdf","asdf", 0);
		fakeList = new User[1];
		doubleFakeList = new User[2];
		fakeList[0] = fake;
		doubleFakeList[0] = fake;
		doubleFakeList[1] = fake;
		sesMan = new MockSessionManager(fake);
		testMap.put("coreuser", new MockUserManager(MockDataStore.getMockDataStore(), fake));
		testManagerLayer = ManagerLayer.getTestInstance(testMap, sesMan);
		testCookies[0] = sesMan.getTestSession().toCookie();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#getUsers(java.lang.String)}.
	 */
	@Test
	public void testGetUsersString() 
	{
		//MockUserManager returns User[0] = new User("fake",id,id, 0)
		User[] u = null;
		try {
			u = testManagerLayer.getUsers("fake");
		} catch (WPISuiteException e) {
			fail("unexpected exception");
		}
		assertEquals(u[0].getName(), "fake");
		assertEquals(u[0].getUsername(), "fake");
	}

	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#getUsers()}.
	 */
	@Test
	public void testGetUsers() {
		assertTrue(testManagerLayer.getUsers() instanceof MockUserManager);
	}

	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#read(java.lang.String[], javax.servlet.http.Cookie[])}.
	 * @throws WPISuiteException 
	 */
	@Test(expected = AuthenticationException.class)
	public void testReadNoCookieOneUserExists() throws WPISuiteException 
	{
		//test case with no cookie where asking for a user that exists.
		testManagerLayer.read(testUserArgsFake, null);
		
		
	}
	
	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#read(java.lang.String[], javax.servlet.http.Cookie[])}.
	 * @throws WPISuiteException 
	 */
	@Test(expected = AuthenticationException.class)
	public void testReadNoCookieOneUserDNE() throws WPISuiteException 
	{
		//test case with no cookie where asking for a user that does not exists.
		testManagerLayer.read(testUserArgsFakeDNE, null);
		
		
	}
	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#read(java.lang.String[], javax.servlet.http.Cookie[])}.
	 * @throws WPISuiteException 
	 */
	@Test(expected = AuthenticationException.class)
	public void testReadNoCookieAllUsers() throws WPISuiteException 
	{
		//test case with no cookie where asking for all users.
		testManagerLayer.read(testUserArgs, null);
		
	}
	
	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#read(java.lang.String[], javax.servlet.http.Cookie[])}.
	 * @throws WPISuiteException 
	 */
	@Test
	public void testReadOneUserExists()
	{
		String s = null;
		//test case where asking for one users.
		try {
			s = testManagerLayer.read(testUserArgsFake, testCookies);
		} catch (WPISuiteException e) {
			fail("Unexpected exception");
		}
		
		String response = "null";
		
		if(fakeList != null)
		{
			response = "[";
			for(Model n : fakeList)
			{
				response = response.concat(n.toJSON()+",");
			}
			response = response.substring(0, response.length() - 1); // remove trailing comma
			response = response.concat("]");
		}
		assertEquals(s,response);
	}
	
	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#read(java.lang.String[], javax.servlet.http.Cookie[])}.
	 * @throws WPISuiteException 
	 */
	@Test
	public void testReadOneUserDNE()
	{
		String s = null;
		//test case where asking for one users.
		try {
			s = testManagerLayer.read(testUserArgsFakeDNE, testCookies);
		} catch (WPISuiteException e) {
			fail("Unexpected exception");
		}
		
		assertEquals(s,"null");
	}
	
	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#read(java.lang.String[], javax.servlet.http.Cookie[])}.
	 * @throws WPISuiteException 
	 */
	@Test
	@Ignore // TODO: ahurle fixed a bug and updated this test, but it never worked in the first place
	public void testReadAllUsers()
	{
		String s = null;
		//test case where asking for one users.
		try {
			s = testManagerLayer.read(testUserArgs, testCookies);
		} catch (WPISuiteException e) {
			fail("Unexpected exception");
		}
		System.out.println("read all the users: "+s);
		
		String response = "null";
		
		if(doubleFakeList != null)
		{
			response = "[";
			for(Model n : doubleFakeList)
			{
				response = response.concat(n.toJSON()+",");
			}
			response = response.substring(0, response.length() - 1); // remove trailing comma
			response = response.concat("]");
		}
		assertEquals(s,response);
	}
	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#create(java.lang.String[], java.lang.String, javax.servlet.http.Cookie[])}.
	 */
	@Test
	public void testCreate() 
	{
		String s = null;
		//test case where asking for one users.
		try {
			s = testManagerLayer.create(testUserArgsFake,gson.toJson(uniqueFake,uniqueFake.getClass()) , testCookies);
		} catch (WPISuiteException e) {
			fail("Unexpected exception");
		}
		
		assertEquals(s,uniqueFake.toJSON());
	}
	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#create(java.lang.String[], java.lang.String, javax.servlet.http.Cookie[])}.
	 * @throws WPISuiteException 
	 */
	@Test(expected = AuthenticationException.class)
	public void testCreateNoCookie() throws WPISuiteException
	{
		testManagerLayer.create(testUserArgsFake,gson.toJson(fake,fake.getClass()) , null);
	}

	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#update(java.lang.String[], java.lang.String, javax.servlet.http.Cookie[])}.
	 */
	@Test
	public void testUpdate() {
		String s = null;
		//test case where asking for one users.
		try {
			s = testManagerLayer.update(testUserArgsFake,gson.toJson(uniqueFake,uniqueFake.getClass()) , testCookies);
		} catch (WPISuiteException e) {
			fail("Unexpected exception");
		}
		
		assertEquals(s,uniqueFake.toJSON());
	}
	
	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#update(java.lang.String[], java.lang.String, javax.servlet.http.Cookie[])}.
	 * @throws WPISuiteException 
	 */
	@Test(expected = AuthenticationException.class)
	public void testUpdateNoCookie() throws WPISuiteException {
		testManagerLayer.update(testUserArgsFake,gson.toJson(fake,fake.getClass()) , null);
	}

	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#delete(java.lang.String[], javax.servlet.http.Cookie[])}.
	 */
	@Test
	public void testDeleteSuccess() {
		String s = null;
		try {
			s = testManagerLayer.delete(testUserArgsFake, testCookies);
		} catch (WPISuiteException e) {
			fail("unexpeced exception");
		}
		assertEquals(s,"success");
	}
	
	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#delete(java.lang.String[], javax.servlet.http.Cookie[])}.
	 */
	@Test
	public void testDeleteFailure() {
		String s = null;
		try {
			s = testManagerLayer.delete(testUserArgs, testCookies);
		} catch (WPISuiteException e) {
			fail("unexpected exception");
		}
		assertEquals(s,"failure");
	}
	
	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#delete(java.lang.String[], javax.servlet.http.Cookie[])}.
	 * @throws WPISuiteException 
	 */
	@Test(expected = AuthenticationException.class)
	public void testDeleteNoCookie() throws WPISuiteException {
		testManagerLayer.delete(testUserArgsFake, null);
	}

	/**
	 * Make sure that each time you get an object from the data base you're getting the same one
	 * be careful with this test, it touches the actual database
	 * @throws WPISuiteException 
	 */
	@Test
	public void testDBSession() throws WPISuiteException
	{
		Data db = DataStore.getDataStore();
		User[] arr = new User[1];
		db.save(new User("andrew", "ahurle", "p", 0));
		User me = db.retrieve(User.class, "username", "ahurle").toArray(arr)[0];
		User me2 = db.retrieve(User.class, "username", "ahurle").toArray(arr)[0];
		db.delete(me);
		System.out.println("equal: " + (me == me2));
		assertEquals(me, me2);
	}
	
	/**
	 * Test method for AdvancedGet
	 */
	@Test
	public void testAdvancedGet()
	{
		String s = null;
		try {
			s = testManagerLayer.advancedGet(testUserArgsFake , testCookies);
		} catch (WPISuiteException e) {
			fail("unexpected exception");
		}
		
		assertEquals(s,testUserArgs[0]);
	}
	
	/**
	 * Test method for AdvancedGet
	 */
	@Test(expected = AuthenticationException.class)
	public void testAdvancedGetNoCookie() throws WPISuiteException
	{
		testManagerLayer.advancedGet(testUserArgsFake , null);
	}
	
	/**
	 * Test method for AdvancedPut
	 */
	@Test
	public void testAdvancedPut()
	{
		String s = null;
		try {
			s = testManagerLayer.advancedPut(testUserArgsFake , "fake", testCookies);
		} catch (WPISuiteException e) {
			fail("unexpected exception");
		}
		
		assertEquals(s,testUserArgs[0]);
	}
	
	/**
	 * Test method for AdvancedGet
	 */
	@Test(expected = AuthenticationException.class)
	public void testAdvancedPutNoCookie() throws WPISuiteException
	{
		testManagerLayer.advancedPut(testUserArgsFake , "fake",  null);
	}
}

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

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.mockobjects.MockDataStore;
import edu.wpi.cs.wpisuitetng.mockobjects.MockSessionManager;
import edu.wpi.cs.wpisuitetng.mockobjects.MockUserManager;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * @author Mike
 *
 */
public class ManagerLayerTest {

	public String[] testUserArgs = {"core","user",""};
	public String[] testUserArgsFake = {"core","user","fake"};
	@SuppressWarnings("rawtypes")
	public Map<String, EntityManager> testMap = new HashMap<String, EntityManager>();
	public User fake;
	public MockSessionManager sesMan;
	public ManagerLayer testManagerLayer;
	public Cookie[] testCookies = new Cookie[1]; 
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		fake = new User("fake","testUser","testUser", 0);
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
		User[] u = testManagerLayer.getUsers("testUser");
		assertEquals(u[0].getName(), "fake");
		assertEquals(u[0].getUsername(), "testUser");
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
	 */
	@Ignore
	public void testRead() 
	{
		//MockUserManager returns User[0] = new User("fake",id,id, 0)
		
		
	}

	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#create(java.lang.String[], java.lang.String, javax.servlet.http.Cookie[])}.
	 */
	@Ignore
	public void testCreate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#update(java.lang.String[], java.lang.String, javax.servlet.http.Cookie[])}.
	 */
	@Ignore
	public void testUpdate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.ManagerLayer#delete(java.lang.String[], javax.servlet.http.Cookie[])}.
	 */
	@Ignore
	public void testDelete() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Make sure that each time you get an object from the data base you're getting the same one
	 * be careful with this test, it touches the actual database
	 */
	@Test
	public void testDBSession()
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
}

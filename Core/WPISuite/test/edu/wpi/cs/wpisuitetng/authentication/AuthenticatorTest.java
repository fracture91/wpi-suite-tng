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

package edu.wpi.cs.wpisuitetng.authentication;

import org.junit.*;

import edu.wpi.cs.wpisuitetng.ManagerLayer;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.SessionManager;
import edu.wpi.cs.wpisuitetng.authentication.Authenticator;
import edu.wpi.cs.wpisuitetng.authentication.BasicAuth;
import edu.wpi.cs.wpisuitetng.authentication.Sha256Password;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.exceptions.AuthenticationException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import static org.junit.Assert.*;

/**
 * Testing the abtract class Authentictor. Uses the BasicAuth 
 * implementation to test the non-abstract methods.
 * @author twack
 *
 */
public class AuthenticatorTest {
	Authenticator auth;
	User u;
	ManagerLayer man;
	SessionManager sessions;
	
	@Before
	public void setUp()
	{
		this.auth = new BasicAuth();
		this.man = ManagerLayer.getInstance();
		this.sessions = man.getSessions();
		
		// add the test user to the database
		DataStore db = DataStore.getDataStore();
		String hashedPassword = new Sha256Password().generateHash("jayms");
		this.u = new User("Tyler", "twack", hashedPassword, 5);
		db.save(this.u);
	}
	
	@After
	/**
	 * Ensure the Sessions are cleared after each Test.
	 */
	public void tearDown()
	{
		ManagerLayer man = ManagerLayer.getInstance();
		SessionManager sessions = man.getSessions();
		UserManager users = man.getUsers();
		
		String ssid = this.sessions.createSession(this.u);
		users.deleteAll(this.sessions.getSession(ssid));
		sessions.clearSessions();
	}
	
	@Test
	public void testGetAuthType()
	{
		String authType = this.auth.getAuthType();
		assertTrue(authType.equals("BasicAuth"));
	}
	
	@Test
	/**
	 * Tests the logout function of Authenticator.
	 * 	Verifies the test by checking the number of sessions. If
	 * 	there should be one fewer session after the user logs out.	
	 */
	public void testLogout()
	{		
		int test = this.sessions.sessionCount();
		String ssid = this.sessions.createSession(this.u);
		
		Session uSes = this.sessions.getSession(ssid);
		
		assertEquals(test+1, this.sessions.sessionCount());
		
		// logout the user
		this.auth.logout(ssid);
		
		assertEquals(test, this.sessions.sessionCount());
	}
	
	@Test
	public void testLoginSuccess() throws WPISuiteException
	{
		int test = this.sessions.sessionCount();
		// generate a login token (password hardcoded)
		String hashedPassword = new Sha256Password().generateHash("jayms");
		String token = BasicAuth.generateBasicAuth(this.u.getUsername(), "jayms"); 
		
		Session ses = this.auth.login(token); // login
		
		// check if session created for the user
		assertEquals(test+1, this.sessions.sessionCount());
		assertEquals(this.u.getUsername(), ses.getUsername());
	}
	
	@Test(expected = AuthenticationException.class)
	/**
	 * Tests Login when given a token with a mismatched password.
	 * 	Should throw an AuthenticationException for test pass
	 * @throws AuthenticationException
	 */
	public void testLoginFailureBadPass() throws AuthenticationException, WPISuiteException
	{		
		assertEquals(0, this.sessions.sessionCount());
		
		// generate login token with incorrect password
		String badToken = BasicAuth.generateBasicAuth(this.u.getUsername(), "letsgetweird");
		
		this.auth.login(badToken);
	}
	
	@Test(expected = AuthenticationException.class)
	/**
	 * Tests Login when given a token with a non-existent username.
	 * 	Should throw an AuthenticationException for test pass
	 * @throws AuthenticationException
	 */
	public void testLoginFailureBadUsername() throws AuthenticationException, WPISuiteException
	{
		assertEquals(0, this.sessions.sessionCount());
		
		// generate login token with non-existent username
		String badToken = BasicAuth.generateBasicAuth("wargarblargle", "jayms");
		
		this.auth.login(badToken);
	}
}

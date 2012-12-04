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

package edu.wpi.cs.wpisuitetng;

import static org.junit.Assert.*;

import org.junit.*;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.SessionManager;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Unit tests for the SessionManager. Runs tests across the Session & SessionManager classes
 * @author twack
 *
 */
public class SessionManagerTest {
	SessionManager man;
	User u1;
	User u2;
	
	@Before
	public void setUp()
	{
		this.u1 = new User("Tyler", "twack", null, 0);
		this.u2 = new User("Mike", "mpdelladonna", null, 1);		
		
		this.man = new SessionManager();
		
	}

	/* Test SessionManager Map Exposure Functions */
	
	@Test
	public void testCreateSession()
	{
		Session session = this.man.createSession(this.u1);
		
		Session createdSession = this.man.getSession(session.toString());
		
		assertEquals(this.man.sessionCount(), 1); // check that only one exists in the Manager.
		assertTrue(createdSession.getUsername().equals(this.u1.getUsername())); // check that the session is the right user
	}
	
	@Test
	public void testSessionExists()
	{
		Session session = this.man.createSession(this.u2);
		
		assertEquals(this.man.sessionCount(), 1);
		assertTrue(this.man.sessionExists(session.toString()));
	}
	
	@Test
	public void testClearSessions()
	{
		this.man.createSession(this.u1);
		this.man.createSession(this.u2);
		
		assertEquals(this.man.sessionCount(), 2); // check that sessions have been added.
		
		this.man.clearSessions();
		
		assertEquals(this.man.sessionCount(), 0); // check that the sessions have been cleared.
	}
	
	@Test
	public void testRemoveSession()
	{
		this.man.createSession(this.u2);
		Session ses = this.man.createSession(this.u1);
		
		assertEquals(this.man.sessionCount(), 2); // check sessions has been created
		
		this.man.removeSession(ses.toString());
		
		assertEquals(this.man.sessionCount(), 1);
	}
	
	/* Test complex SessionManager functions */
	
	@Ignore
	/**
	 * Test the renewSession() function.
	 * 	The expected behavior is that, given a user's sessionToken string,
	 * 	Remove the session matching that token, and created/add a new
	 * 	Session for the user.
	 * 
	 * 	DB Test -- interacts with database
	 */
	public void testRenewSession()
	{
		//TODO - Implement new session based actions where old ones are commented out.
		// add the users to the database.
		ManagerLayer manager = ManagerLayer.getInstance();
		UserManager users = manager.getUsers();
		//users.save(this.u1);
		//users.save(this.u2);
		
		// add the session to renew
		Session oldSession = this.man.createSession(this.u1);
		String oldToken = oldSession.toString(); // the key in the manager map for the created Session
		assertEquals(this.man.sessionCount(), 1);
		
		// renew the session
		Session renewed = this.man.renewSession(oldToken);
		
		assertEquals(this.man.sessionCount(), 1); // the new session has been added
		assertTrue(this.man.sessionExists(renewed.toString()));
		
		//TODO: determine if we can use a wait to push the clock forward.
		// assertFalse(this.man.sessionExists(oldToken)); 		
		
		// clear the database for the next test.
		//users.deleteEntity(this.u1.getUsername());
		//users.deleteEntity(this.u2.getUsername());
	}

}

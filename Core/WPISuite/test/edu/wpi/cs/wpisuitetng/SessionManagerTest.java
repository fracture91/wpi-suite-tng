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
import edu.wpi.cs.wpisuitetng.exceptions.AuthenticationException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.SessionException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.ProjectManager;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
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
		this.u1 = new User("Tyler", "twack", "jayms", 2);
		this.u2 = new User("Mike", "mpdelladonna", "yams", 3);
		this.u2.setRole(Role.ADMIN);
		
		this.man = new SessionManager();
	}
	
	@After
	/**
	 * Clears out the sessions after each run.
	 */
	public void tearDown()
	{
		man.clearSessions();
	}

	/* Test SessionManager Map Exposure Functions */
	
	@Test
	public void testCreateSession()
	{
		String ssid = this.man.createSession(this.u1);
		
		Session createdSession = this.man.getSession(ssid);
		
		assertEquals(this.man.sessionCount(), 1); // check that only one exists in the Manager.
		assertTrue(createdSession.getUsername().equals(this.u1.getUsername())); // check that the session is the right user
	}
	
	@Test
	public void testSessionExists()
	{
		String session = this.man.createSession(this.u2);
		
		assertEquals(this.man.sessionCount(), 1);
		assertTrue(this.man.sessionExists(session));
	}
	
	@Test
	public void testClearSessions()
	{
		this.man.createSession(this.u1);
		this.man.createSession(this.u2);
		
		assertEquals(2, this.man.sessionCount()); // check that sessions have been added.
		
		this.man.clearSessions();
		
		assertEquals(this.man.sessionCount(), 0); // check that the sessions have been cleared.
	}
	
	@Test
	public void testRemoveSession()
	{
		this.man.createSession(this.u2);
		String ssid = this.man.createSession(this.u1);
		
		assertEquals(2, this.man.sessionCount()); // check sessions has been created
		
		this.man.removeSession(ssid);
		
		assertEquals(this.man.sessionCount(), 1);
	}
	
	/* Test complex SessionManager functions */
	
	@Test
	@Ignore // db test, ignoring until we make a cleaner testing strategy.
	/**
	 * Test the switchProject function in SessionManger. It should replace the
	 * 	given session with a session logged into the given project. 
	 * 
	 * 	DB Test -- interacts with database
	 */
	public void testSwitchSessionProject() throws WPISuiteException
	{
		// get the Managers out
		ManagerLayer manager = ManagerLayer.getInstance();
		UserManager users = manager.getUsers();
		SessionManager sessions = manager.getSessions();
		ProjectManager projects = manager.getProjects();
		
		String originalSsid = sessions.createSession(u2);
		Session originalSession = sessions.getSession(originalSsid);
		
		String projectId = "proj1";
		Project p = new Project("wpisuite", projectId);
		
		try
		{
			projects.makeEntity(originalSession, p.toJSON());
		}
		catch(ConflictException e)
		{
			// this is okay because it means the project already exists in the database.
		}
		
		String newSsid = sessions.switchToProject(originalSsid, projectId);
		Session projectSession = sessions.getSession(newSsid);
		
		assertFalse(sessions.sessionExists(originalSsid));
		assertTrue(projectSession != null);
		
		assertTrue(originalSession.getProject() == null);
		assertTrue(projectSession.getProject().equals(p));
		
		try
		{
			projects.deleteEntity(projectSession, p.toJSON());	
		}
		catch(NotFoundException e)
		{
			// this is okay since we are trying to make the project 'not found'
		}
	}
	
	@Test(expected=SessionException.class)
	/**
	 * Test the switchProject function in SessionManger. This tests the integrity checks
	 * 	for the session to switch. Should throw an exception because the given SSID is
	 * 	not a held session.
	 * 
	 * 	DB Test -- interacts with database
	 */
	public void testSwitchProjectInvalidSession() throws WPISuiteException
	{
		// get the Managers out
		ManagerLayer manager = ManagerLayer.getInstance();
		UserManager users = manager.getUsers();
		SessionManager sessions = manager.getSessions();
		ProjectManager projects = manager.getProjects();
		
		String originalSsid = "abc123";
		
		String projectId = "proj1";
		
		String newSsid = sessions.switchToProject(originalSsid, projectId); // exception expected here
	}
	
	@Test(expected=SessionException.class)
	/**
	 * Test the switchProject function in SessionManger. It should replace the
	 * 	given session with a session logged into the given project. 
	 * 
	 * 	DB Test -- interacts with database
	 */
	public void testSwitchProjectInvalidProject() throws WPISuiteException
	{
		// get the Managers out
		ManagerLayer manager = ManagerLayer.getInstance();
		UserManager users = manager.getUsers();
		SessionManager sessions = manager.getSessions();
		ProjectManager projects = manager.getProjects();
		
		String originalSsid = sessions.createSession(u2);
		Session originalSession = sessions.getSession(originalSsid);
		
		String projectId = "proj00";
		
		String newSsid = sessions.switchToProject(originalSsid, projectId); // should throw an exception

	}

}

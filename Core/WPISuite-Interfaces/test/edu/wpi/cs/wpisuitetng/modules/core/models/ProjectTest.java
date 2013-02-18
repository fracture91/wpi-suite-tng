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

package edu.wpi.cs.wpisuitetng.modules.core.models;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the Project model
 * @author twack
 *
 */
public class ProjectTest {
	Project p1;
	Project p2;
	Project p3;
	
	User u1;
	User u2;
	User u3;
	User u4;
	
	User[] team1;
	String[] support1;
	
	@Before
	public void setUp()
	{
		p1 = new Project("defectTracker", "proj1");
		p2 = new Project("postBoard", "proj2");
		
		u1 = new User("James Bond", "jbond", "abcde", 7);
		u2 = new User("Money Penny", "mpenny", null, 2);
		u3 = new User("Q", "q", "whatup", 1);
		u4 = new User("M", "m", null, 0);
		
		team1 = new User[3];
		team1[0] = u1;
		team1[1] = u2;
		team1[2] = u3;
		
		support1 = new String[2];
		support1[0] = "defecttracker";
		support1[1] = "postboard";
		
		p3 = new Project("calendar", "proj3", u1, team1, support1);
	}
	
	@Test
	/**
	 * Tests the Object.equals override implemented by Project
	 */
	public void testEquals()
	{
		Object p1match = new Project("defectTracker", "proj1");
		
		assertTrue(p1.equals(p1match));
		assertFalse(p1.equals(p2));
	}
	
	@Test
	/**
	 * Ensures that the Project.getProject() method returns null. A project should
	 * 	not belong to another project. No recursive relationships.
	 */
	public void testGetProject()
	{
		assertTrue(p1.getProject() == null);
	}
	
	@Test
	/**
	 * Tests the getName function of Project
	 */
	public void testGetName()
	{		
		assertTrue(p1.getName().equals("defectTracker"));
	}
	
	@Test
	public void testSetName()
	{
		String name = "messageBoard";
		
		assertTrue(p1.getName().equals("defectTracker"));
		
		p1.setName(name);
		
		assertTrue(p1.getName().equals(name));
	}
	
	@Test
	public void testGetIdNum()
	{
		assertTrue(p1.getIdNum().equals("proj1"));
	}
	
	@Test
	/**
	 * Tests that addTeamMember function. Tests the error-checking boolean
	 * 	return values as well as the size of the team.
	 */
	public void testAddTeamMember()
	{
		int initCount = p3.getTeam().length;
		
		assertTrue(p3.addTeamMember(u4));
		assertEquals(initCount + 1, p3.getTeam().length);
		
		assertFalse(p3.addTeamMember(u4));
	}
	
	@Test
	/**
	 * Tests removing a team member from the list.
	 */
	public void testRemoveTeamMember()
	{
		int initCount = p3.getTeam().length;
		
		assertTrue(p3.removeTeamMember(u1));
		assertFalse(p3.removeTeamMember(u4));
		assertEquals(initCount - 1, p3.getTeam().length);
	}
	
	@Test
	public void testGetTeam()
	{
		ArrayList<User> teamList = new ArrayList<User>();
		teamList.add(u1);
		teamList.add(u2);
		teamList.add(u3);
		
		User[] team = p3.getTeam();
		int teamSize = team.length;
		
		assertEquals(teamList.size(), teamSize);
		
		// check that the lists are the same
		for(int i = 0; i < teamSize; i++)
		{
			assertTrue(teamList.contains(team[i]));
		}
	}
	
	@Test
	public void testGetOwner()
	{
		User owner = p3.getOwner();
		
		assertTrue(owner.equals(u1));
	}
	
	@Test
	public void testSetOwner()
	{
		User oldOwner = p3.getOwner();
		
		p3.setOwner(u2);
		assertFalse(p3.getOwner().equals(oldOwner));
		assertTrue(p3.getOwner().equals(u2));
	}

	@Test
	public void testProjectToJson() {
		String deflated = p3.toJSON();
		
		assertTrue(deflated.startsWith("{"));
		assertTrue(deflated.endsWith("}"));
		
		assertTrue(deflated.contains("name"));
		assertTrue(deflated.contains("calendar"));
		assertTrue(deflated.contains("name"));
		
		assertTrue(deflated.contains("idNum"));
		assertTrue(deflated.contains("proj3"));
		
		assertTrue(deflated.contains("owner"));
		assertTrue(deflated.contains(u1.toJSON()));
		
		assertTrue(deflated.contains("team"));

		assertTrue(deflated.contains("supportedModules"));
	}
}

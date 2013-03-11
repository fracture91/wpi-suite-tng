/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 *    Tyler Wack
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.MockData;

public class CommentManagerTest {

	Comment goodComment;
	User bob;
	Project testProject;
	Session defaultSession;
	Defect defect;
	Data db;
	CommentManager manager;
	
	String mockSsid;
	
	@Before
	public void setUp() throws Exception {
		mockSsid = "abc123";
		bob = new User("bob", "bob", "1234", 1);
		testProject = new Project("test", "1");
		defaultSession = new Session(bob, testProject, mockSsid);
		defect = new Defect(1, "title", "description", bob);
		goodComment = new Comment(1, bob, "this defect is stupid, and so are you");
		
		db = new MockData(new HashSet<Object>());
		db.save(defect, testProject);
		db.save(bob);
		manager = new CommentManager(db);
	}

	@Test
	public void testMakeEntity() throws WPISuiteException {
		Comment created = manager.makeEntity(defaultSession, goodComment.toJSON());
		assertEquals(1, created.getDefectId());
		assertSame(created, defect.getEvents().get(0));
		assertSame(testProject, created.getProject());
	}
	
	@Test(expected=BadRequestException.class)
	public void testMakeBadEntity() throws WPISuiteException {
		goodComment.setBody(null); // make sure the validator is being used
		manager.makeEntity(defaultSession, goodComment.toJSON());
	}

}

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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.models.validators;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.MockData;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;

public class CommentValidatorTest {

	Defect defect;
	Data db;
	CommentValidator validator;
	User bob;
	Project testProject;
	Session defaultSession;
	String mockSsid;
	Comment goodNewComment;
	
	@Before
	public void setUp() throws Exception {
		bob = new User("bob", "bob", "1234", 1);
		testProject = new Project("test", "1");
		mockSsid = "abc123";
		defaultSession = new Session(bob, testProject, mockSsid);
		defect = new Defect(1, "title", "description", bob);
		
		User bobCopy = new User(null, "bob", null, -1);
		goodNewComment = new Comment(1, bobCopy, "hello");
		
		db = new MockData(new HashSet<Object>());
		db.save(defect, testProject);
		db.save(bob);
		validator = new CommentValidator(db);
	}

	public List<ValidationIssue> checkNumIssues(int num, Session session, Comment comment) {
		List<ValidationIssue> issues;
		try {
			issues = validator.validate(session, comment);
			assertEquals(num, issues.size());
		} catch(WPISuiteException e) {
			throw new RuntimeException("Unexpected WPISuiteException", e);
		}
		return issues;
	}
	
	public void checkNoIssues(Session session, Comment comment) {
		checkNumIssues(0, session, comment);
	}
	
	public List<ValidationIssue> checkIssue(Session session, Comment comment) {
		return checkNumIssues(1, session, comment);
	}
	
	public List<ValidationIssue> checkNonFieldIssue(Session session, Comment comment) {
		List<ValidationIssue> issues = checkIssue(session, comment);
		assertFalse(issues.get(0).hasFieldName());
		return issues;
	}
	
	public List<ValidationIssue> checkFieldIssue(Session session, Comment comment, String fieldName) {
		List<ValidationIssue> issues = checkNumIssues(1, session, comment);
		assertEquals(fieldName, issues.get(0).getFieldName());
		return issues;
	}
	
	@Test
	public void testGoodNewComment() {
		checkNoIssues(defaultSession, goodNewComment);
		assertSame(bob, goodNewComment.getUser());
		assertNotNull(goodNewComment.getDate());
		assertSame(defect, validator.getLastExistingDefect());
	}
	
	@Test
	public void testNullComment() {
		checkNonFieldIssue(defaultSession, null);
	}
	
	@Test
	public void testAuthorMismatch() {
		goodNewComment.setUser(new User("joe", "joe", "", 2));
		checkFieldIssue(defaultSession, goodNewComment, "user");
	}
	
	@Test
	public void testNullAuthor() {
		goodNewComment.setUser(null);
		checkFieldIssue(defaultSession, goodNewComment, "user");
	}
	
	@Test
	public void testBadAuthor() {
		goodNewComment.setUser(new User(null, null, null, -1));
		checkFieldIssue(defaultSession, goodNewComment, "user");
		goodNewComment.setUser(new User(null, "blah", null, -1));
		checkFieldIssue(defaultSession, goodNewComment, "user");
	}
	
	@Test
	public void testBadDate() {
		Date badDate = new Date(0);
		goodNewComment.setDate(badDate);
		checkNoIssues(defaultSession, goodNewComment);
		assertNotSame(badDate, goodNewComment.getDate());
	}
	
	@Test
	public void testBadDefectId() {
		goodNewComment.setDefectId(97);
		checkFieldIssue(defaultSession, goodNewComment, "defectId");
	}
	
	@Test
	public void testNullBody() {
		goodNewComment.setBody(null);
		checkFieldIssue(defaultSession, goodNewComment, "body");
	}
	
	@Test
	public void testEmptyBody() {
		goodNewComment.setBody("");
		checkFieldIssue(defaultSession, goodNewComment, "body");
	}
	
	@Test
	public void testLongBody() {
		goodNewComment.setBody(DefectValidatorTest.makeLongString(10001));
		checkFieldIssue(defaultSession, goodNewComment, "body");
	}

}

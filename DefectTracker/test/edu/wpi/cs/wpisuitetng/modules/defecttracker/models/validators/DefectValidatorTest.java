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
 *    Mike Della Donna
 *    Tyler Wack
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.models.validators;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.MockData;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectStatus;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Tag;

public class DefectValidatorTest {

	Defect existingDefect;
	User existingUser;
	User existingUserCopy;
	Defect goodNewDefect;
	Defect goodUpdatedDefect;
	User bob;
	User bobCopy;
	User invalidUser;
	Tag tag;
	Tag tagCopy;
	List<DefectEvent> ignoredEvents;
	Project testProject;
	Session defaultSession;
	String mockSsid;
	Data db;
	DefectValidator validator;
	Defect otherDefect;
	Project otherProject;
		
	@Before
	public void setUp() {
		invalidUser = new User("idontexist", "blah", "1234", 99);
		
		tag = new Tag("tag");
		bob = new User("bob", "bob", "1234", 1);
		existingUser = new User("joe", "joe", "1234", 2);
		existingDefect = new Defect(1, "An existing defect", "", bob);
		existingDefect.setCreationDate(new Date(0));
		existingDefect.setLastModifiedDate(new Date(0));
		existingDefect.setEvents(new ArrayList<DefectEvent>());
		
		otherDefect = new Defect(2, "A defect in a different project", "", bob);
		otherProject = new Project("other", "2");
		
		testProject = new Project("test", "1");
		mockSsid = "abc123";
		defaultSession = new Session(bob, testProject, mockSsid);
		
		//need copies to simulate db4o cross-container problem
		tagCopy = new Tag("tag");
		bobCopy = new User(null, "bob", null, -1);
		goodNewDefect = new Defect(-1, "This is a good title", "", bobCopy);
		goodNewDefect.setAssignee(bobCopy);
		goodNewDefect.getTags().add(tagCopy);
		goodNewDefect.setStatus(DefectStatus.CONFIRMED); // ignored
		ignoredEvents = new ArrayList<DefectEvent>();
		goodNewDefect.setEvents(ignoredEvents); // ignored
		
		existingUserCopy = new User(null, "joe", null, -1);
		goodUpdatedDefect = new Defect(1, "A changed title", "A changed description", bobCopy);
		goodUpdatedDefect.setAssignee(existingUserCopy);
		goodUpdatedDefect.setEvents(new ArrayList<DefectEvent>());
		goodUpdatedDefect.getTags().add(tagCopy);
		goodUpdatedDefect.setStatus(DefectStatus.CONFIRMED);
		
		db = new MockData(new HashSet<Object>());
		db.save(tag, testProject);
		db.save(bob);
		db.save(existingDefect, testProject);
		db.save(existingUser);
		db.save(otherDefect, otherProject);
		validator = new DefectValidator(db);
	}
	
	@Test
	public void testDBState() throws WPISuiteException {
		assertSame(tag, db.retrieve(Tag.class, "name", "tag").get(0));
		assertSame(bob, db.retrieve(User.class, "username", "bob").get(0));
		assertSame(existingDefect, db.retrieve(Defect.class, "id", 1).get(0));
		assertSame(otherDefect, db.retrieve(Defect.class, "id", 2).get(0));
	}
	
	public List<ValidationIssue> checkNumIssues(int num, Session session, Defect defect, Mode mode) {
		List<ValidationIssue> issues;
		try {
			issues = validator.validate(session, defect, mode);
			assertEquals(num, issues.size());
		} catch(WPISuiteException e) {
			throw new RuntimeException("Unexpected WPISuiteException", e);
		}
		return issues;
	}
	
	public void checkNoIssues(Session session, Defect defect, Mode mode) {
		checkNumIssues(0, session, defect, mode);
	}
	
	public List<ValidationIssue> checkIssue(Session session, Defect defect, Mode mode) {
		return checkNumIssues(1, session, defect, mode);
	}
	
	public List<ValidationIssue> checkNonFieldIssue(Session session, Defect defect, Mode mode) {
		List<ValidationIssue> issues = checkIssue(session, defect, mode);
		assertFalse(issues.get(0).hasFieldName());
		return issues;
	}
	
	public List<ValidationIssue> checkFieldIssue(Session session, Defect defect, Mode mode, 
			String fieldName) {
		List<ValidationIssue> issues = checkNumIssues(1, session, defect, mode);
		assertEquals(fieldName, issues.get(0).getFieldName());
		return issues;
	}
	
	@Test
	public void testNullNewDefect() {
		checkNonFieldIssue(defaultSession, null, Mode.CREATE);
	}
	
	@Test
	public void testGoodNewDefect() {
		checkNoIssues(defaultSession, goodNewDefect, Mode.CREATE);
		assertSame(bob, goodNewDefect.getAssignee());
		assertSame(bob, goodNewDefect.getCreator());
		for(Tag t : goodNewDefect.getTags()) {
			assertSame(tag, t);
		}
		assertEquals(DefectStatus.NEW, goodNewDefect.getStatus());
		assertEquals(0, goodNewDefect.getEvents().size());
		assertNotSame(ignoredEvents, goodNewDefect.getEvents());
		assertNotNull(goodNewDefect.getCreationDate());
		assertNotNull(goodNewDefect.getLastModifiedDate());
	}
	
	@Test
	public void testNoCreator() {
		goodNewDefect.setCreator(null);
		checkFieldIssue(defaultSession, goodNewDefect, Mode.CREATE, "creator");
	}
	
	@Test
	public void testBadCreator() {
		goodNewDefect.setCreator(invalidUser);
		checkFieldIssue(defaultSession, goodNewDefect, Mode.CREATE, "creator");
	}
	
	@Test
	public void testCreatorMismatch() {
		goodNewDefect.setCreator(existingUser);
		checkFieldIssue(defaultSession, goodNewDefect, Mode.CREATE, "creator");
	}
	
	@Test
	public void testNoAssignee() {
		goodNewDefect.setAssignee(null);
		checkNoIssues(defaultSession, goodNewDefect, Mode.CREATE);
	}
	
	@Test
	public void testBadAssignee() {
		goodNewDefect.setAssignee(invalidUser);
		checkFieldIssue(defaultSession, goodNewDefect, Mode.CREATE, "assignee");
	}
	
	@Test
	public void testNoTitle() {
		goodNewDefect.setTitle(null);
		checkFieldIssue(defaultSession, goodNewDefect, Mode.CREATE, "title");
	}
	
	@Test
	public void testShortTitle() {
		goodNewDefect.setTitle("abcd");
		checkFieldIssue(defaultSession, goodNewDefect, Mode.CREATE, "title");
	}
	
	public static String makeLongString(int size) {
		StringBuilder str = new StringBuilder(size);
		for(int i = 0; i < size; i++) {
			str.append('a');
		}
		return str.toString();
	}
	
	@Test
	public void testLongTitle() {
		goodNewDefect.setTitle(makeLongString(151));
		checkFieldIssue(defaultSession, goodNewDefect, Mode.CREATE, "title");
	}
	
	@Test
	public void testNoDescription() {
		goodNewDefect.setDescription(null);
		checkNoIssues(defaultSession, goodNewDefect, Mode.CREATE);
	}
	
	@Test
	public void testLongDescription() {
		goodNewDefect.setDescription(makeLongString(5001));
		checkFieldIssue(defaultSession, goodNewDefect, Mode.CREATE, "description");
	}
	
	@Test
	public void testNullTag() {
		goodNewDefect.getTags().add(null);
		checkFieldIssue(defaultSession, goodNewDefect, Mode.CREATE, "tags");
	}
	
	@Test
	public void testTagSetNull() {
		goodNewDefect.setTags(null);
		checkNoIssues(defaultSession, goodNewDefect, Mode.CREATE);
		assertTrue(goodNewDefect.getTags() instanceof Set<?>);
	}
	
	@Test
	public void testNullTagName() {
		goodNewDefect.getTags().add(new Tag(null));
		checkFieldIssue(defaultSession, goodNewDefect, Mode.CREATE, "tags");
	}
	
	@Test
	public void testEmptyTagName() {
		goodNewDefect.getTags().add(new Tag(""));
		checkFieldIssue(defaultSession, goodNewDefect, Mode.CREATE, "tags");
	}
	
	@Test
	public void testTooManyTags() {
		for(int i = 0; i < 100; i++) {
			goodNewDefect.getTags().add(new Tag(Integer.toString(i)));
		}
		checkFieldIssue(defaultSession, goodNewDefect, Mode.CREATE, "tags");
	}
	
	@Test
	public void testNewTag() {
		Tag newTag = new Tag("imvalid");
		goodNewDefect.getTags().add(newTag);
		checkNoIssues(defaultSession, goodNewDefect, Mode.CREATE);
		assertTrue(goodNewDefect.getTags().contains(newTag));
	}
	
	@Test
	public void testBadDates() {
		Date badDate = new Date(0);
		goodNewDefect.setCreationDate(badDate);
		goodNewDefect.setLastModifiedDate(badDate);
		checkNoIssues(defaultSession, goodNewDefect, Mode.CREATE);
		assertNotSame(badDate, goodNewDefect.getCreationDate());
		assertNotSame(badDate, goodNewDefect.getLastModifiedDate());
	}
	
	@Test
	public void testUpdateNullDefect() {
		checkNonFieldIssue(defaultSession, null, Mode.EDIT);
	}
	
	@Test
	public void testGoodUpdatedDefect() {
		// make sure users other than creator can update
		checkNoIssues(new Session(new User(null, "someguy", null, 50), testProject, mockSsid), goodUpdatedDefect,
				Mode.EDIT);
		assertEquals("A changed title", goodUpdatedDefect.getTitle());
		assertEquals("A changed description", goodUpdatedDefect.getDescription());
		assertSame(existingUser, goodUpdatedDefect.getAssignee());
		assertSame(bob, goodUpdatedDefect.getCreator());
		for(Tag t : goodUpdatedDefect.getTags()) {
			assertSame(tag, t);
		}
		assertEquals(DefectStatus.CONFIRMED, goodUpdatedDefect.getStatus());
		assertSame(existingDefect.getEvents(), goodUpdatedDefect.getEvents());
		assertEquals(existingDefect.getCreationDate(), goodUpdatedDefect.getCreationDate());
		assertNotNull(goodUpdatedDefect.getLastModifiedDate());
		assertSame(existingDefect, validator.getLastExistingDefect());
	}
	
	@Test
	public void testUpdateBadId() {
		goodUpdatedDefect.setId(999);
		checkFieldIssue(defaultSession, goodUpdatedDefect, Mode.EDIT, "id");
	}
	
	@Test
	public void testUpdateDefectInOtherProject() {
		goodUpdatedDefect.setId(otherDefect.getId());
		checkFieldIssue(defaultSession, goodUpdatedDefect, Mode.EDIT, "id");
	}
	
	@Test
	public void testUpdateNullStatus() {
		goodUpdatedDefect.setStatus(null);
		checkFieldIssue(defaultSession, goodUpdatedDefect, Mode.EDIT, "status");
	}
	
	@Test
	public void testUpdateChangeCreator() {
		goodUpdatedDefect.setCreator(existingUserCopy);
		checkNoIssues(defaultSession, goodUpdatedDefect, Mode.EDIT);
		assertSame(bob, goodUpdatedDefect.getCreator());
	}

}

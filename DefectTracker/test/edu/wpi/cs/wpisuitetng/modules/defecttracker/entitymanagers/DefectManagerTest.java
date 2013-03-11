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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.MockData;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectChangeset;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectStatus;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.FieldChange;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Tag;

public class DefectManagerTest {

	MockData db;
	User existingUser;
	Defect existingDefect;
	Session defaultSession;
	String mockSsid;
	DefectManager manager;
	Defect newDefect;
	User bob;
	Defect goodUpdatedDefect;
	Tag tag;
	Session adminSession;
	Project testProject;
	Project otherProject;
	Defect otherDefect;
	
	@Before
	public void setUp() throws Exception {
		User admin = new User("admin", "admin", "1234", 27);
		admin.setRole(Role.ADMIN);
		testProject = new Project("test", "1");
		otherProject = new Project("other", "2");
		mockSsid = "abc123";
		adminSession = new Session(admin, testProject, mockSsid);
		
		existingUser = new User("joe", "joe", "1234", 2);
		existingDefect = new Defect(1, "An existing defect", "", existingUser);
		existingDefect.setCreationDate(new Date(0));
		existingDefect.setLastModifiedDate(new Date(0));
		existingDefect.setEvents(new ArrayList<DefectEvent>());
		
		otherDefect = new Defect(2, "A defect in a different project", "", existingUser);
		
		tag = new Tag("tag");
		goodUpdatedDefect = new Defect(1, "A changed title", "A changed description", bob);
		goodUpdatedDefect.setAssignee(existingUser);
		goodUpdatedDefect.setEvents(new ArrayList<DefectEvent>());
		goodUpdatedDefect.getTags().add(tag);
		goodUpdatedDefect.setStatus(DefectStatus.CONFIRMED);
		
		defaultSession = new Session(existingUser, testProject, mockSsid);
		newDefect = new Defect(-1, "A new defect", "A description", existingUser);
		
		db = new MockData(new HashSet<Object>());
		db.save(existingDefect, testProject);
		db.save(existingUser);
		db.save(otherDefect, otherProject);
		db.save(admin);
		manager = new DefectManager(db);
	}

	@Test
	public void testMakeEntity() throws WPISuiteException {
		Defect created = manager.makeEntity(defaultSession, newDefect.toJSON());
		assertEquals(3, created.getId()); // IDs are unique across projects
		assertEquals("A new defect", created.getTitle());
		assertSame(db.retrieve(Defect.class, "id", 3).get(0), created);
	}
	
	@Test(expected=BadRequestException.class)
	public void testMakeBadEntity() throws WPISuiteException {
		newDefect.setTitle(""); // invalid title
		// make sure it's being passed through the validator
		manager.makeEntity(defaultSession, newDefect.toJSON());
	}
	
	@Test
	public void testGetEntity() throws NotFoundException {
		Defect[] gotten = manager.getEntity(defaultSession, "1");
		assertSame(existingDefect, gotten[0]);
	}

	@Test(expected=NotFoundException.class)
	public void testGetBadId() throws NotFoundException {
		manager.getEntity(defaultSession, "-1");
	}

	@Test(expected=NotFoundException.class)
	public void testGetMissingEntity() throws NotFoundException {
		manager.getEntity(defaultSession, "2");
	}
	
	@Test
	public void testGetAll() {
		Defect[] gotten = manager.getAll(defaultSession);
		assertEquals(1, gotten.length);
		assertSame(existingDefect, gotten[0]);
	}
	
	@Test
	public void testSave() {
		Defect newDefect = new Defect(3, "A title", "", existingUser);
		manager.save(defaultSession, newDefect);
		assertSame(newDefect, db.retrieve(Defect.class, "id", 3).get(0));
		assertSame(testProject, newDefect.getProject());
	}
	
	@Test
	public void testDelete() throws WPISuiteException {
		assertSame(existingDefect, db.retrieve(Defect.class, "id", 1).get(0));
		assertTrue(manager.deleteEntity(adminSession, "1"));
		assertEquals(0, db.retrieve(Defect.class, "id", 1).size());
	}
	
	@Test(expected=NotFoundException.class)
	public void testDeleteMissing() throws WPISuiteException {
		manager.deleteEntity(adminSession, "4534");
	}
	
	@Test(expected=NotFoundException.class)
	public void testDeleteFromOtherProject() throws WPISuiteException {
		manager.deleteEntity(adminSession, Integer.toString(otherDefect.getId()));
	}
	
	@Test(expected=UnauthorizedException.class)
	public void testDeleteNotAllowed() throws WPISuiteException {
		manager.deleteEntity(defaultSession, Integer.toString(existingDefect.getId()));
	}
	
	@Test
	public void testDeleteAll() throws WPISuiteException {
		Defect anotherDefect = new Defect(-1, "a title", "a description", existingUser);
		manager.makeEntity(defaultSession, anotherDefect.toJSON());
		assertEquals(2, db.retrieveAll(new Defect(), testProject).size());
		manager.deleteAll(adminSession);
		assertEquals(0, db.retrieveAll(new Defect(), testProject).size());
		// otherDefect should still be around
		assertEquals(1, db.retrieveAll(new Defect(), otherProject).size());
	}
	
	@Test(expected=UnauthorizedException.class)
	public void testDeleteAllNotAllowed() throws WPISuiteException {
		manager.deleteAll(defaultSession);
	}
	
	@Test
	public void testDeleteAllWhenEmpty() throws WPISuiteException {
		manager.deleteAll(adminSession);
		manager.deleteAll(adminSession);
		// no exceptions
	}
	
	@Test
	public void testCount() {
		assertEquals(2, manager.Count());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testUpdate() throws WPISuiteException {
		Defect updated = manager.update(defaultSession, goodUpdatedDefect.toJSON());
		assertSame(existingDefect, updated);
		assertEquals(goodUpdatedDefect.getTitle(), updated.getTitle()); // make sure ModelMapper is used
		assertEquals(1, updated.getEvents().size());
		
		DefectChangeset changeset = (DefectChangeset) updated.getEvents().get(0);
		assertSame(existingUser, changeset.getUser());
		assertEquals(updated.getLastModifiedDate(), changeset.getDate());
		
		Map<String, FieldChange<?>> changes = changeset.getChanges();
		// these fields shouldn't be recorded in the changeset
		// creator was different in goodUpdatedDefect, but should be ignored
		assertFalse(changes.keySet().containsAll(Arrays.asList("events", "lastModifiedDate", "creator")));
		
		FieldChange<String> titleChange = (FieldChange<String>) changes.get("title");
		assertEquals("An existing defect", titleChange.getOldValue());
		assertEquals("A changed title", titleChange.getNewValue());
		
		// make sure events are being saved explicitly to get around a bug
		// TODO: remove this when said bug is fixed
		assertSame(updated.getEvents(), db.retrieveAll(new ArrayList<DefectEvent>()).get(0));
	}
	
	@Test(expected=BadRequestException.class)
	public void testBadUpdate() throws WPISuiteException {
		goodUpdatedDefect.setTitle("");
		manager.update(defaultSession, goodUpdatedDefect.toJSON());
	}
	
	@Test
	public void testNoUpdate() throws WPISuiteException {
		Date origLastModified = existingDefect.getLastModifiedDate();
		Defect updated = manager.update(defaultSession, existingDefect.toJSON());
		assertSame(existingDefect, updated);
		// there were no changes - make sure lastModifiedDate is same, no new events
		assertEquals(origLastModified, updated.getLastModifiedDate());
		assertEquals(0, updated.getEvents().size());
	}
	
	@Test
	public void testProjectChangeIgnored() throws WPISuiteException {
		Defect existingDefectCopy = new Defect(1, "An existing defect", "", existingUser);
		existingDefectCopy.setProject(otherProject);
		Defect updated = manager.update(defaultSession, existingDefectCopy.toJSON());
		assertEquals(0, updated.getEvents().size());
		assertSame(testProject, updated.getProject());
	}
	
	@Test(expected=NotImplementedException.class)
	public void testAdvancedGet() throws WPISuiteException {
		manager.advancedGet(defaultSession, new String[0]);
	}
	
	@Test(expected=NotImplementedException.class)
	public void testAdvancedPost() throws WPISuiteException {
		manager.advancedPost(defaultSession, "", "");
	}
	
	@Test(expected=NotImplementedException.class)
	public void testAdvancedPut() throws WPISuiteException {
		manager.advancedPut(defaultSession, new String[0], "");
	}

}

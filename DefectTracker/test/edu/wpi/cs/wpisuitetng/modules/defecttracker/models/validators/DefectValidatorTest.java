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
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectStatus;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Tag;

public class DefectValidatorTest {

	Defect existingDefect;
	User existingUser;
	Defect goodNewDefect;
	Defect goodUpdatedDefect;
	User bob;
	User bobCopy;
	User invalidUser;
	Tag tag;
	Tag tagCopy;
	List<DefectEvent> ignoredEvents;
	Session defaultSession;
	Data db;
	DefectValidator validator;
	
	@Before
	public void setUp() {
		invalidUser = new User("idontexist", "blah", "1234", 99);
		
		tag = new Tag("tag");
		bob = new User("bob", "bob", "1234", 1);
		existingUser = new User("joe", "joe", "1234", 2);
		existingDefect = new Defect(1, "An existing defect", "", bob);
		existingDefect.setCreationDate(new Date());
		existingDefect.setLastModifiedDate(new Date());
		existingDefect.setEvents(new ArrayList<DefectEvent>());
		
		defaultSession = new Session(bob);
		
		//need copies to simulate db4o cross-container problem
		tagCopy = new Tag("tag");
		bobCopy = new User(null, "bob", null, -1);
		goodNewDefect = new Defect(-1, "This is a good title", "", bobCopy);
		goodNewDefect.setAssignee(bobCopy);
		goodNewDefect.getTags().add(tagCopy);
		goodNewDefect.setStatus(DefectStatus.CONFIRMED); // ignored
		ignoredEvents = new ArrayList<DefectEvent>();
		goodNewDefect.setEvents(ignoredEvents); // ignored
		
		Set<Model> models = new HashSet<Model>();
		models.add(tag);
		models.add(bob);
		models.add(existingDefect);
		models.add(existingUser);
		db = new MockDefectData(models);
		validator = new DefectValidator(db);
	}
	
	@Test
	public void testDBState() {
		assertSame(tag, db.retrieve(Tag.class, "name", "tag").get(0));
		assertSame(bob, db.retrieve(User.class, "username", "bob").get(0));
		assertSame(existingDefect, db.retrieve(Defect.class, "id", 1).get(0));
	}
	
	public void checkNoIssues(Session session, Defect defect, Mode mode) {
		List<ValidationIssue> issues = validator.validate(session, defect, mode);
		assertEquals(0, issues.size());
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
	
	public List<ValidationIssue> checkFieldIssue(Session session, Defect defect, Mode mode, 
			String fieldName) {
		List<ValidationIssue> issues = validator.validate(session, defect, mode);
		assertEquals(1, issues.size());
		assertEquals(fieldName, issues.get(0).getFieldName());
		return issues;
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

}

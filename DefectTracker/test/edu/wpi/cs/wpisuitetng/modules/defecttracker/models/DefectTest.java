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
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class DefectTest {

	User bob;
	Defect d1;
	Defect d1copy;
	Defect d2;
	Project project;
	
	@Before
	public void setUp() {
		bob = new User("Bob", "bob", "", -1);
		d1 = new Defect(1, "", "", bob);
		d1.getEvents().add(new DefectChangeset());
		d1.getEvents().add(new Comment());
		d1.getTags().add(new Tag("test"));
		d1copy = new Defect(1, "", "", bob);
		d2 = new Defect(2, "", "", bob);
		project = new Project("test", "1");
	}
	
	@Test
	public void testIdentify() {
		assertTrue(d1.identify(d1));
		assertTrue(d1.identify(d1copy));
		assertTrue(d1.identify("1"));
		assertFalse(d1.identify(d2));
		assertFalse(d1.identify("2"));
		assertFalse(d1.identify(new Object()));
		assertFalse(d1.identify(null));
	}
	
	@Test
	public void testfromJSON() {
		String json = d1.toJSON();
		Defect newDefect = Defect.fromJSON(json);
		assertTrue(newDefect.getEvents().get(0) instanceof DefectChangeset);
		assertTrue(newDefect.getEvents().get(1) instanceof Comment);
		assertEquals(1, newDefect.getId());
	}
	
	@Test
	public void testSetProject() {
		d1.setProject(project);
		assertSame(project, d1.getProject());
		// nested models need their projects set as well
		assertSame(project, d1.getTags().toArray(new Tag[0])[0].getProject());
		assertSame(project, d1.getEvents().get(0).getProject());
		assertSame(project, d1.getEvents().get(1).getProject());
	}

}

package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class DefectTest {

	private final User bob = new User("Bob", "bob", "", -1);
	private final Defect d1 = new Defect(1, "", "", bob);
	private final Defect d1copy = new Defect(1, "", "", bob);
	private final Defect d2 = new Defect(2, "", "", bob);
	
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

}

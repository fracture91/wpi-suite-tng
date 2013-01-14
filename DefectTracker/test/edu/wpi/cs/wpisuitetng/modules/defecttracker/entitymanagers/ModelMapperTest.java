package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;

public class ModelMapperTest {

	ModelMapper mapper;
	User user;
	Defect a;
	Defect b;
	
	@Before
	public void setUp() {
		mapper = new ModelMapper();
		user = new User("a", "a", "a", 1);
		a = new Defect(1, "a", "a", user);
		b = new Defect(2, "b", "b", null);
	}

	@Test
	public void testDefectMapping() {
		mapper.map(a, b);
		assertEquals(1, b.getId());
		assertEquals("a", b.getTitle());
		assertEquals("a", b.getDescription());
		assertSame(user, b.getCreator());
	}
	
	@Test(expected=RuntimeException.class)
	public void testBrokenMapping() {
		mapper.getBlacklist().remove("permission");
		mapper.map(a, b);
	}
	
	@Test
	public void testTypeMismatch() {
		mapper.map(user, a); // no shared fields, nothing happens, no exception
	}

}

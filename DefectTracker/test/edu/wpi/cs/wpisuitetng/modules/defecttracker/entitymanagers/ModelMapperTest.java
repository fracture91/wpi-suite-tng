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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers.ModelMapper.MapCallback;
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
	
	public abstract class TestMapCallback implements MapCallback{
		List<String> receivedNames = new ArrayList<String>();
	}
	
	@Test
	public void testCallback() {
		TestMapCallback callback = new TestMapCallback() {
			@Override
			public Object call(Model source, Model destination, String fieldName, Object sourceValue,
					Object destinationValue) {
				assertSame(a, source);
				assertSame(b, destination);
				receivedNames.add(fieldName);
				if(fieldName.equals("title")) {
					assertEquals("a", sourceValue);
					assertEquals("b", destinationValue);
				} else if(fieldName.equals("id")) {
					return -1;
				}
				return null;
			}
		};
		mapper.map(a, b, callback);
		// not an exhaustive list
		assertTrue(callback.receivedNames.containsAll(Arrays.asList("id", "title", "lastModifiedDate")));
		// make sure mapper honored callback return value - again, not exhaustive
		assertEquals(-1, b.getId());
		assertNull(b.getTitle());
		assertNull(b.getDescription());
		assertNull(b.getCreator());
	}

}

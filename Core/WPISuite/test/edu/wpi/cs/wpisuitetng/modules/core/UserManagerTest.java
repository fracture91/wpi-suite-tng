package edu.wpi.cs.wpisuitetng.modules.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.mockobjects.MockDataStore;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class UserManagerTest {

	UserManager test;
	User temp;
	User conflict;
	Gson json;
	
	@Before
	public void setUp()
	{
		test = new UserManager(MockDataStore.getMockDataStore());
		temp = new User("test","test","test",0);
		conflict = new User("steve", "steve",null, 0);
		json = new Gson();
	}
	
	@Test
	public void test()
	{
		DataStore.getDataStore().retrieve(User.class, "username", "timmy");
	}
	
	@Test
	public void testMakeEntity() {
		User u = null;
		try {
			u = test.makeEntity(new Session(temp), json.toJson(temp, User.class));
		} catch (WPISuiteException e) {
			fail("unexpected exception");
		}
		assertEquals(u,temp);
	}
	
	@Test(expected = ConflictException.class)
	public void testMakeEntityExists() {
		
	}
	
	@Test(expected = WPISuiteException.class)
	public void testMakeEntityBadJson() {
		
	}

	@Test
	public void testGetEntitySessionString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEntityString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteEntity() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testCount() {
		fail("Not yet implemented");
	}

}

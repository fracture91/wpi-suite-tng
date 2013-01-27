package edu.wpi.cs.wpisuitetng.modules.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.mockobjects.MockDataStore;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.ProjectManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class ProjectManagerTest {

	ProjectManager test;
	ProjectManager testWithRealDB;
	Project temp;
	Project updateTemp;
	Project conflict;
	Gson json;
	Session tempSession;
	User tempUser;
	
	@Before
	public void setUp()
	{
		test = new ProjectManager(MockDataStore.getMockDataStore());
		testWithRealDB = new ProjectManager(DataStore.getDataStore());
		temp = new Project("test","8");
		tempUser = new User("name", "username", "password", 1);
		temp.setPermission(Permission.WRITE, tempUser);
		updateTemp = new Project("0", "proj0");
		conflict = new Project("test", "5");
		tempSession = new Session(tempUser);
		json = new Gson();
	}
	
	
	
	@Test
	public void testMakeEntity() {
		Project u = null;
		try {
			u = test.makeEntity(new Session(tempUser), json.toJson(temp, Project.class));
		} catch (WPISuiteException e) {
			fail("unexpected exception");
		}
		assertEquals(u,temp);
	}
	
	@Test(expected = ConflictException.class)
	public void testMakeEntityExists() throws WPISuiteException {
		test.makeEntity(tempSession, json.toJson(conflict, Project.class));
	}
	
	@Test(expected = BadRequestException.class)
	public void testMakeEntityBadJson() throws WPISuiteException {
		test.makeEntity(tempSession, "Garbage");
	}

	@Ignore
	public void testGetEntitySessionString() {
		fail("GetAll is not yet implemented");
	}

	@Test(expected = NotFoundException.class)
	public void testGetEntityStringEmptyString() throws NotFoundException {
		test.getEntity("");
	}
	
	@Test
	public void testGetEntityStringProjectExists() {
		Project[] u = null;
		try {
			u = test.getEntity("5");
		} catch (NotFoundException e) {
			fail("unexpected exception");
		}
		assertEquals(conflict, u[0]);
	}
	
	@Test(expected = NotFoundException.class)
	public void testGetEntityStrinProjectDNE() throws NotFoundException {
		test.getEntity("jefferythegiraffe");
	}

	@Test
	public void testGetAll() throws WPISuiteException {
		testWithRealDB.save(tempSession, temp);
		testWithRealDB.save(tempSession, updateTemp);
		Project[] myList = testWithRealDB.getAll(new Session(tempUser));
		assertEquals(2, myList.length);
		testWithRealDB.deleteAll(new Session(tempUser));
	}

	@Test(expected = WPISuiteException.class)
	public void testSaveFail() throws WPISuiteException {
		new ProjectManager(new Data(){
			@Override
			public <T> boolean save(T aTNG) {return false;}
			@Override
			public List<Model> retrieve(Class anObjectQueried,String aFieldName, Object theGivenValue) {return null;}
			@Override
			public <T> T delete(T aTNG) {return null;}
			@Override
			public void update(Class anObjectToBeModified, String fieldName,Object uniqueID, String changeField, Object changeValue) {}
			@Override
			public <T> List<T> retrieveAll(T arg0) {
				return null;
			}
			@Override
			public <T> List<T> deleteAll(T aSample) {
				// TODO Auto-generated method stub
				return null;
			}
			}
		).save(null, null);
	}

	@Test
	public void testDeleteEntityFail() throws NotFoundException {
		new ProjectManager(new Data(){
			@Override
			public <T> boolean save(T aTNG) {return false;}
			@Override
			public List<Model> retrieve(Class anObjectQueried,String aFieldName, Object theGivenValue) {
				List<Model> a = new ArrayList<Model>();
				a.add(temp);
				return a;
				}
			@Override
			public <T> T delete(T aTNG) {return null;}
			@Override
			public void update(Class anObjectToBeModified, String fieldName,Object uniqueID, String changeField, Object changeValue) {}
			@Override
			public <T> List<T> retrieveAll(T arg0) {
				return null;
			}
			@Override
			public <T> List<T> deleteAll(T aSample) {
				// TODO Auto-generated method stub
				return null;
			}
			}
		).deleteEntity(null, temp.getIdNum());
	}
	
	@Test
	public void testDeleteEntity() throws NotFoundException
	{
		new ProjectManager(new Data(){
			@Override
			public <T> boolean save(T aTNG) {return false;}
			@Override
			public List<Model> retrieve(Class anObjectQueried,String aFieldName, Object theGivenValue) {
				List<Model> a = new ArrayList<Model>();
				a.add(temp);
				return a;}
			@Override
			public <T> T delete(T aTNG) {return aTNG;}
			@Override
			public void update(Class anObjectToBeModified, String fieldName,Object uniqueID, String changeField, Object changeValue) {}
			@Override
			public <T> List<T> retrieveAll(T arg0) {
				return null;
			}
			@Override
			public <T> List<T> deleteAll(T aSample) {
				return null;
			}
			}
		).deleteEntity(null, temp.getIdNum());
	}

	@Test
	public void testDeleteAll() throws WPISuiteException {
		testWithRealDB.save(tempSession, temp);
		testWithRealDB.save(tempSession, updateTemp);
		Project[] myList = testWithRealDB.getAll(new Session(tempUser));
		assertEquals(2, myList.length);
		
		testWithRealDB.deleteAll(new Session(tempUser));
		myList = testWithRealDB.getAll(new Session(tempUser));
		assertEquals(1, myList.length);
		assertEquals(myList[0], null);
	}

	@Ignore
	public void testCount() {
		fail("Not yet implemented");
	}
	
	@Test
	/**
	 * Tests if the update() function properly maps the JSON string then applies
	 * 	the changes to the given User.
	 * @throws WPISuiteException
	 */
	public void testUpdate() throws WPISuiteException
	{
		Session ses = null;
		String updateString = "{ \"idNum\": \"2\", \"name\": \"proj2\" }";
		Project newTemp = this.test.update(ses, updateTemp, updateString);
		
		// TODO: find a way to retrieve the User from storage to run assertions on.
		
		assertTrue(newTemp.getIdNum().equals("2"));
		assertTrue(newTemp.getName().equals("proj2"));
	}
	
	@Test(expected = WPISuiteException.class)
	/**
	 * Tests failure in update's ObjectMapper. 
	 * @throws WPISuiteException	on success
	 */
	public void testUpdateFailure() throws WPISuiteException
	{
		Session ses = null;
		String updateString = "{ \"idNum\": \"2\", \"name\": \"proj2\",,,,,,,,,,, }"; // extra commas cause problems in ObjectMapper
		
		this.test.update(ses, updateTemp, updateString);
		
		fail("Exception should have been thrown");
	}

}

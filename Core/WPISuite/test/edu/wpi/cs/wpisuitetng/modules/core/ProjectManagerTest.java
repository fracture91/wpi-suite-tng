package edu.wpi.cs.wpisuitetng.modules.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.mockobjects.MockDataStore;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.ProjectManager;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class ProjectManagerTest {

	ProjectManager test;
	Project temp;
	Project conflict;
	Gson json;
	Session tempSession;
	User tempUser;
	
	@Before
	public void setUp()
	{
		test = new ProjectManager(MockDataStore.getMockDataStore());
		temp = new Project("test","5");
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

	@Test
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

	@Ignore
	public void testGetAll() {
		fail("Not yet implemented");
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
			}
		).save(null, null);
	}

	@Test
	public void testDeleteEntityFail() {
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
			}
		).deleteEntity(null, temp.getIdNum());
	}
	
	@Test
	public void testDeleteEntity()
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
			}
		).deleteEntity(null, temp.getIdNum());
	}

	@Ignore
	public void testDeleteAll() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testCount() {
		fail("Not yet implemented");
	}

}

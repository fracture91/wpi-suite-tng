package edu.wpi.cs.wpisuitetng.database;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectChangeset;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectStatus;

public class TestDB4oDatabase {
	
	
	public class DepthPerson extends AbstractModel{
		public int id;
		public String name;
		public DepthPerson person;
	
	
	public DepthPerson(int id, String name, DepthPerson guy){
		this.id = id;
		this.name = name;
		this.person = guy;
	}


	public boolean equals(DepthPerson guy){
		if(this.id == guy.id
			&& (this.name.equals(guy.name))
			&& (this.person.equals(guy.person)))
				return true;
			else
				return false;
	}
	
	public int getId(){
		return this.id;
	}
	
	public DepthPerson getDepthPerson(){
		return this.person;
	}
	
	@Override
	public void save() {
		return;
	}


	@Override
	public void delete() {
		return;
	}


	@Override
	public String toJSON() {
		return null;
	}


	@Override
	public Boolean identify(Object o) {
		return null;
	}
	}
	

	@Test
	public void testSaveandRetrieve() {
		Data db = DataStore.getDataStore();
		User[] arr = new User[1];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		db.save(firstUser);
		User me = db.retrieve(User.class, "username", "rchamer").toArray(arr)[0];
		assertEquals(me, firstUser);
		db.delete(me);
	}
	
	@Test
	public void testDelete(){
		Data db = DataStore.getDataStore();
		User[] arr = new User[1];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		db.save(firstUser);
		db.delete(firstUser);
		User me = db.retrieve(User.class, "username", "rchamer").toArray(arr)[0];
		assertEquals(me, null);
	}
	
	@Test
	public void testUpdate(){
		Data db = DataStore.getDataStore();
		User[] arr = new User[2];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		db.save(firstUser);
		db.update(User.class, "username", "rchamer", "name", "Mjolnir");
		User Mjolnir = db.retrieve(User.class, "username", "rchamer").toArray(arr)[0];
		assertEquals(firstUser, Mjolnir);
		db.delete(Mjolnir);
		
		
	}
	@Test
	public void testNotRetrieve(){
		Data db = DataStore.getDataStore();
		User[] arr = new User[2];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		db.deleteAll(firstUser);
		db.save(firstUser);
		User me = db.notRetrieve(User.class, "username", "bgaffey").toArray(arr)[0];
		assertEquals(me, firstUser);
	}
	
	@Test
	public void testOrRetrieve() throws WPISuiteException, IllegalAccessException, InvocationTargetException{
		Data db = DataStore.getDataStore();
		
		User[] arr = new User[2];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		User secondUser = new User("Bryan", "bgaffey", "pword", 1);
		List<User> both = new ArrayList<User>();
		both.add(firstUser);
		both.add(secondUser);
		db.deleteAll(firstUser);
		db.save(firstUser);
		db.save(secondUser);
		String[] list = new String[2];
		list[0] = "Username";
		list[1] = "Name";
		List<Object> objlist = new ArrayList<Object>();
		objlist.add("rchamer");
		objlist.add("Bryan");
		List<Model> me = db.orRetrieve(firstUser.getClass(), list, objlist);
		assertEquals(me, both);
	}
	
	@Test
	public void testAndRetrieve() throws WPISuiteException, IllegalAccessException, InvocationTargetException{ 
	
		Data db = DataStore.getDataStore();
		
		User[] arr = new User[2];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		User secondUser = new User("Bryan", "rchamer", "pword", 1);
		List<User> first = new ArrayList<User>();
		first.add(firstUser);
		db.deleteAll(firstUser);
		db.save(firstUser);
		db.save(secondUser);
		String[] list = new String[2];
		list[0] = "Username";
		list[1] = "Name";
		List<Object> objlist = new ArrayList<Object>();
		objlist.add("rchamer");
		objlist.add("Ryan");
		List<Model> me = db.andRetrieve(firstUser.getClass(), list, objlist);
		assertEquals(me, first);
	}
	
	@Test
	public void testComplexRetrieve() throws WPISuiteException, IllegalAccessException, InvocationTargetException{
Data db = DataStore.getDataStore();
		
		
		User[] arr = new User[2];
		User firstUser = new User("Ryan", "rchamer", "password", 0);
		User secondUser = new User("Bryan", "rchamer", "pword", 1);
		User thirdUser = new User("Tyler", "twack", "word", 2);
		List<User> first = new ArrayList<User>();
		db.deleteAll(firstUser);
		first.add(firstUser);
		first.add(thirdUser);
		db.deleteAll(firstUser);
		db.save(firstUser);
		db.save(secondUser);
		db.save(thirdUser);
		String[] list = new String[2];
		list[0] = "Username";
		list[1] = "Name";
		List<Object> objlist = new ArrayList<Object>();
		objlist.add("rchamer");
		objlist.add("Ryan");
		
		String[] orList = new String[2];
		orList[0] = "idNum";
		orList[1] = "Name";
		List<Object> orObjList = new ArrayList<Object>();
		orObjList.add(0);
		orObjList.add("Tyler");
		
		List<Model> me = db.complexRetrieve(firstUser.getClass(), list, objlist, firstUser.getClass(), orList, orObjList);
		assertEquals(me, first);
	}
	/*
	@Test
	public void testSaveDepth() throws WPISuiteException{
		DepthPerson ray = new DepthPerson(6, "ray", null);
		DepthPerson chris = new DepthPerson(5, "chris", ray);
		DepthPerson tyler = new DepthPerson(4, "tyler", chris);
		DepthPerson ted = new DepthPerson(3, "ted", tyler);
		DepthPerson guy = new DepthPerson(2, "guy", ted);
		DepthPerson person1 = new DepthPerson(1, "mike", guy);
		
		Data db = DataStore.getDataStore();
		db.save(person1);
		DepthPerson getBack = (DepthPerson) db.retrieve(person1.getClass(), "id", 1).toArray()[1];
		db.deleteAll(person1.getClass());
		db.save(person1, 100);
		DepthPerson getBackDepth = (DepthPerson) db.retrieve(person1.getClass(), "id", 1, 100).toArray()[1];
		assertEquals(1,1);
		
		db.deleteAll(person1.getClass());
		db.save(person1, 1);
		DepthPerson pers1 = (DepthPerson) db.retrieve(person1.getClass(), "id", 1).toArray()[1];
		List<Model> next = new ArrayList<Model>();
		next.add(person1);
		assertEquals(pers1, person1);
		
	}
*/
}

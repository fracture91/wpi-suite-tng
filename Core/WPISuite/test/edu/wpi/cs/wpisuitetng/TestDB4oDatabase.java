package edu.wpi.cs.wpisuitetng;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class TestDB4oDatabase {
	

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
	public void testRetrieveAll(){
		Data db = DataStore.getDataStore();
		User[] arr = new User[2];
		User firstUser = new User("Brian", "bgaffey", "password", 0);
		db.save(firstUser);
		User secondUser = new User("Gaffey", "gafftron", "password", 0);
		db.save(secondUser);
		List<User> retrievedList = db.retrieveAll(firstUser);
		
		assertEquals(2, retrievedList.size());
		assertTrue(retrievedList.contains(firstUser));
		assertTrue(retrievedList.contains(secondUser));
		
		db.delete(firstUser);
		retrievedList = db.retrieveAll(firstUser);
		
		assertEquals(1, retrievedList.size());
		assertTrue(retrievedList.contains(secondUser));
		assertFalse(retrievedList.contains(firstUser));
		
		db.save(firstUser);
		retrievedList = db.deleteAll(firstUser);
		User me1 = db.retrieve(User.class, "username", "bgaffey").toArray(arr)[0];
		User me2 = db.retrieve(User.class, "username", "gafftron").toArray(arr)[0];
		assertEquals(2, retrievedList.size());
		assertEquals(me1, null);
		assertEquals(me2, null);
	}
	

}

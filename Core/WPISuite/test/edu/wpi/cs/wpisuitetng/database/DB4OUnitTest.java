package edu.wpi.cs.wpisuitetng.database;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;

import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class DB4OUnitTest {

	@Ignore
	public void testAddandRetrieve() throws Exception {
		
		String WPI_TNG_DB ="WPISuite_TNG_unitTest";
		DataStore myself = null;
		ObjectContainer theDB;
		ObjectServer server;
		int PORT = 0;
		String DB4oUser = "tester";
		String DB4oPass = "password";
		String DB4oServer = "localhost";
		User JSmith = new User("John Smith", "jsmith", null, 0);
		List<User> mtList = new ArrayList<User>();
		List<User> userList = new ArrayList<User>();
		myself.getDataStore();
		userList.add(JSmith);
		
		myself.getDataStore();
		myself.addUser("{\"name\":\"John Smith\",\"username\":\"jsmith\",\"idNum\":0}", JSmith.getClass());
		assertTrue(((User)myself.retrieve(JSmith.getClass(), "userName", "jsmith").get(0)).equals(JSmith));;
	}

}

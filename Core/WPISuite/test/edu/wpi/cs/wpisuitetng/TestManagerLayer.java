package edu.wpi.cs.wpisuitetng;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.ManagerLayer;
import edu.wpi.cs.wpisuitetng.core.entitymanagers.MockUserManager;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;

public class TestManagerLayer {

	public String[] testUserArgs = {"core","user",""};
	@SuppressWarnings("rawtypes")
	public Map<String, EntityManager> testMap = new HashMap<String, EntityManager>();
	
	
	@Test
	public void test() 
	{
		testMap.put("coreuser", new MockUserManager());
		ManagerLayer test = ManagerLayer.getTestInstance(testMap);
		
		String testResponse = null;
		try {
			testResponse = test.read(testUserArgs,null);
		} catch (WPISuiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals("[{\"name\":\"fake\",\"username\":\"\",\"idNum\":0}]", testResponse);
	}

}

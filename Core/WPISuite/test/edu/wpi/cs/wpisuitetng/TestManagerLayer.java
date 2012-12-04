/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mpdelladonna
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.ManagerLayer;
import edu.wpi.cs.wpisuitetng.core.entitymanagers.MockUserManager;
import edu.wpi.cs.wpisuitetng.database.MockDataStore;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;

public class TestManagerLayer {

	public String[] testUserArgs = {"core","user",""};
	@SuppressWarnings("rawtypes")
	public Map<String, EntityManager> testMap = new HashMap<String, EntityManager>();
	
	
	@Test
	public void test() 
	{
		testMap.put("coreuser", new MockUserManager(MockDataStore.getMockDataStore()));
		ManagerLayer test = ManagerLayer.getTestInstance(testMap);
		
		String testResponse = test.read(testUserArgs);
		
		assertEquals("[{\"name\":\"fake\",\"username\":\"\",\"idNum\":0}]", testResponse);
	}

}

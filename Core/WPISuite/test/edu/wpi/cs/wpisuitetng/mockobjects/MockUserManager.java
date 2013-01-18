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

package edu.wpi.cs.wpisuitetng.mockobjects;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class MockUserManager extends UserManager {

	@Override
	public User update(Session s, String content) throws WPISuiteException {
		return new User("asdf","asdf","asdf", 0);
	}

	@Override
	public boolean deleteEntity(Session s1, String id) {
		if(id.equalsIgnoreCase("fake"))
			return true;
		else
			return false;
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		return args[0];
	}
	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		return args[0];
	}

	@Override
	public User makeEntity(Session s, String content) throws WPISuiteException {
		return new User("asdf","asdf","asdf", 0);
	}

	User fake;
	
	public MockUserManager(Data data, User fake) {
		super(data);
		this.fake = fake;
	}
	

	@Override
	public User[] getEntity(String id)
	{
		User[] u = new User[1];
		u[0] = fake;

		
		return u;
	}
	
	@Override
	public User[] getEntity(Session s, String id)
	{
		User[] u = new User[1];
		u[0] = fake;
		System.out.println("MockUserManager id: " + id);
		if(id.equalsIgnoreCase(""))
		{
			u = new User[2];
			u[0] = fake;
			u[1] = fake;
		}
		
		if(id.equalsIgnoreCase("asdf"))
			u[0] = new User("asdf","asdf","asdf", 0);
		
		if(!(id.equalsIgnoreCase(fake.getUsername()) || id.equalsIgnoreCase("") || id.equalsIgnoreCase("asdf")))
			return null;
		System.out.println("MockUserManager retval: " + u[0]);
		return u;
	}
	
	
	
	
}

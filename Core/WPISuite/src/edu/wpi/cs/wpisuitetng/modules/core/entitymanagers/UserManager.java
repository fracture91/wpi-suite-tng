/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.core.entitymanagers;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class UserManager implements EntityManager<User> {

	Class<User> user = User.class;
	Gson gson;
	
	public UserManager()
	{
		gson = new Gson();
	}
	
	@Override
	public User makeEntity(String content) {
		
		User p;
		
		p = gson.fromJson(content, user);
		
		if(getEntity(p.getUsername())[0] == null)
		{
			save(p);
		}
		
		return p;
	}

	@Override
	public User[] getEntity(String id) 
	{
		User[] m = new User[1];
		if(id.equalsIgnoreCase(""))
		{
			return getAll();
		}
		else
		{
			return DataStore.getDataStore().retrieve(user, "username", id).toArray(m);
		}
	}

	@Override
	public User[] getAll() {
		// TODO Implement this feature in a later release
		return null;
	}

	@Override
	public void save(User model) {
		DataStore.getDataStore().save(model);
		
	}

	@Override
	public boolean deleteEntity(String id) {
		
		DataStore data = DataStore.getDataStore();
		
		Model m = data.delete(data.retrieve(user, "username", id).get(0));
		
		return (m != null) ? true : false;
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}

}

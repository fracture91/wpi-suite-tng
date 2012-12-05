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
import com.google.gson.JsonSyntaxException;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class UserManager implements EntityManager<User> {

	Class<User> user = User.class;
	Gson gson;
	Data data;

	public UserManager(Data data)
	{
		this.data = data;
		gson = new Gson();
	}
	
	@Override
	public User makeEntity(Session s, String content) throws WPISuiteException{
		
		User p;
		try{
			p = gson.fromJson(content, user);
		} catch(JsonSyntaxException e){
			throw new WPISuiteException();
		}
		
		if(getEntity(s,p.getUsername())[0] == null)
		{
			save(s,p);
		}
		else
		{
			throw new ConflictException();
		}
		
		return p;
	}

	@Override
	public User[] getEntity(Session s,String id) 
	{
		User[] m = new User[1];
		if(id.equalsIgnoreCase(""))
		{
			return getAll(s);
		}
		else
		{
			return data.retrieve(user, "username", id).toArray(m);
		}
	}
	
	/**
	 * returns a user without requiring a session, 
	 * specifically for the scenario where a session needs to be created.
	 * only ever returns one user, "" is not a valid argument;
	 * 
	 * @param id - the id of the user, in this case it's the username
	 * @return a list of matching users
	 */
	public User[] getEntity(String id) 
	{
		User[] m = new User[1];
		if(id.equalsIgnoreCase(""))
		{
			return m;
		}
		else
		{
			return data.retrieve(user, "username", id).toArray(m);
		}
	}

	@Override
	public User[] getAll(Session s) {
		// TODO Implement this feature in a later release
		return null;
	}

	@Override
	public void save(Session s,User model) throws WPISuiteException {
		if(data.save(model))
		{
			return ;
		}
		else
		{
			throw new WPISuiteException();
		}
		
	}

	@Override
	public boolean deleteEntity(Session s1 ,String id) {
		
		Model m = data.delete(data.retrieve(user, "username", id).get(0));
		
		return (m != null) ? true : false;
		
	}

	@Override
	public void deleteAll(Session s) {
		// TODO pending on get all
	}

	@Override
	public int Count() {
		// TODO pending on get all
		return 0;
	}

}

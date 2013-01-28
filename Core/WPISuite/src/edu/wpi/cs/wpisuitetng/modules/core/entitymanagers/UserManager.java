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
 *    twack - update
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.core.entitymanagers;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * The EntityManager implementation for the User class. Manages interaction with the 
 * 	set of Users in the Database defined by the constructor
 * @author 
 *
 */
public class UserManager implements EntityManager<User> {

	Class<User> user = User.class;
	Gson gson;
	Data data;

	/**
	 * Creates a UserManager operating on the given Data.
	 * 	Attaches the custom serializer and deserializers for the Gson library.
	 * @param data
	 */
	public UserManager(Data data)
	{
		this.data = data;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(this.user, new UserDeserializer());
		
		this.gson = builder.create();
	}
	
	@Override
	public User makeEntity(Session s, String content) throws WPISuiteException{
		
		User p;
		try{
			p = gson.fromJson(content, user);
		} catch(JsonSyntaxException e){
			throw new BadRequestException();
		}
		
		if(getEntity(s,p.getUsername())[0] == null)
		{
			String newPassword = UserDeserializer.parsePassword(content);
			//TODO: password hashing
			p.setPassword(newPassword);
			
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
	 * @throws NotFoundException if the user cannot be found
	 */
	public User[] getEntity(String id) throws NotFoundException
	{
		User[] m = new User[1];
		if(id.equalsIgnoreCase(""))
		{
			throw new NotFoundException();
		}
		else
		{
			m = data.retrieve(user, "username", id).toArray(m);
			
			if(m[0] == null)
			{
				throw new NotFoundException();
			}
			else
			{
				return m;
			}
		}
	}

	@Override
	public User[] getAll(Session s) {
		User[] ret = new User[1];
		ret = data.retrieveAll(new User("","","",0)).toArray(ret);
		return ret;
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
		data.deleteAll(new User("","","",0));
	}

	@Override
	public int Count() {
		// TODO pending on get all
		return 0;
	}
	
	/**
	 * 	Updates a single user object based on the JSON update string provided.
	 * 		Inflates the JSON into a User object then checks each field for differences.
	 * @param s	The Session to check authorization for this action
	 * @param toUpdate	the User to update
	 * @param changeSet	a JSON string representation of a User object. Contains the fields
	 * 	to be updated.
	 * @exception WPISuiteException	thrown when the ObjectMapper fails
	 * @return	The updated User.
	 */
	public User update(Session s, User toUpdate, String changeSet) throws WPISuiteException
	{
		// TODO: permissions checking here
		
		User changes;
		
		// Inflate the changeSet into a User object.
		try
		{
			changes = this.gson.fromJson(changeSet, this.user);
		}
		catch(JsonParseException e)
		{
			throw new WPISuiteException();
		}
		
		// Resolve differences toUpdate using changes, field-by-field.
		toUpdate.setIdNum(changes.getIdNum());
		
		if(changes.getName() != null)
		{
			toUpdate.setName(changes.getName());
		}
		
		if(changes.getUsername() != null)
		{
			toUpdate.setUserName(changes.getUsername());
		}
		
		if(!changes.getRole().equals(toUpdate.getRole()))
		{
			toUpdate.setRole(changes.getRole());
		}
		
		// save the changes back
		this.save(s, toUpdate);
		
		return toUpdate;
	}

//	public User update(Session s, String content) throws WPISuiteException {
//		// TODO Auto-generated method stub
//		return null;
//	}


}

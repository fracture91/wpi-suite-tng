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
import edu.wpi.cs.wpisuitetng.PasswordCryptographer;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.Sha256Password;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.DatabaseException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.SerializationException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.core.models.UserDeserializer;

/**
 * The EntityManager implementation for the User class. Manages interaction with the 
 * 	set of Users in the Database defined by the constructor
 * @author 
 *
 */
public class UserManager implements EntityManager<User> {

	Class<User> user = User.class;
	private PasswordCryptographer passwordHash;
	Gson gson;
	Data data;

	/**
	 * Creates a UserManager operating on the given Data.
	 * 	Attaches the custom serializer and deserializers for the Gson library.
	 * Determines the algorithm used to secure passwords.
	 * @param data
	 */
	public UserManager(Data data)
	{
		this.data = data;
		this.passwordHash = new Sha256Password();

		// build the custom serializer/deserializer
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(this.user, new UserDeserializer());

		this.gson = builder.create();

	}

	
	@Override
	public User makeEntity(Session s, String content) throws WPISuiteException{

		//TODO: create a custom de-serializer & serializer so we can hash the desired password & remove it from others.
		User p;
		try{
			p = User.fromJSON(content);
		} catch(JsonSyntaxException e){
			throw new BadRequestException("The entity creation string had invalid format. Entity String: " + content);
		}

		if(getEntity(s,p.getUsername())[0] == null)
		{
			String newPassword = UserDeserializer.parsePassword(content);

			String hashedPassword = this.passwordHash.generateHash(newPassword);

			p.setPassword(hashedPassword);

			save(s,p);
		}
		else
		{
			throw new ConflictException("A user with the given ID already exists. Entity String: " + content);
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
			throw new NotFoundException("No User id given.");
		}
		else
		{
			m = data.retrieve(user, "username", id).toArray(m);
			
			if(m[0] == null)
			{
				throw new NotFoundException("User with id <" + id + "> not found.");
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
			throw new DatabaseException("Save failure for User."); // Session User: " + s.getUsername() + " User: " + model.getName());
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
			changes = User.fromJSON(changeSet);
		}
		catch(JsonParseException e)
		{
			throw new SerializationException("Error inflating the changeset: " + e.getMessage());
		}

		// Resolve differences toUpdate using changes, field-by-field.
		toUpdate.setIdNum(changes.getIdNum()); // TODO: check if IDnums exist... should we even be updating the IdNum ever?

		if(changes.getName() != null)
		{
			toUpdate.setName(changes.getName());
		}

		//shouldn't be able to change unique identifier
		/*if(changes.getUsername() != null)
		{
			toUpdate.setUserName(changes.getUsername());
		}*/

		if(!changes.getRole().equals(toUpdate.getRole()))
		{
			toUpdate.setRole(changes.getRole());
		}

		// save the changes back
		this.save(s, toUpdate);

		return toUpdate;
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		throw new NotImplementedException();
	}


	@Override
	public User update(Session s, String content) throws WPISuiteException {
		String str = UserManager.parseUsername(content);
		
		return this.update(s, this.getEntity(str)[0], content);
	}
	
	/**
	 * This static utility method takes a JSON string and attempts to
	 * 	retrieve a username field from it.
	 * @param serializedUser	a JSON string containing a password
	 * @return	the username field parsed.
	 */
	public static String parseUsername(String serializedUser)
	{
		if(!serializedUser.contains("username"))
		{
			throw new JsonParseException("The given JSON string did not contain a username field.");
		}
		
		int fieldStartIndex = serializedUser.indexOf("username");
		int separator = serializedUser.indexOf(':', fieldStartIndex);
		int startIndex = serializedUser.indexOf('"', separator) + 1;
		int endIndex = serializedUser.indexOf('"', startIndex);
		
		String username = serializedUser.substring(startIndex, endIndex);
		
		return username;
	}

}

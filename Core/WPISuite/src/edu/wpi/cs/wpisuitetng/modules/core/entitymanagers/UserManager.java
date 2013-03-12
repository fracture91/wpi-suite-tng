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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import edu.wpi.cs.wpisuitetng.authentication.PasswordCryptographer;
import edu.wpi.cs.wpisuitetng.authentication.Sha256Password;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.DatabaseException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.SerializationException;
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
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
	
	private static final Logger logger = Logger.getLogger(UserManager.class.getName());

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
		
		logger.log(Level.FINE, "Attempting new User creation...");

		User p;
		try{
			p = User.fromJSON(content);
		} catch(JsonSyntaxException e){
			logger.log(Level.WARNING, "Invalid User entity creation string.");
			throw new BadRequestException("The entity creation string had invalid format. Entity String: " + content);
		}

		if(getEntity(s,p.getUsername())[0] == null)
		{
			String newPassword = UserDeserializer.parsePassword(content);
			String hashedPassword = this.passwordHash.generateHash(newPassword);

			p.setPassword(hashedPassword);
			
			p.setRole(Role.USER);
			
			save(s,p);
		}
		else
		{
			logger.log(Level.WARNING, "Conflict Exception during User creation.");
			throw new ConflictException("A user with the given ID already exists. Entity String: " + content);
		}

		logger.log(Level.FINE, "User creation success!");

		return p;
	}
	
	
	@Override
	public User[] getEntity(Session s,String id) throws WPISuiteException 
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
	 * @throws WPISuiteException 
	 */
	public User[] getEntity(String id) throws WPISuiteException
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
			logger.log(Level.FINE, "User Saved :" + model);

			return ;
		}
		else
		{
			logger.log(Level.WARNING, "User Save Failure!");
			throw new DatabaseException("Save failure for User."); // Session User: " + s.getUsername() + " User: " + model.getName());
		}
		
	}

	@Override
	public boolean deleteEntity(Session s1 ,String id) throws WPISuiteException {

		if(s1.getUser().getRole().equals(Role.ADMIN))
		{
			Model m = data.delete(data.retrieve(user, "username", id).get(0));
			logger.log(Level.INFO, "UserManager deleting user <" + id + ">");
			return (m != null) ? true : false;
		}
		else
		{
			logger.log(Level.WARNING,"User: "+s1.getUser().getUsername()+"attempted to delete: "+id);
			throw new UnauthorizedException("Delete not authorized");
		}
		
		
		
		
	}

	@Override
	public void deleteAll(Session s) {
		logger.log(Level.INFO, "UserManager invoking DeleteAll...");
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
			logger.log(Level.FINE, "User update being attempted...");
			changes = User.fromJSON(changeSet);
		}
		catch(JsonParseException e)
		{
			logger.log(Level.WARNING, "UserManager.update() had a failure in the changeset mapper.");

			throw new SerializationException("Error inflating the changeset: " + e.getMessage());
		}

		
		if(s.getUser().getUsername().equals(toUpdate.getUsername()) || s.getUser().getRole().equals(Role.ADMIN))
		{
			// Resolve differences toUpdate using changes, field-by-field.
			toUpdate.setIdNum(changes.getIdNum()); 
	
			if(changes.getName() != null)
			{
				toUpdate.setName(changes.getName());
			}
	
			//shouldn't be able to change unique identifier
			/*if(changes.getUsername() != null)
			{
				toUpdate.setUserName(changes.getUsername());
			}*/
			
			if(changes.getPassword() != null)
			{
				String encryptedPass = this.passwordHash.generateHash(changes.getPassword());
				toUpdate.setPassword(encryptedPass);
			}
	
			if((changes.getRole() != null))
			{
				if(s.getUser().getRole().equals(Role.ADMIN))
				{
					toUpdate.setRole(changes.getRole());
				}
				else
				{
					logger.log(Level.WARNING,"User: "+s.getUser().getUsername()+" attempted unauthorized priveledge elevation");
				}
			}
	
			// save the changes back
			this.save(s, toUpdate);
		}
		else
		{
			logger.log(Level.WARNING, "Access denied to user: "+s.getUser().getUsername());
			throw new UnauthorizedException("Users accessible only by Admins and themselves");
		}
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
		logger.log(Level.FINE, "Attempting username parsing...");
		
		if(serializedUser == null || !serializedUser.contains("username"))
		{
			throw new JsonParseException("The given JSON string did not contain a username field.");
		}
		
		int fieldStartIndex = serializedUser.indexOf("username");
		int separator = serializedUser.indexOf(':', fieldStartIndex);
		int startIndex = serializedUser.indexOf('"', separator) + 1;
		int endIndex = serializedUser.indexOf('"', startIndex);
		
		String username = serializedUser.substring(startIndex, endIndex);
		
		logger.log(Level.FINE, "Username parsing success!");
		return username;
	}
	
	/**
	 * Creates an Admin user if one does not exist
	 */
	public User createAdmin()
	{
		logger.log(Level.INFO, "Adding an admin");

		User p = new User("Admin", "admin", "password", 0);

		try {
			if(getEntity(null,p.getUsername())[0] == null)
			{
				String newPassword = "password";
				String hashedPassword = this.passwordHash.generateHash(newPassword);

				p.setPassword(hashedPassword);
				
				p.setRole(Role.ADMIN);
				
				save(null,p);
			}
			else
			{
				p = getEntity(null,p.getUsername())[0];
			}
		} catch (WPISuiteException e) {
		}

		logger.log(Level.INFO, "Admin creation success!");
		
		return p;
	}

}
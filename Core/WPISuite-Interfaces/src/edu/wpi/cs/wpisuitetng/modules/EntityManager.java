/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    twack
 *    mpdelladonna
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;

/**
 * Interface for all EntityManagers. Enforces standards for interaction
 * 	with the Model class T.
 * @author twack, mpdelladonna
 *
 * @param <T>	The Model Class
 */
public interface EntityManager<T extends Model> 
{
	/* Create (Entity Builder) */
	/**
	 * Defines Model-specific instantiation
	 * @return	an instance of this Manager's Model class T
	 * @throws BadRequestException if json cannot be parsed
	 * @throws ConflictException if entity already exists with unique identifier
	 */
	public T makeEntity(Session s, String content) throws BadRequestException, ConflictException, WPISuiteException;
	
	/* Retrieve */	
	/**
	 * Retrieves the entity with the given unique identifier, id.
	 * @param id	the unique identifier value
	 * @return	the entity with the given ID
	 * @throws NotFoundException if entity does not exist
	 */
	public T[] getEntity(Session s, String id) throws NotFoundException, WPISuiteException;
	
	/**
	 * Retrieves all entities of Model class T
	 * @return	an ArrayList<T> with all instances of T
	 */
	public T[] getAll(Session s) throws WPISuiteException;
	
	
	/* Update */
	/**
	 * 
	 * @param s the session of the User executing this action
	 * @param content - JSON representation of the model updates
	 * @return the updated model
	 * @throws WPISuiteException
	 */
	public T update(Session s, String content) throws WPISuiteException;
	
	/**
	 * Saves the given model of class T to the database
	 * @param model	the Model to update.
	 */
	public void save(Session s, T model) throws WPISuiteException;
	
	/* Delete */
	/**
	 * Deletes the entity with the given unique identifier, id.
	 * @param id	the unique identifier for the entity
	 */
	public boolean deleteEntity(Session s, String id) throws WPISuiteException;
	
	/* Advanced Get*/
	/**
	 * This method is accessed through the API by a GET request to the AdvancedAPI
	 * @param s	The session that requested this action
	 * @param args[] A String array containing the entire path of the GET that initiated this action
	 */
	public String advancedGet(Session s, String[] args) throws WPISuiteException;
	
	/**
	 * Deletes all entities of Model class T
	 */
	public void deleteAll(Session s) throws WPISuiteException;
	
	/* Utility Methods */
	/**
	 * 
	 * @return	the number of records of this Manager's Model class T
	 */
	public int Count() throws WPISuiteException;

	/* Advanced Put*/
	/**
	 * **************
	 * A note about the content body.  Must not contain any line breaks.  Only the first line is passed 
	 * to this function.                                            
	 * **************
	 * This method is accessed through the API by a PUT request to the AdvancedAPI
	 * @param s	The session that requested this action
	 * @param args[] A String array containing the entire path of the PUT that initiated this action
	 * @param content The content body of the request
	 */
	public String advancedPut(Session s, String[] args, String content) throws WPISuiteException;

	/*Advanced Post*/
	/**
	 * 
	 * **********************
	 * A note about advanced post.  the content body should not contain any line breaks.
	 * only the first line will be passed through to the function.
	 * **********************
	 * This method is similar to the Advanced Put method, except that where ADvanced Put recieves the entire path
	 * as a String array, Advanced Post only receives the third path argument, for example API/advanced/module/model/argument
	 * The reasoning behind this was to allow module developers who required more methods a way to multiplex this 
	 * one call into multiple different functions.  By using the argument as a key in a switch statement or if else block, 
	 * they can process the content payload through an unlimited number of functions.
	 * 
	 * @param s - The session that requested this action
	 * @param string - A path identifier [/module/model/identifier]
	 * @param content - The String payload of the request
	 * @return - a String to be sent back as the body of the request
	 */
	public String advancedPost(Session s, String string, String content) throws WPISuiteException;
}

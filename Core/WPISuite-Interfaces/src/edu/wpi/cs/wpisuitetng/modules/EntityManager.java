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
}

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
	 */
	public T makeEntity(String content);
	
	/* Retrieve */	
	/**
	 * Retrieves the entity with the given unique identifier, id.
	 * @param id	the unique identifier value
	 * @return	the entity with the given ID
	 */
	public T[] getEntity(String id);
	
	/**
	 * Retrieves all entities of Model class T
	 * @return	an ArrayList<T> with all instances of T
	 */
	public T[] getAll();
	
	
	/* Update */
	/**
	 * Saves the given model of class T to the database
	 * @param model	the Model to update.
	 */
	public void save(T model);
	
	/* Delete */
	/**
	 * Deletes the entity with the given unique identifier, id.
	 * @param id	the unique identifier for the entity
	 */
	public boolean deleteEntity(String id);
	
	/**
	 * Deletes all entities of Model class T
	 */
	public void deleteAll();
	
	/* Utility Methods */
	/**
	 * 
	 * @return	the number of records of this Manager's Model class T
	 */
	public int Count();
}

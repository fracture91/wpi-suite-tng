/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bgaffey
 *    rchamer
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.database;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.Model;

/**
 * Interface that dictates database behavior 
 * @author rchamer
 * @param <C>
 *
 */
public interface DatabaseInterface {
	
	
	
	
	/** Retrieves objects of type objectType when the value of the field
	 * fieldName equals compareValue
	 * The object type is the class of the object to be retrieved
	 * 
	 * @param objectType - Class of object to be retrieved
	 * @param fieldName - Name of the field that will be compared against
	 * @param compareValue - Value of field, fieldName, that is being queried against
	 * @return The object that is retrieved
	 */
	public List<Model> retrieve(final Class anObjectQueried, String aFieldName, final Object theGivenValue);
	
	/**
	 * Updates the object with the value uniqueID for the field fieldName
	 * and changes the value of the field changeField to the value changeValue
	 * 
	 * @param objectType - object type of object to be retrieved
	 * @param fieldName - field whose value the object will be retrieved by
	 * @param uniqueID - value of the field that the object is retrieved by
	 * @param changeValue - field whose value will be changed 
	 */
	public void update(Class objectType, String fieldName, Object uniqueID, String changeField, Object changeValue);
	
	/**
	 * Deletes the object of type objectType which has the value uniqueID in the field fieldName
	 * 
	 * For this method to work there must be a setter of the form setFieldName which returns
	 * a copy of the updated object
	 * @param objectType
	 * @param fieldName
	 * @param uniqueID
	 * @return The object that was deleted
	 */
	public Model delete(Class objectType, String fieldName, Object uniqueID);

	
	/**
	 * Saves the given model in the database
	 * @param aModel
	 * @return true if the save was successful.
	 */
	public boolean save(Model aModel);
}

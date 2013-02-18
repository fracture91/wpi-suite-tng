/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    ??
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.database;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

public interface Data 
{
	/**
	 * Saves aTNG passed into the database
	 * @param aModel - The model to be saved
	 * @return true if save was successful, false otherwise
	 */
	public <T> boolean save(T aModel);
	
	/**
	 * Saves aTNG passed into the database
	 * @param aModel - the Model to save
	 * @param aProject - the Project the model is associated with 
	 * @return true if save was successful, false otherwise
	 */
	public <T> boolean save(T aModel, Project aProject);
	
	/**
	 * Retrieves the object of the class anObjectQueried with the value
	 * theGivenValue in the field aFieldName
	 * @param anObjectQueried - class of object to be retrieved
	 * @param aFieldName - field that object will be retrieved by
	 * @param theGivenValue - value of aFieldName
	 * @return
	 * @throws WPISuiteException 
	 */
	public List<Model> retrieve(@SuppressWarnings("rawtypes") final Class anObjectQueried, String aFieldName, final Object theGivenValue) throws WPISuiteException;
	
	
	/**
	 * Retrieves the object of the class anObjectQueried with the value
	 * theGivenValue in the field aFieldName
	 * @param anObjectQueried - class of object to be retrieved
	 * @param aFieldName - field that object will be retrieved by
	 * @param theGivenValue - value of aFieldName
	 * @param theProject - what Project to search in 
	 * @return
	 * @throws WPISuiteException 
	 */
	public List<Model> retrieve(@SuppressWarnings("rawtypes") final Class anObjectQueried, String aFieldName, final Object theGivenValue, final Project theProject) throws WPISuiteException;
	
	
	/**
	 * Deletes aTNG from the database
	 * @param aTNG
	 * @return the object that was deleted
	 */
	public <T> T delete(T aTNG);
	
	/**
	 * Updates an object in the database.  
	 * @param anObjectToBeModified - Class of object to be updated
	 * @param fieldName - Field of object that object is being identified by
	 * @param uniqueID - Value of fieldName
	 * @param changeField - Field that will be changed through the update
	 * @param changeValue - Value that changeField will be changed to
	 * @throws WPISuiteException 
	 */
	public void update(final Class anObjectToBeModified, String fieldName, Object uniqueID, String changeField, Object changeValue) throws WPISuiteException;
	
	/**
	 * Retrieves all objects of the the same class as aSample in the database
	 * @param aSample - Object whose class will be used 
	 * @return List of all of the objects of the class of aSample
	 */
	public <T> List<T> retrieveAll(T aSample);

	/**
	 * Retrieves all objects of the the same class as aSample in the database
	 * @param aSample - Object whose class will be used 
	 * @param aProject - Project to Query within
	 * @return List of all of the objects of the class of aSample
	 */
	public <T> List<Model> retrieveAll(T aSample, Project aProject);
	
	/**
	 * Deletes all objects of the same class as aSample in the database
	 * @param aSample - Object whose class will be used
	 * @return List of all the objects of the class of aSample which were deleted
	 */
	public <T> List<T> deleteAll(T aSample);
	
	/**
	 * Deletes all objects of the same class as aSample in the database
	 * @param aSample - Object whose class will be used
	 * @param aProject - Project to query in to delete from
	 * @return List of all the objects of the class of aSample which were deleted
	 */
	public <T> List<Model> deleteAll(T aSample, Project aProject);
	
	List<Model> andRetrieve(final Class anObjectQueried, String[] aFieldNameList, final List<Object> theGivenValueList) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;
	
	List<Model> orRetrieve(final Class anObjectQueried, String[] aFieldNameList, final List<Object> theGivenValueList) throws WPISuiteException, IllegalAccessException, InvocationTargetException;
	
	public List<Model> complexRetrieve(final Class andanObjectQueried, String[] andFieldNameList, final List<Object> andGivenValueList, final Class orAnObjectQueried, String[] orFieldNameList, final List<Object> orGivenValueList) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;

}

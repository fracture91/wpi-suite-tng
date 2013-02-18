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
	 * @return List<Model> - list of the models matching the given criteria
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
	 * @return List<Model> - list of the models matching the given criteria
	 * @throws WPISuiteException 
	 */
	public List<Model> retrieve(@SuppressWarnings("rawtypes") final Class anObjectQueried, String aFieldName, final Object theGivenValue, final Project theProject) throws WPISuiteException;
	
	
	/**
	 * Deletes aModel from the database
	 * @param aModel the object to be deleted
	 * @return the object that was deleted
	 */
	public <T> T delete(T aModel);
	
	/**
	 * Updates an object in the database.  
	 * @param anObjectToBeModified - Class of object to be updated
	 * @param fieldName - Field of object that object is being identified by
	 * @param uniqueID - Value of fieldName
	 * @param changeField - Field that will be changed through the update
	 * @param changeValue - Value that changeField will be changed to
	 * @throws WPISuiteException 
	 */
	@SuppressWarnings("rawtypes")
	public void update(final Class anObjectToBeModified, String fieldName, Object uniqueID, 
			String changeField, Object changeValue) throws WPISuiteException;
	
	/**
	 * Updates an object in the database.  
	 * @param anObjectToBeModified - Class of object to be updated
	 * @param fieldName - Field of object that object is being identified by
	 * @param uniqueID - Value of fieldName
	 * @param changeField - Field that will be changed through the update
	 * @param changeValue - Value that changeField will be changed to
	 * @throws WPISuiteException 
	 */
	@SuppressWarnings("rawtypes")
	public void update(final Class anObjectToBeModified, String fieldName, Object uniqueID, 
			String changeField, Object changeValue, Project aProject) throws WPISuiteException;
	
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
	
	/**
	 * Retrieves a list of all the models matching all the given fields to the given value in the database
	 * @param anObjectQueried - Class of the object to be retrieved
	 * @param aFieldNameList - An Array of Strings matching the names of the fields to check against
	 * @param theGivenValueList - An Array of values to compare the fields to. These must appear in the same order
	 * @return List of all the models matching all of the given the fields and values
	 * @throws WPISuiteException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	List<Model> andRetrieve(final Class anObjectQueried, String[] aFieldNameList, 
			final List<Object> theGivenValueList) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;
	
	/**
	 * Retrieves a list of all the models matching all the given fields to the given value in the database
	 * @param anObjectQueried - Class of the object to be retrieved
	 * @param aFieldNameList - An Array of Strings matching the names of the fields to check against
	 * @param theGivenValueList - An Array of values to compare the fields to. These must appear in the same order
	 * @param aProject - The project that all returned objects must be from
	 * @return List of all the models matching all of the given the fields and values in the given project
	 * @throws WPISuiteException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	List<Model> andRetrieve(final Class anObjectQueried, String[] aFieldNameList, 
			final List<Object> theGivenValueList, final Project aProject) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;
	
	/**
	 * Retrieves a list of all the models matching one of the given fields to the given value in the database
	 * @param anObjectQueried - Class of the object to be retrieved
	 * @param aFieldNameList - An Array of Strings matching the names of the fields to check against
	 * @param theGivenValueList - An Array of values to compare the fields to. These must appear in the same order
	 * @return List of all the models matching one of the given the fields and values
	 * @throws WPISuiteException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	List<Model> orRetrieve(final Class anObjectQueried, String[] aFieldNameList, final List<Object> theGivenValueList) throws WPISuiteException, IllegalAccessException, InvocationTargetException;
	
	/**
	 * Retrieves a list of all the models matching one of the given fields to the given value in the database
	 * @param anObjectQueried - Class of the object to be retrieved
	 * @param aFieldNameList - An Array of Strings matching the names of the fields to check against
	 * @param theGivenValueList - An Array of values to compare the fields to. These must appear in the same order
	 * @param aProject - The project that all returned objects must be from
	 * @return List of all the models matching one of the given the fields and values in the given project
	 * @throws WPISuiteException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	List<Model> orRetrieve(final Class anObjectQueried, String[] aFieldNameList, 
			final List<Object> theGivenValueList, final Project aProject) throws WPISuiteException, IllegalAccessException, InvocationTargetException;
	
	/**
	 * Retrieves a list of all models matching all of the given fields to the given values of the "And"s 
	 * and matching one of the given fields to the given values of the "Or"s in the database
	 * @param andanObjectQueried - Class of the object to be queried with the and
	 * @param andFieldNameList - The names of the fields to be queried with an and
	 * @param andGivenValueList - The values of the fields to be queried with an and
	 * @param orAnObjectQueried - Class of the object to be queried with an or
	 * @param orFieldNameList - The names of the fields to be queried with an or
	 * @param orGivenValueList - the values of the fields to be queried with an or
	 * @return A list of models that match all of the and values and at least 1 of the or values 
	 * @throws WPISuiteException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	public List<Model> complexRetrieve(final Class andanObjectQueried, String[] andFieldNameList, 
			final List<Object> andGivenValueList, final Class orAnObjectQueried, String[] orFieldNameList, 
			final List<Object> orGivenValueList) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;
	
	/**
	 * Retrieves a list of all models matching all of the given fields to the given values of the "And"s 
	 * and matching one of the given fields to the given values of the "Or"s in the database from the given project
	 * @param andanObjectQueried - Class of the object to be queried with the and
	 * @param andFieldNameList - The names of the fields to be queried with an and
	 * @param andGivenValueList - The values of the fields to be queried with an and
	 * @param orAnObjectQueried - Class of the object to be queried with an or
	 * @param orFieldNameList - The names of the fields to be queried with an or
	 * @param orGivenValueList - the values of the fields to be queried with an or
	 * @param aProject - The project that all matching objects must come from
	 * @return A list of models that match all of the and values and at least 1 of the or values from the given project
	 * @throws WPISuiteException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	public List<Model> complexRetrieve(final Class andanObjectQueried, String[] andFieldNameList, 
			final List<Object> andGivenValueList, final Class orAnObjectQueried, String[] orFieldNameList, 
			final List<Object> orGivenValueList, final Project aProject) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;

	
}

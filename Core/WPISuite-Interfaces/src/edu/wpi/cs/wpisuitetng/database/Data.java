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
	 * @param aModel - The model to be saved
	 * @param depth - Depth to which the object should be saved
	 * @return true if save was successful, false otherwise
	 */
	public <T> boolean save(T aModel, int depth);
	
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
	
	/**
	 *  For this function to work you need to have a getter that takes zero arguments,
	 *  and has the name
	 *  convention of get + the given fieldName (ie getID for the field id from an object). The value can
	 *  be of any type, provided that there is a .equals method for it.  To query
	 *  by something else, like by a user object or defect object, you must create a .equals 
	 *  function for it, that will return true if and only if all the fields of the object 
	 *  have the same values.
	 * @param anObjectQueried the class type of the object being queried. You can get this by giving
	 * an object of the desired type and calling .getClass()
	 * @param aFieldNameList the field Names of the values in the object you are querying about (this should be 
	 * the suffix of the getter. So for getID you would make this field be "ID".)  The order of field names and 
	 * values must correspond.  If the first field is Id, the first value in theGivenValueList must be the value for the id
	 * @param theGivenValue The values of fields in aFieldNameList that you want all returned objects to have
	 * at least one of.  
	 * @return a List of objects of the given type that have at least one of the given fields match the given value for that field
	 * @throws WPISuiteException 
	 */
	List<Model> andRetrieve(final Class anObjectQueried, String[] aFieldNameList, final List<Object> theGivenValueList) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;
	
	/**
	 *  For this function to work you need to have a getter that takes zero arguments,
	 *  and has the name
	 *  convention of get + the given fieldName (ie getID for the field id from an object). The value can
	 *  be of any type, provided that there is a .equals method for it.  To query
	 *  by something else, like by a user object or defect object, you must create a .equals 
	 *  function for it, that will return true if and only if all the fields of the object 
	 *  have the same values.
	 * @param anObjectQueried the class type of the object being queried. You can get this by giving
	 * an object of the desired type and calling .getClass()
	 * @param aFieldNameList the field Names of the values in the object you are querying about (this should be 
	 * the suffix of the getter. So for getID you would make this field be "ID".)  The order of field names and 
	 * values must correspond.  If the first field is Id, the first value in theGivenValueList must be the value for the Id
	 * @param theGivenValue The values of fields in aFieldNameList that you want all returned objects to have
	 * all of.  
	 * @return a List of objects of the given type that have all of the given fields match the given value for that field
	 * @throws WPISuiteException 
	 */
	List<Model> orRetrieve(final Class anObjectQueried, String[] aFieldNameList, final List<Object> theGivenValueList) throws WPISuiteException, IllegalAccessException, InvocationTargetException;
	
	/**
	 *  For this function to work you need to have a getter that takes zero arguments,
	 *  and has the name
	 *  convention of get + the given fieldName (ie getID for the field id from an object). The value can
	 *  be of any type, provided that there is a .equals method for it.  To query
	 *  by something else, like by a user object or defect object, you must create a .equals 
	 *  function for it, that will return true if and only if all the fields of the object 
	 *  have the same values
	 *  @param andAnObjectQueried - Class of the object you wish the and part of the query to be about
	 *  @param andFieldNameList - List of field names to be used in the and part of the query
	 *  @param andGivenValueList - List of given values to be used in the and part of the query.
	 *  These values will be compared to the values of the fields in the andFieldNameList
	 *  The order of the orGivenValueList and the orFieldNameList must be the same. If Id is first
	 *  in the orFieldNameList, it must be first in the orGivenValueList.
	 *  @param orAnObjectQueried - Class of the object you wish the or part of the query to be about
	 *  @param orFieldNameList - List of field names to be used in the or part of the query
	 *  @param orGivenValueList - List of given values to be used in the and part of the query.
	 *  These values will be compared to the values of the fields in the orFieldNameList.  
	 *  The order of the orGivenValueList and the orFieldNameList must be the same. If Id is first
	 *  in the orFieldNameList, it must be first in the orGivenValueList.
	 *  @return a List of objects that match the or query as well the objects that match the and query
	 *  @throws WPISuiteException 
	 */
	public List<Model> complexRetrieve(final Class andanObjectQueried, String[] andFieldNameList, final List<Object> andGivenValueList, final Class orAnObjectQueried, String[] orFieldNameList, final List<Object> orGivenValueList) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;

}

/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:rchamer, bgaffey, mpdelladonna
 *    
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.query.Predicate;
import com.db4o.reflect.jdk.JdkReflector;

import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class DataStore implements Data {

	static String WPI_TNG_DB ="WPISuite_TNG_local";
	private static DataStore myself = null;
	static ObjectContainer theDB;
	static ObjectServer server;
	static int PORT = 0;
	static String DB4oUser = "bgaffey";
	static String DB4oPass = "password";
	static String DB4oServer = "localhost";

	private static final Logger logger = Logger.getLogger(DataStore.class.getName());

	/**
	 * Get the single instance of the Database
	 * @return the only instance of the Database
	 */
	public static DataStore getDataStore()
	{
		if(myself == null)
		{
			logger.log(Level.FINE, "Opening connection to db4o database...");
			myself = new DataStore();
			// accessLocalServer
			// Please see Wiki for more information on the ServerConfiguration.
			ServerConfiguration config = Db4oClientServer.newServerConfiguration();
			config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
			config.common().objectClass(User.class).storeTransientFields(true); // Enables data persistence for passwords

			//Connect to the Database
			server = Db4oClientServer.openServer(config, WPI_TNG_DB, PORT);
			server.grantAccess(DB4oUser,DB4oPass);

			theDB = server.openClient();
			logger.log(Level.FINE, "Connected to db4o database!");
		}
		return myself;
	}

	/**
	 * Saves a Model into the database
	 * @param Model to save
	 */
	public <T> boolean save(T aModel){
		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		theDB.store(aModel);
		System.out.println("Stored " + aModel);
		logger.log(Level.FINE, "Saving model [" + aModel + "]");
		theDB.commit();
		return true;
	}

	/**
	 * Saves a Model associated with the given project into the database 
	 * @param Model to save
	 */
	public <T> boolean save(T aModel, Project aProject){
		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		((Model) aModel).setProject(aProject); //Sets the model's project to the given project
		theDB.store(aModel);
		System.out.println("Stored " + aModel);
		logger.log(Level.FINE, "Saving model [" + aModel + "]");
		theDB.commit();
		return true;
	}

	/**
	 * Retrieves objects of the given class with the given value for the given field. 
	 * This function is not project specific.
	 * @param anObjectQueried - The class type of the object being queried. 
	 * @param aFieldName - The field name of the value in the object you are querying.
	 * @param theGivenValue - The value of field aFieldName that you want all returned objects to have
	 * @return a List of objects of the given type that have the given field match the given value
	 * @throws WPISuiteException 
	 */
	@SuppressWarnings("rawtypes") //Ignore the warning about the use of type Class
	public List<Model> retrieve(final Class anObjectQueried, String aFieldName, final Object theGivenValue) throws WPISuiteException{
		/*  For this function to work you need to have a getter that takes zero arguments,
		 *  and has the name convention of get + the given fieldName (ie getID for the field id from an object). 
		 *  The value can be of any type, provided that there is a .equals method for it.  To query
		 *  by something else, like by a user object or defect object, you must create a .equals 
		 *  function for it, that will return true if and only if all the fields of the object 
		 *  have the same values.
		 *  anObjectQueried - You can get this by giving an object of the desired type and calling .getClass()
		 *  aFieldName - this should be the suffix of the getter. So for getID you would make this field be "ID"
		 */

		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		logger.log(Level.FINE, "Attempting Database Retrieve...");

		Method[] allMethods = anObjectQueried.getMethods();
		Method methodToBeSaved = null;
		for(Method m: allMethods){//Cycles through all of the methods in the class anObjectQueried
			if(m.getName().equalsIgnoreCase("get"+aFieldName)){
				methodToBeSaved = m; //saves the method called "get" + aFieldName
			}
		}
		final Method theGetter = methodToBeSaved;
		if(theGetter == null){
			logger.log(Level.WARNING, "Getter method was null during retrieve attempt");
			throw new WPISuiteException("Null getter method.");
		}

		List<Model> result = theDB.query(new Predicate<Model>(){
			public boolean match(Model anObject){
				try {
					return theGetter.invoke(anObjectQueried.cast(anObject)).equals(theGivenValue);
					//objects that have aFieldName equal to theGivenValue get added to the list 
				} catch (IllegalArgumentException e) {
					return false;
				} catch (IllegalAccessException e) {
					return false;
				} catch (InvocationTargetException e) {
					return false;         
				}
			}
		});

		System.out.println(result);
		theDB.commit();

		logger.log(Level.FINE, "Database Retrieve Success!");
		return result;
	}

	/**
	 * Retrieves the objects of the given class type with the given field value from only
	 * the given project. 
	 * @param anObjectQueried - The class type of the object being queried. 
	 * @param aFieldName - The field Name of the value in the object you are querying about.
	 * @param theGivenValue - The value of field aFieldName that you want all returned objects to have
	 * @param theProject - The Project that returned Models are required to belong to
	 * @return a List of objects of the given type that have the given field match the given value
	 * @throws WPISuiteException 
	 */
	@SuppressWarnings("rawtypes") //Ignore the warning about the use of type Class
	@Override
	public List<Model> retrieve(final Class anObjectQueried, String aFieldName,
			final Object theGivenValue, final Project theProject) throws WPISuiteException {

		//If no project is given, then the project neutral retrieve should be used 
		if(theProject == null){
			//If no project is given, just search the entire database
			retrieve(anObjectQueried, aFieldName, theGivenValue);
		}

		/*  For this function to work you need to have a getter that takes zero arguments,
		 *  and has the name convention of get + the given fieldName (ie getID for the field id from an object). 
		 *  The value can be of any type, provided that there is a .equals method for it.  To query
		 *  by something else, like by a user object or defect object, you must create a .equals 
		 *  function for it, that will return true if and only if all the fields of the object 
		 *  have the same values.
		 *  anObjectQueried - You can get this by giving an object of the desired type and calling .getClass()
		 *  aFieldName - this should be the suffix of the getter. So for getID you would make this field be "ID"
		 */

		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		logger.log(Level.FINE, "Attempting Database Retrieve...");
		Method[] allMethods = anObjectQueried.getMethods();
		Method methodToBeSaved = null;
		for(Method m: allMethods){//Cycles through all of the methods in the class anObjectQueried
			if(m.getName().equalsIgnoreCase("get"+aFieldName)){
				methodToBeSaved = m; //saves the method called "get" + aFieldName
			}
		}

		final Method theGetter = methodToBeSaved;
		if(theGetter == null){
			logger.log(Level.WARNING, "Getter method was null during retrieve attempt");
			throw new WPISuiteException("Null getter method.");
		}


		List<Model> result = theDB.query(new Predicate<Model>(){
			public boolean match(Model anObject){
				try {
					if(anObject.getProject() == null){
						return false;
					}
					else{
						return (anObject.getProject().getIdNum().equals(theProject.getIdNum())) &&
								theGetter.invoke(anObjectQueried.cast(anObject)).equals(theGivenValue);
						//objects that have the same ProjectID and aFieldName equal to theGivenValue get added to the list
					}
				} catch (IllegalAccessException e) {
					return false;
				} catch (IllegalArgumentException e) {
					return false;
				} catch (InvocationTargetException e) {
					return false;
				}
			}
		});

		System.out.println(result);
		theDB.commit();

		logger.log(Level.FINE, "Database Retrieve Success!");
		return result;

	}

	/**
	 * Retrieves all objects of the given Class from the Database. 
	 * @param aSample an object of the class we want to retrieve All of
	 * @return a List of all of the objects of the given class
	 */
	public <T> List<T> retrieveAll(T aSample){
		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		List<T> result = theDB.queryByExample(aSample.getClass());
		System.out.println("retrievedAll: "+result);
		theDB.commit();

		logger.log(Level.FINE, "Database RetrieveAll Performed");
		return result;
	}

	/**
	 * Retrieves all objects of the given Class in a given Project
	 * @param aSample an object of the class we want to retrieve All of
	 * @param aProject Project to search in for the objects
	 * @return a List of all of the objects of the given class
	 */
	public <T> List<Model> retrieveAll(T aSample, Project aProject){
		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		ArrayList<Model> result = new ArrayList<Model>(); 
		List<Model> allResults = theDB.queryByExample(aSample.getClass());
		for(Model theModel : allResults) {
			if(theModel.getProject() != null &&
					theModel.getProject().getIdNum().equalsIgnoreCase(aProject.getIdNum())){
				result.add(theModel);
			}
		}
		System.out.println("retrievedAll: "+result);
		theDB.commit();
		logger.log(Level.FINE, "Database RetrieveAll Performed");
		return result;
	}

	/** 
	 * Deletes the given object and returns the object if successful
	 * @param The object to be deleted
	 * @return The object being deleted
	 */
	public <T> T delete(T aTNG){
		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		logger.log(Level.FINE, "Database Delete Attempt...");
		//ObjectContainer client = server.openClient();
		ObjectSet<T> result = theDB.queryByExample(aTNG);
		T found = (T) result.next();
		theDB.delete(found);
		theDB.commit();

		logger.log(Level.FINE, "Database Delete Success!");
		//return "Deleted "+aTNG;
		return found;

	}
	
	/**
	 * Deletes all objects of the given Class in the database
	 * @param aSample an object of the class we want to retrieve All of
	 * @return a List of all of the objects deleted
	 */
	public <T> List<T> deleteAll(T aSample){
		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		List<T> toBeDeleted = retrieveAll(aSample);
		for(T aTNG: toBeDeleted){
			System.out.println("Deleting: "+aTNG);
			theDB.delete(aTNG);
		}
		theDB.commit();
		logger.log(Level.INFO, "Database Delete All performed");
		return toBeDeleted;

	}

	/**
	 * Deletes all objects of the given Class in a given Project
	 * @param aSample an object of the class we want to retrieve All of
	 * @param aProject Project to search in for the objects
	 * @return a List of all of the objects of the given class in the given project
	 */
	public <T> List<Model> deleteAll(T aSample, Project aProject){
		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		List<Model> toBeDeleted = retrieveAll(aSample, aProject);
		for(Model aTNG: toBeDeleted){
			System.out.println("Deleting: "+aTNG);
			theDB.delete(aTNG);
		}
		theDB.commit();

		logger.log(Level.INFO, "Database Delete All performed");
		return toBeDeleted;

	}


	/** 
	 * Updates the given field in every object which has the uniqueID value in a specific project
	 * @param anObjectToBeModified - Class of object to be updated
	 * @param fieldName - Field the object will be identified by
	 * @param uniqueID - Value of fieldName that the object will be identified by
	 * @param changeField - Field whose value will be changed
	 * @param changeValue - Value that changeField will be changed to
	 * @param aProject - The project the object to be updated belongs to
	 * @throws WPISuiteException 
	 */
	@SuppressWarnings("rawtypes") //Ignore the warning about the use of type Class
	public void update(final Class anObjectToBeModified, String fieldName, Object uniqueID, String changeField, Object changeValue, Project aProject) throws WPISuiteException{
		if(aProject == null){
			update(anObjectToBeModified, fieldName, uniqueID, changeField, changeValue);
		}
		logger.log(Level.INFO, "Database Update Attempt...");

		List<? extends Object> objectsToUpdate = retrieve(anObjectToBeModified, fieldName, uniqueID, aProject);
		Object theObject;
		for(int i = 0; i < objectsToUpdate.size(); i++){
			final Class <?> objectClass = objectsToUpdate.get(i).getClass();
			Method[] allMethods = objectClass.getMethods();
			Method methodToBeSaved = null;
			for(Method m: allMethods){
				if(m.getName().equalsIgnoreCase("set"+changeField)){
					methodToBeSaved = m;
				}
			}
			final Method theSetter = methodToBeSaved;
			if(theSetter == null){
				logger.log(Level.WARNING, "Setter method was null during retrieve attempt");
				throw new WPISuiteException("Null setter method.");
			}

			try {
				theObject = (Object) theSetter.invoke(objectsToUpdate.get(i), changeValue);
				save(theObject, aProject);
			} catch (IllegalArgumentException e) {
				logger.log(Level.WARNING, "Database Update Failed!");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				logger.log(Level.WARNING, "Database Update Failed!");
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				logger.log(Level.WARNING, "Database Update Failed!");
				e.printStackTrace();
			}

			theDB.commit();
		}

		logger.log(Level.INFO, "Database Update Success!");
	}

	/** 
	 * Updates the given field in every object which has the uniqueID value
	 * @param anObjectToBeModified - Class of object to be updated
	 * @param fieldName - Field the object will be identified by
	 * @param uniqueID - value of fieldName that the object will be identified by
	 * @param changeField - Field whose value will be changed
	 * @param changeValue - Value that changeField will be changed to
	 * @throws WPISuiteException 
	 */
	@SuppressWarnings("rawtypes") //Ignore the warning about the use of type Class
	public void update(final Class anObjectToBeModified, String fieldName, Object uniqueID, String changeField, Object changeValue) throws WPISuiteException{
		/*
		 * For this function to work you need to have a setter that takes the value to change,
		 *  the field to and is named in the convention
		 *  convention of set + the given fieldName (ie setID for the field ID from an object). 
		 *  The value can be of any type, provided that there is a .equals method for it. 
		 *  To query by something else, like by a user object or defect object, you must create a .equals 
		 *  function for it, that will return true if and only if all the fields of the object 
		 *  have the same values.
		 */
		logger.log(Level.INFO, "Database Update Attempt...");

		List<? extends Object> objectsToUpdate = retrieve(anObjectToBeModified, fieldName, uniqueID);
		Object theObject;
		for(int i = 0; i < objectsToUpdate.size(); i++){
			final Class <?> objectClass = objectsToUpdate.get(i).getClass();
			Method[] allMethods = objectClass.getMethods();
			Method methodToBeSaved = null;
			for(Method m: allMethods){
				if(m.getName().equalsIgnoreCase("set"+changeField)){
					methodToBeSaved = m;
				}
			}
			final Method theSetter = methodToBeSaved;
			if(theSetter == null){
				logger.log(Level.WARNING, "Setter method was null during retrieve attempt");
				throw new WPISuiteException("Null setter method.");
			}

			try {
				theObject = (Object) theSetter.invoke(objectsToUpdate.get(i), changeValue);
				save(theObject);
			} catch (IllegalArgumentException e) {
				logger.log(Level.WARNING, "Database Update Failed!");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				logger.log(Level.WARNING, "Database Update Failed!");
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				logger.log(Level.WARNING, "Database Update Failed!");
				e.printStackTrace();
			}

			theDB.commit();
		}

		logger.log(Level.INFO, "Database Update Success!");
	}

	/**
	 * Retrieves objects which do not have the given value
	 * @param anObjectQueried - Class of the object to be queried
	 * @param aFieldName - name of the Field to check
	 * @param theGivenValue - Value the field should not equal
	 * @return A List of Models containing all of the models that do not match the given field
	 * @throws WPISuiteException
	 */
	@SuppressWarnings("rawtypes") //Ignore the warning about the use of type Class
	public List<Model> notRetrieve(final Class anObjectQueried, String aFieldName, final Object theGivenValue) throws WPISuiteException{
		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		Method[] allMethods = anObjectQueried.getMethods();
		Method methodToBeSaved = null;
		for(Method m: allMethods){//Cycles through all of the methods in the class anObjectQueried
			if(m.getName().equalsIgnoreCase("get"+aFieldName)){
				methodToBeSaved = m; //saves the method called "get" + aFieldName
			}
		}
		final Method theGetter = methodToBeSaved;
		if(theGetter == null){
			logger.log(Level.WARNING, "Getter method was null during retrieve attempt");
			throw new WPISuiteException("Null getter method.");
		}

		List<Model> result = theDB.query(new Predicate<Model>(){
			public boolean match(Model anObject){
				try {
					Object foundobj = theGetter.invoke(anObjectQueried.cast(anObject));
					return (!(foundobj.equals(theGivenValue)));//objects that have aFieldName equal to theGivenValue get added to the list 
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					return false;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					return false;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					return false;         
				}
			}
		});

		System.out.println(result);
		return result;
	}
	
	/**
	 * Retrieves objects which match one of the given fields to one of the given values
	 * @param anObjectQueried - Class of the object to be queried
	 * @param aFieldName - Array of names of the Field to check in order
	 * @param theGivenValueList - Array of values the field should equal in order
	 * @return A List of Models containing all of the models that match one of the given fields
	 * @throws WPISuiteException
	 */
	@SuppressWarnings("rawtypes") //Ignore the warning about the use of type Class
	public List<Model> orRetrieve(final Class anObjectQueried, String[] aFieldNameList, final List<Object> theGivenValueList) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		final int aFieldNameListSize = aFieldNameList.length;
		final int theGivenValueListSize = theGivenValueList.size();
		if(aFieldNameListSize != theGivenValueListSize)
		{
			throw new WPISuiteException("Gave too few Fields or Values");
		}
		Method[] allMethods = anObjectQueried.getMethods();
		int i=0;
		List<Method> methodsToBeSaved = new ArrayList<Method>();
		while(i < aFieldNameListSize){
			for(Method m: allMethods){//Cycles through all of the methods in the class anObjectQueried
				if(i == aFieldNameListSize){
					//do nothing
				}
				else if(m.getName().equalsIgnoreCase("get" + aFieldNameList[i])){
					methodsToBeSaved.add(m); //saves the method called "get" + aFieldName
					i++;
				}
			}
		}
		final List<Method> theGetter = methodsToBeSaved;
		if(theGetter.size() == 0){
			logger.log(Level.WARNING, "Getter method was null during retrieve attempt");
			throw new WPISuiteException("Null getter method.");
		}
		final int theGettersSize = theGetter.size();
		int j = 0;
		List<Model> fullresult = new ArrayList<Model>();
		List<Model> result = new ArrayList<Model>(); 
		for(j=0; j < theGettersSize ; j++){
			final int finalcount = j;
			final Method getBack = theGetter.get(finalcount);
			final Object givenValue = theGivenValueList.get(finalcount);
			result = theDB.query(new Predicate<Model>(){
				public boolean match(Model anObject){
					try {
						return getBack.invoke(anObjectQueried.cast(anObject)).equals(givenValue);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					return false;
				}
			});
			fullresult.addAll(result);
		}
		System.out.println(fullresult);
		return fullresult;
	}

	/**
	 * Retrieves objects which match all of the given fields to all of the given values
	 * @param anObjectQueried - Class of the object to be queried
	 * @param aFieldName - Array of names of the Field to check in order
	 * @param theGivenValueList - Array of values the field should equal in order
	 * @return A List of Models containing all of the models that match all of the given fields
	 * @throws WPISuiteException
	 */
	@SuppressWarnings("rawtypes") //Ignore the warning about the use of type Class
	public List<Model> andRetrieve(final Class anObjectQueried, String[] aFieldNameList, final List<Object> theGivenValueList) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		final int aFieldNameListSize = aFieldNameList.length;
		final int theGivenValueListSize = theGivenValueList.size();
		if(aFieldNameListSize != theGivenValueListSize)
		{
			throw new WPISuiteException("Gave too few fields or values");
		}
		Method[] allMethods = anObjectQueried.getMethods();
		int i=0;
		List<Method> methodsToBeSaved = new ArrayList<Method>();
		while(i < aFieldNameListSize){
			for(Method m: allMethods){//Cycles through all of the methods in the class anObjectQueried
				if(i == aFieldNameListSize){
					//do nothing
				}
				else if(m.getName().equalsIgnoreCase("get" + aFieldNameList[i])){
					methodsToBeSaved.add(m); //saves the method called "get" + aFieldName
					i++;
				}
			}
		}
		final List<Method> theGetter = methodsToBeSaved;
		if(theGetter.size() == 0){
			logger.log(Level.WARNING, "Getter method was null during retrieve attempt");
			throw new WPISuiteException("Null getter method.");
		}
		final int theGettersSize = theGetter.size();
		int j = 0;
		List<Model> fullresult = new ArrayList<Model>();
		List<Model> result = new ArrayList<Model>(); 
		for(j=0; j < theGettersSize ; j++){
			final int finalcount = j;
			final Method getBack = theGetter.get(finalcount);
			final Object givenValue = theGivenValueList.get(finalcount);
			result = theDB.query(new Predicate<Model>(){
				public boolean match(Model anObject){
					try {
						return getBack.invoke(anObjectQueried.cast(anObject)).equals(givenValue);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					return false;
				}
			});
			fullresult.addAll(result);
			for(int k=0;k<fullresult.size();k++){
				for(j=0; j < theGettersSize ; j++)
				{	
					Object fieldValue = theGetter.get(j).invoke(fullresult.get(k));
					Object theGivenValue = theGivenValueList.get(j);
					if(!(fieldValue.equals(theGivenValue)))
					{
						fullresult.remove(k);
					}
					else
					{
						//it has the required values so it can stay in the list to be returned
					}
				}
			}
		}
		System.out.println(fullresult);
		return fullresult;
	}

	/**
	 * Retrieves objects which match all of the given fields to all of the given values
	 * @param anObjectQueried - Class of the object to be queried
	 * @param aFieldName - Array of names of the Field to check in order
	 * @param theGivenValueList - Array of values the field should equal in order
	 * @param aProject - Project that all returned models must belong to
	 * @return A List of Models containing all of the models that match all of the given fields
	 * @throws WPISuiteException
	 */
	@SuppressWarnings("rawtypes") //Ignore the warning about the use of type Class
	public List<Model> andRetrieve(final Class anObjectQueried, String[] aFieldNameList, final List<Object> theGivenValueList, final Project aProject) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		if(aProject == null){
			return andRetrieve(anObjectQueried, aFieldNameList, theGivenValueList);
		}
		final int aFieldNameListSize = aFieldNameList.length;
		final int theGivenValueListSize = theGivenValueList.size();
		if(aFieldNameListSize != theGivenValueListSize)
		{
			throw new WPISuiteException("Too few fields or values given");
		}
		Method[] allMethods = anObjectQueried.getMethods();
		int i=0;
		List<Method> methodsToBeSaved = new ArrayList<Method>();
		while(i < aFieldNameListSize){
			for(Method m: allMethods){//Cycles through all of the methods in the class anObjectQueried
				if(i == aFieldNameListSize){
					//do nothing
				}
				else if(m.getName().equalsIgnoreCase("get" + aFieldNameList[i])){
					methodsToBeSaved.add(m); //saves the method called "get" + aFieldName
					i++;
				}
			}
		}
		final List<Method> theGetter = methodsToBeSaved;
		if(theGetter.size() == 0){
			logger.log(Level.WARNING, "Getter method was null during retrieve attempt");
			throw new WPISuiteException("Null getter method.");
		}
		final int theGettersSize = theGetter.size();
		int j = 0;
		List<Model> fullresult = new ArrayList<Model>();
		List<Model> result = new ArrayList<Model>(); 
		for(j=0; j < theGettersSize ; j++){
			final int finalcount = j;
			final Method getBack = theGetter.get(finalcount);
			final Object givenValue = theGivenValueList.get(finalcount);
			result = theDB.query(new Predicate<Model>(){
				public boolean match(Model anObject){
					try {
						return anObject.getProject().getIdNum().equals(aProject.getIdNum()) && 
								getBack.invoke(anObjectQueried.cast(anObject)).equals(givenValue);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					return false;
				}
			});
			fullresult.addAll(result);
			for(int k=0;k<fullresult.size();k++){
				for(j=0; j < theGettersSize ; j++)
				{	
					Object fieldValue = theGetter.get(j).invoke(fullresult.get(k));
					Object theGivenValue = theGivenValueList.get(j);
					if(!(fieldValue.equals(theGivenValue)))
					{
						fullresult.remove(k);
					}
					else
					{
						//it has the required values so it can stay in the list to be returned
					}
				}
			}
		}
		System.out.println(fullresult);
		return fullresult;
	}

	/**
	 * Retrieves objects which match one of the given fields to one of the given values
	 * @param anObjectQueried - Class of the object to be queried
	 * @param aFieldName - Array of names of the Field to check in order
	 * @param theGivenValueList - Array of values the field should equal in order
	 * @param aProject - Project that all returned models must belong to
	 * @return A List of Models containing all of the models that match one of the given fields
	 * @throws WPISuiteException
	 */
	@SuppressWarnings("rawtypes") //Ignore the warning about the use of type Class
	public List<Model> orRetrieve(final Class anObjectQueried, String[] aFieldNameList, final List<Object> theGivenValueList, final Project aProject) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		// Please see Wiki for more information on the ServerConfiguration.
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		if(aProject==null){
			return orRetrieve(anObjectQueried, aFieldNameList, theGivenValueList);
		}
		final int aFieldNameListSize = aFieldNameList.length;
		final int theGivenValueListSize = theGivenValueList.size();
		if(aFieldNameListSize != theGivenValueListSize)
		{
			throw new WPISuiteException();
		}
		Method[] allMethods = anObjectQueried.getMethods();
		int i=0;
		List<Method> methodsToBeSaved = new ArrayList<Method>();
		while(i < aFieldNameListSize){
			for(Method m: allMethods){//Cycles through all of the methods in the class anObjectQueried
				if(i == aFieldNameListSize){
					//do nothing
				}
				else if(m.getName().equalsIgnoreCase("get" + aFieldNameList[i])){
					methodsToBeSaved.add(m); //saves the method called "get" + aFieldName
					i++;
				}
			}
		}
		final List<Method> theGetter = methodsToBeSaved;
		if(theGetter.size() == 0){
			logger.log(Level.WARNING, "Getter method was null during retrieve attempt");
			throw new WPISuiteException("Null getter method.");
		}
		final int theGettersSize = theGetter.size();
		int j = 0;
		List<Model> fullresult = new ArrayList<Model>();
		List<Model> result = new ArrayList<Model>(); 
		for(j=0; j < theGettersSize ; j++){
			final int finalcount = j;
			final Method getBack = theGetter.get(finalcount);
			final Object givenValue = theGivenValueList.get(finalcount);
			result = theDB.query(new Predicate<Model>(){
				public boolean match(Model anObject){
					try {
						return anObject.getProject().getIdNum().equals(aProject.getIdNum()) && 
								getBack.invoke(anObjectQueried.cast(anObject)).equals(givenValue);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					return false;
				}
			});
			fullresult.addAll(result);
		}
		System.out.println(fullresult);
		return fullresult;
	}
	
	/**
	 * Retrieves objects which match all of the given "and" fields to all of the given "and" values AND 
	 * which match one of the given "or" fields to one of the given "or" values 
	 * @param andAnObjectQueried - Class of the "and" object to be queried
	 * @param andFieldNameList - Array of names of the "and" Fields to check in order
	 * @param andGivenValueList - Array of values the "and" fields should equal in order
	 * @param orObjectQueried - Class of the "or" object to be queried
	 * @param orFieldNameList - Array of names of the "or" Fields to check in order
	 * @param orGivenValueList - Array of values the "or" fields should equal in order
	 * @return A List of Models containing all of the models that match all of the given fields
	 * @throws WPISuiteException
	 */
	@SuppressWarnings("rawtypes") //Ignore the warning about the use of type Class
	public List<Model> complexRetrieve(final Class andAnObjectQueried, String[] andFieldNameList, final List<Object> andGivenValueList, 
			final Class orAnObjectQueried, String[] orFieldNameList, final List<Object> orGivenValueList) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		List<Model> returnList = new ArrayList<Model>();
		List<Model> bothList = new ArrayList<Model>();
		List<Model> orList = new ArrayList<Model>();
		List<Model> andList = new ArrayList<Model>();
		orList = orRetrieve(orAnObjectQueried, orFieldNameList, orGivenValueList);
		andList = andRetrieve(andAnObjectQueried, andFieldNameList, andGivenValueList);
		bothList.addAll(orList);
		bothList.addAll(andList);
		returnList = removeDuplicates(bothList);
		return returnList;
	}

	/**
	 * Returns a List with all duplicate items removed
	 * @param listWithDuplicates - A list with duplicate items
	 * @return a list with no duplicate items
	 */
	public List<Model> removeDuplicates(List<Model> listWithDuplicates)
	{
		List<Model> oldList = new ArrayList<Model>();
		oldList = listWithDuplicates;
		List<Model> newList = new ArrayList<Model>();
		for(Model m: oldList)
		{
			if(!(newList.contains(m)))
			{
				newList.add(m);
			}
		}
		return newList;
	}

	/**
	 * Retrieves objects which match all of the given "and" fields to all of the given "and" values AND 
	 * which match one of the given "or" fields to one of the given "or" values in the given project
	 * @param andAnObjectQueried - Class of the "and" object to be queried
	 * @param andFieldNameList - Array of names of the "and" Fields to check in order
	 * @param andGivenValueList - Array of values the "and" fields should equal in order
	 * @param orObjectQueried - Class of the "or" object to be queried
	 * @param orFieldNameList - Array of names of the "or" Fields to check in order
	 * @param orGivenValueList - Array of values the "or" fields should equal in order
	 * @param aProject - Project that all returned models must belong to
	 * @return A List of Models containing all of the models that match all of the given fields in the given Project
	 * @throws WPISuiteException
	 */
	@SuppressWarnings("rawtypes") //Ignore the warning about the use of type Class
	public List<Model> complexRetrieve(final Class andAnObjectQueried, String[] andFieldNameList, final List<Object> andGivenValueList, 
			final Class orAnObjectQueried, String[] orFieldNameList, final List<Object> orGivenValueList, final Project aProject) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
			{
		List<Model> returnList = new ArrayList<Model>();
		List<Model> bothList = new ArrayList<Model>();
		List<Model> orList = new ArrayList<Model>();
		List<Model> andList = new ArrayList<Model>();
		orList = orRetrieve(orAnObjectQueried, orFieldNameList, orGivenValueList, aProject);
		andList = andRetrieve(andAnObjectQueried, andFieldNameList, andGivenValueList, aProject);
		bothList.addAll(orList);
		bothList.addAll(andList);
		returnList = removeDuplicates(bothList);
		return returnList;
			}

}

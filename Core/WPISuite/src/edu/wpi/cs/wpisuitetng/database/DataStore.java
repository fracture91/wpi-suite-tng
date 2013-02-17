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

public class DataStore implements Data {

	static String WPI_TNG_DB ="WPISuite_TNG_local";
	static String WPI_TNG_DB_TEST = "WpiSuite_TNG_local_Test";
	private static DataStore myself = null;
	static ObjectContainer theDB;
	static ObjectServer server;
	//static ServerConfiguration serverConfig = Db4oClientServer.newServerConfiguration();
	static int PORT = 0;
	static String DB4oUser = "bgaffey";
	static String DB4oPass = "password";
	static String DB4oServer = "localhost";
	
	private static final Logger logger = Logger.getLogger(DataStore.class.getName());


	public static DataStore getDataStore()
	{
		if(myself == null)
		{
			logger.log(Level.FINE, "Opening connection to db4o database...");
			myself = new DataStore();
			// accessLocalServer
			ServerConfiguration config = Db4oClientServer.newServerConfiguration();
			config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
			server = Db4oClientServer.openServer(config, WPI_TNG_DB, PORT);
			server.grantAccess(DB4oUser,DB4oPass);

			theDB = server.openClient();
			logger.log(Level.FINE, "Connected to db4o database!");
		}
		return myself;
	}
	
	public static DataStore getTestDataStore(){
		if(myself == null)
		{
			logger.log(Level.FINE, "Opening connection to db4o database...");
			myself = new DataStore();
			// accessLocalServer
			ServerConfiguration config = Db4oClientServer.newServerConfiguration();
			config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
			server = Db4oClientServer.openServer(config, WPI_TNG_DB_TEST, PORT);
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
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		theDB.store(aModel);
		System.out.println("Stored " + aModel);
		logger.log(Level.FINE, "Saving model [" + aModel + "]");
		theDB.commit();
		return true;
	}
	
	/**
	 * Saves a Model into the database
	 * @param Model to save
	 */
	public <T> boolean save(T aModel, Project aProject){
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));

		//ObjectContainer client = server.openClient();
		((Model) aModel).setProject(aProject);
		theDB.store(aModel);
		System.out.println("Stored " + aModel);
		logger.log(Level.FINE, "Saving model [" + aModel + "]");
		theDB.commit();
		return true;
	}

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
	 * @param aFieldName the field Name of the value in the object you are querying about (this should be 
	 * the suffix of the getter. So for getID you would make this field be "ID".
	 * @param theGivenValue The value of field aFieldName that you want all returned objects to have
	 * @return a List of objects of the given type that have the given field match the given value
	 * @throws WPISuiteException 
	 */
	public List<Model> retrieve(final Class anObjectQueried, String aFieldName, final Object theGivenValue) throws WPISuiteException{
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
	 * 
	 * For this function to work you need to have a getter that takes zero arguments,
	 *  and has the name
	 *  convention of get + the given fieldName (ie getID for the field id from an object). The value can
	 *  be of any type, provided that there is a .equals method for it.  To query
	 *  by something else, like by a user object or defect object, you must create a .equals 
	 *  function for it, that will return true if and only if all the fields of the object 
	 *  have the same values.
	 *  
	 * @param anObjectQueried the class type of the object being queried. You can get this by giving
	 * an object of the desired type and calling .getClass()
	 * @param aFieldName the field Name of the value in the object you are querying about (this should be 
	 * the suffix of the getter. So for getID you would make this field be "ID".
	 * @param theGivenValue The value of field aFieldName that you want all returned objects to have
	 * @param theProject - The Project that we should search in
	 * @return a List of objects of the given type that have the given field match the given value
	 * @throws WPISuiteException 
	 */
	@Override
	public List<Model> retrieve(final Class anObjectQueried, String aFieldName,
			final Object theGivenValue, final Project theProject) throws WPISuiteException {

		if(theProject == null){
			//If no project is given, just search the entire database
			retrieve(anObjectQueried, aFieldName, theGivenValue);
		}

		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
		
		logger.log(Level.FINE, "Attempting Database Retrieve...");
		//ObjectContainer client = server.openClient();
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
						return (anObject.getProject().getName().equals(theProject.getName())) &&
								theGetter.invoke(anObjectQueried.cast(anObject)).equals(theGivenValue);
						//objects that have aFieldName equal to theGivenValue get added to the list
					}
				} catch (IllegalAccessException e) {
					return false;
				} catch (IllegalArgumentException e) {
					return false;
				} catch (InvocationTargetException e) {
					return false;
				}//objects that have aFieldName equal to theGivenValue get added to the list 
			}
		});

		System.out.println(result);
		theDB.commit();
		
		logger.log(Level.FINE, "Database Retrieve Success!");
		return result;

	}

	/**
	 * Retrieves all objects of the given Class. 
	 * @param aSample an object of the class we want to retrieve All of
	 * @return a List of all of the objects of the given class
	 */
	public <T> List<T> retrieveAll(T aSample){
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
		List<T> result = theDB.queryByExample(aSample.getClass());
		System.out.println("retrievedAll: "+result);
		theDB.commit();
		
		logger.log(Level.FINE, "Database RetrieveAll Performed");
		return result;
	}
	
	/**
	 * Retrieves all objects of the given Class. 
	 * @param aSample an object of the class we want to retrieve All of
	 * @param aProject Project to search in for the objects
	 * @return a List of all of the objects of the given class
	 */
	public <T> List<Model> retrieveAll(T aSample, Project aProject){
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
		ArrayList<Model> result = new ArrayList<Model>(); 
		List<T> allResults = theDB.queryByExample(aSample.getClass());
		for(Iterator iterator = result.iterator(); iterator.hasNext();){
			Model theModel = (Model)iterator.next();
			if(theModel.getProject() != null &&
					theModel.getProject().getName().equalsIgnoreCase(aProject.getName())){
				result.add(theModel);
			}
		}
		System.out.println("retrievedAll: "+result);
		theDB.commit();
		logger.log(Level.FINE, "Database RetrieveAll Performed");
		return result;
	}

	/** Deletes the given object and returns the object if successful
	 * @param The object to be deleted
	 * @return The object being deleted
	 */
	public <T> T delete(T aTNG){
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



	public <T> List<T> deleteAll(T aSample){
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
	
	public <T> List<Model> deleteAll(T aSample, Project aProject){
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


	/** For this function to work you need to have a setter that takes the value to change,
	 *  the field to and is named in the convention
	 *  convention of set + the given fieldName (ie setID for the field ID from an object). 
	 *  The value can be of any type, provided that there is a .equals method for it. 
	 *  To query by something else, like by a user object or defect object, you must create a .equals 
	 *  function for it, that will return true if and only if all the fields of the object 
	 *  have the same values.
	 * @param anObjectToBeModified - Class of object to be updated
	 * @param fieldName - Field the object will be identified by
	 * @param uniqueID - value of fieldName that the object will be identified by
	 * @param changeField - Field whose value will be changed
	 * @param changeValue - Value that changeField will be changed to
	 * @throws WPISuiteException 
	 */
	public void update(final Class anObjectToBeModified, String fieldName, Object uniqueID, String changeField, Object changeValue) throws WPISuiteException{
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
	
	public List<Model> notRetrieve(final Class anObjectQueried, String aFieldName, final Object theGivenValue) throws WPISuiteException{
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

	
	public List<Model> orRetrieve(final Class anObjectQueried, String[] aFieldNameList, final List<Object> theGivenValueList) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
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
	
	 public List<Model> andRetrieve(final Class anObjectQueried, String[] aFieldNameList, final List<Object> theGivenValueList) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
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
	 
	 
	 public List<Model> complexRetrieve(final Class andAnObjectQueried, String[] andFieldNameList, final List<Object> andGivenValueList, final Class orAnObjectQueried, String[] orFieldNameList, final List<Object> orGivenValueList) throws WPISuiteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
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


}

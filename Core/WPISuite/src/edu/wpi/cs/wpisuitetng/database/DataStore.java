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
import java.util.List;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.query.Predicate;
import com.db4o.reflect.jdk.JdkReflector;
import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class DataStore implements Data {
	
	static String WPI_TNG_DB ="WPISuite_TNG_local";
	private static DataStore myself = null;
	static ObjectContainer theDB;
	static ObjectServer server;
	//static ServerConfiguration serverConfig = Db4oClientServer.newServerConfiguration();
	static int PORT = 0;
	static String DB4oUser = "bgaffey";
	static String DB4oPass = "password";
	static String DB4oServer = "localhost";
		  
	/**
	 * Singleton for DB4o database
	 * @return Datastore instance to be used
	 */
	public static DataStore getDataStore()
	{
		if(myself == null)
		{
			myself = new DataStore();
			// accessLocalServer
			ServerConfiguration config = Db4oClientServer.newServerConfiguration();
			config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
			server = Db4oClientServer.openServer(config, WPI_TNG_DB, PORT);
			server.grantAccess(DB4oUser,DB4oPass);
			
			theDB = server.openClient();
		}
		return myself;
	}
	
	/**
	 * Saves T into the database
	 * @param Model to save
	 */
	public <T> boolean save(T aTNG){
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
		
			//ObjectContainer client = server.openClient();
			theDB.store(aTNG);
			System.out.println("Stored " + aTNG);
			//client.close();
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
	 */
	public List<Model> retrieve(final Class anObjectQueried, String aFieldName, final Object theGivenValue){
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
		
		//ObjectContainer client = server.openClient();
		Method[] allMethods = anObjectQueried.getMethods();
		Method methodToBeSaved = null;
		for(Method m: allMethods){//Cycles through all of the methods in the class anObjectQueried
			if(m.getName().equalsIgnoreCase("get"+aFieldName)){
				methodToBeSaved = m; //saves the method called "get" + aFieldName
			}
		}
		//TODO: IF Null solve this problem...
		final Method theGetter = methodToBeSaved;
		
		List<Model> result = theDB.query(new Predicate<Model>(){
			public boolean match(Model anObject){
				try {
					return theGetter.invoke(anObjectQueried.cast(anObject)).equals(theGivenValue);//objects that have aFieldName equal to theGivenValue get added to the list 
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;         
				}
			}
		});
	
		System.out.println(result);
		//client.close();
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
		System.out.println("RetrieveAll returned:");
		System.out.println(result);
		return result;
	}
	
	/** Deletes the given object and returns the object if successful
	 * @param The object to be deleted
	 * @return The object being deleted
	 */
	public <T> T delete(T aTNG){
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
		
		//ObjectContainer client = server.openClient();
		ObjectSet<T> result = theDB.queryByExample(aTNG);
	    T found = (T) result.next();
	    theDB.delete(found);
		//client.close();
		//return "Deleted "+aTNG;
		return found;
		
	}
	
	
	/** /** For this function to work you need to have a setter that takes the value to change,
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
	 */
	public void update(final Class anObjectToBeModified, String fieldName, Object uniqueID, String changeField, Object changeValue){
		List<? extends Object> objectsToUpdate = retrieve(anObjectToBeModified, fieldName, uniqueID);
		Object theObject;
		for(int i = 0; i < objectsToUpdate.size(); i++){
			final Class <?> objectClass = objectsToUpdate.get(i).getClass();
			Method[] allMethods = objectClass.getMethods();
			Method methodToBeSaved = null;
			for(Method m: allMethods){
				if(m.getName().equalsIgnoreCase("set"+fieldName)){
					methodToBeSaved = m;
				}
			}
			//TODO: IF Null solve this problem...
			final Method theSetter = methodToBeSaved;
			
			try {
				theObject = (Object) theSetter.invoke(objectsToUpdate.get(i), changeValue);
				save(theObject);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
	}
	
	
	

}

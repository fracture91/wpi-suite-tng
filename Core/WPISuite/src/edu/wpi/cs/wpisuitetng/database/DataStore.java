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
	 *  For this function to work you need to have a getter that takes zero arguments, and has the name
	 *  convention of get + the given fieldName (ie getID for the field id from an object). The value can
	 *  be of any type, provided that there is a .equals method for it. This method exists for the 8 
	 *  Primative java types (Integer, Short, Long, Boolean, Byte, Double, Float). If you want to query
	 *  by something else, like by a user object or defect object, you must create your own .equals 
	 *  function for it, that will return true if and only if all the values are the same (the two 
	 *  objects have equal values). 
	 * @param db The db4O database container you are looking into
	 * @param anObjectQueried the class type of the object being queried. You can get this by giving
	 * an object of the desired type and calling .getClass()
	 * @param aFieldName the field Name of the value in the object you are querying about (this should be 
	 * the suffix of the getter. So for getID you would make this field be "ID".
	 * @param theGivenValue The value that you want all returned objects to have
	 * @return a List of objects of the given type that have the given field match the given value
	 */
	public List<Model> retrieve(final Class anObjectQueried, String aFieldName, final Object theGivenValue){
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
		
		//ObjectContainer client = server.openClient();
		Method[] allMethods = anObjectQueried.getMethods();
		Method methodToBeSaved = null;
		for(Method m: allMethods){
			if(m.getName().equalsIgnoreCase("get"+aFieldName)){
				methodToBeSaved = m;
			}
		}
		//TODO: IF Null solve this problem...
		final Method theGetter = methodToBeSaved;
		
		List<Model> result = theDB.query(new Predicate<Model>(){
			public boolean match(Model anObject){
				try {
					return theGetter.invoke(anObjectQueried.cast(anObject)).equals(theGivenValue);
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
	
	//Code in progress for multiquerying
	public <T> List<T> retrieve(final Class<T> anObjectQueried, String[] aFieldName, 
			final T[] theGivenValue, final String operator) throws IllegalArgumentException {
		final int fieldNameLength = aFieldName.length;
		int theGivenValueLength = theGivenValue.length;
		
		if(fieldNameLength != theGivenValueLength){
			new IllegalArgumentException("The length of the two given arrays does not match");
		}
		
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
		//ObjectContainer client = server.openClient();
		
		Method[] allMethods = anObjectQueried.getMethods();
		Method methodToBeSaved = null;
		Method[] methodsToBeExecuted = new Method[fieldNameLength];
		int fieldNameCount = 0;
		for(String name: aFieldName)
		{
			for(Method m: allMethods){
				if(m.getName().equalsIgnoreCase("get"+name)){
					methodsToBeExecuted[fieldNameCount] = m;
				}
			}
			fieldNameCount++;
		}
		//TODO: IF Null solve this problem...
		final Method[] theGetters = methodsToBeExecuted;
		final String theOperator = operator;
		
		List<Model> result = theDB.query(new Predicate<Model>(){
			public boolean match(Model aDefect){
				try {
					boolean matchSoFar = true;
					if(theOperator.equalsIgnoreCase("and")){
						for(int i = 0; i<fieldNameLength; i++)
						{
							matchSoFar =  matchSoFar && theGetters[i].invoke(aDefect).equals(theGivenValue[i]);
						}
					}
					else if(theOperator.equalsIgnoreCase("or")){ 
						matchSoFar = false;
						for(int i = 0; i<fieldNameLength; i++)
						{
							matchSoFar = matchSoFar || theGetters[i].invoke(aDefect).equals(theGivenValue[i]);
						}
					}
					else{
						for(int i = 0; i<fieldNameLength; i++)
						{
							matchSoFar = matchSoFar && theGetters[i].invoke(aDefect).equals(theGivenValue[i]);
						}
					}
					return matchSoFar;
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
		return (List<T>) result;
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public <T> List<T> retrieveAll(final T item){
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
		
		ObjectContainer client = server.openClient();
		List<T> result = client.query(new Predicate<T>(){
			public boolean match(T anObject){
				try {
					return anObject.getClass().equals(item.getClass());
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
		});
	
		System.out.println(result);
		client.close();
		return result;
	}
	
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
	
	public Object delete(Class objectType, String fieldName, Object uniqueID){
		return (Object) delete(objectType);
	}
	
	
	public void update(Class objectType, String fieldName, Object uniqueID, String changeField, Object changeValue){
		List<? extends Object> objectsToUpdate = retrieve(objectType.getClass(), fieldName, uniqueID);
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
				theObject = (Object) theSetter.invoke(objectsToUpdate.get(i));
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
	
	public User[] getUser(String username)
	{
		User[] ret = new User[1];
		retrieve(new User("","","", 0).getClass(), "username", username).toArray(ret);
		return ret;
		
	}
	
	public Model addUser(String json, Class<? extends Model> type)
	{
		Gson gson = new Gson();
		Model u = gson.fromJson(json, type);
		save(u);
		return u;
	}
	
	public Model addProject(String json)
	{
		Gson gson = new Gson();
		Project p = gson.fromJson(json, Project.class);
		save(p);
		return p;
	}
	
	public Project[] getProject(int idNum)
	{
		Project[] ret = new Project[1];
		return retrieve(new Project("","").getClass(), "idnum", idNum).toArray(ret);
		
	}
	

}

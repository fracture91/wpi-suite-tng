package wpisuite.models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.query.Predicate;
import com.google.gson.Gson;

import placeholderFiles.Defect;
import placeholderFiles.TNG;

public class DataStore {
	
	static String WPI_TNG_DB ="WPISuite_TNG_new";
	private static DataStore myself = null;
	static ObjectContainer theDB;
	static ObjectServer server;
	
	public static DataStore getDataStore()
	{
		if(myself == null)
			myself = new DataStore();
		// accessLocalServer
		server = Db4oClientServer.openServer(Db4oClientServer
				.newServerConfiguration(), WPI_TNG_DB, 8081);
		server.grantAccess("bgaffey", "password");
	//	try {
		/*ObjectContainer client = Db4oClientServer.openClient(Db4oClientServer
		        .newClientConfiguration(), "localhost", 8081, "bgaffey", "password");
		        */
		//ObjectContainer client = server.openClient();
		   
		/*   // Do something with this client, or open more clients
		   client.close();
		} finally {
		   server.close();
		}
		ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), WPI_TNG_DB); 
		*/
		//theDB = client;
		return myself;
	}
	
	public void dbTest(){
		ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded
		        .newConfiguration(), WPI_TNG_DB);
		theDB = db;
		Date theDate = Calendar.getInstance().getTime();
		TNG defect1 = new Defect(1,"Gaffey",theDate, "It's broken");
		try {

			save(db, (Defect)defect1);
			retrieve(db, defect1.getClass(), "ID", 1);
			retrieve(db, defect1.getClass(), "ReportedBy", "Gaffey");
			retrieve(db, defect1.getClass(), "Submited", theDate);
			//delete(db, defect1);
		    // do something with db4o
		} finally {
		    db.close();
		}
	}
	
	public boolean save(ObjectContainer db, TNG aTNG){
		ObjectContainer client = Db4oClientServer.openClient(Db4oClientServer
		        .newClientConfiguration(), "localhost", 8081, "bgaffey", "password");
		client.store(aTNG);
		System.out.println("Stored " + aTNG);
		client.close();
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
	public <T> List<?> retrieve(ObjectContainer db, final Class<?> anObjectQueried, String aFieldName, 
			final T theGivenValue){
		ObjectContainer client = Db4oClientServer.openClient(Db4oClientServer
		        .newClientConfiguration(), "localhost", 8081, "bgaffey", "password");
		Method[] allMethods = anObjectQueried.getMethods();
		Method methodToBeSaved = null;
		for(Method m: allMethods){
			if(m.getName().equalsIgnoreCase("get"+aFieldName)){
				methodToBeSaved = m;
			}
		}
		//TODO: IF Null solve this problem...
		final Method theGetter = methodToBeSaved;
		
		List<TNG> result = client.query(new Predicate<TNG>(){
			public boolean match(TNG aDefect){
				try {
					return theGetter.invoke(aDefect).equals(theGivenValue);
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
		client.close();
		return result;
	}
	
	public <T> List<?> retrieve(ObjectContainer db, final Class<?> anObjectQueried, String[] aFieldName, 
			final T[] theGivenValue, final String operator) throws IllegalArgumentException {
		final int fieldNameLength = aFieldName.length;
		int theGivenValueLength = theGivenValue.length;
		
		if(fieldNameLength != theGivenValueLength){
			new IllegalArgumentException("The length of the two given arrays does not match");
		}
		
		ObjectContainer client = Db4oClientServer.openClient(Db4oClientServer
		        .newClientConfiguration(), "localhost", 8081, "bgaffey", "password");
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
		
		List<TNG> result = client.query(new Predicate<TNG>(){
			public boolean match(TNG aDefect){
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
		client.close();
		return result;
	}
	
	public boolean delete(ObjectContainer db, Defect aDefect){
		ObjectContainer client = Db4oClientServer.openClient(Db4oClientServer
		        .newClientConfiguration(), "localhost", 8081, "bgaffey", "password");
		client.delete(aDefect);
		System.out.println("Deleted "+aDefect);
		return true;
	}
	
	public User[] getUser(String username)
	{
		User[] ret = new User[1];
		return retrieve(theDB,new User("","",0).getClass(), "username", username).toArray(ret);
		
	}
	
	public void addUser(String json)
	{
		Gson gson = new Gson();
		User u = gson.fromJson(json, User.class);
		save(theDB, u);
		//users.add(u);
	}
	
	public void addProject(String json)
	{
		Gson gson = new Gson();
		Project p = gson.fromJson(json, Project.class);
		save(theDB, p);
		//projects.add(p);
	}
	
	public Project[] getProject(int idNum)
	{
		Project[] ret = new Project[1];
		return retrieve(theDB,new Project("",0).getClass(), "idnum", idNum).toArray(ret);
		
	}
	
	public void finalize(){
		server.close();
	}

}

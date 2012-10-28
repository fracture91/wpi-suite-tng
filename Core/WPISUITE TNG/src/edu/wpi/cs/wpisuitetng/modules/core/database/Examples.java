package edu.wpi.cs.wpisuitetng.modules.core.database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import EDU.purdue.cs.bloat.editor.Switch;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

public class Examples {
	public Examples(){}
	
	String WPI_TNG_DB ="WPISuite_TNG";
	public void dbTest(){
		ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded
		        .newConfiguration(), WPI_TNG_DB);
		Date theDate = Calendar.getInstance().getTime();
		TNG defect1 = new Defect(1,"Gaffey",theDate, "It's broken");
		try {
			//save(db, defect1);
//			retrieve(db, new defectIdMatcher(1));
			retrieve(db, defect1.getClass(), "ID", 1);
			retrieve(db, defect1.getClass(), "ReportedBy", "Gaffey");
			//delete(db, defect1);
		    // do something with db4o
		} finally {
		    db.close();
		}
	}
	
	public boolean save(ObjectContainer db, Defect aDefect){
		db.store(aDefect);
		System.out.println("Stored " + aDefect);
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
	 * @param db The db4O database file you are looking into
	 * @param anObjectQueried the class type of the object being queried. You can get this by giving
	 * an object of the desired type and calling .getClass()
	 * @param aFieldName the field Name of the value in the object you are querying about (this should be 
	 * the suffix of the getter. So for getID you would make this field be "ID".
	 * @param theGivenValue The value that you want all returned objects to have
	 * @return a List of objects of the given type that have the given field match the given value
	 */
	public <T> List<TNG> retrieve(ObjectContainer db, final Class<?> anObjectQueried, String aFieldName, 
			final T theGivenValue){
		Method[] allMethods = anObjectQueried.getMethods();
		Method methodToBeSaved = null;
		for(Method m: allMethods){
			if(m.getName().equalsIgnoreCase("get"+aFieldName)){
				methodToBeSaved = m;
			}
		}
		//TODO: IF Null solve this problem...
		final Method theGetter = methodToBeSaved;
		
		List<TNG> result = db.query(new Predicate<TNG>(){
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
		return result;
	}
	
	public boolean delete(ObjectContainer db, Defect aDefect){
		db.delete(aDefect);
		System.out.println("Deleted "+aDefect);
		return true;
	}
}

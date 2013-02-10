package edu.wpi.cs.wpisuitetng.modules.defecttracker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.modules.Model;

/**
 * A mock data implementation for server-side testing. 
 */
public class MockData implements Data {

	private final Set<Object> objects;
	
	/**
	 * Create a MockData instance initially containing the given set of objects
	 * @param objects The set of objects this "database" starts with
	 */
	public MockData(Set<Object> objects) {
		this.objects = objects;
	}

	@Override
	public <T> T delete(T arg0) {
		if(objects.contains(arg0)) {
			objects.remove(arg0);
			return arg0;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> deleteAll(T arg0) {
		List<T> deleted = new ArrayList<T>();
		for(Object obj : objects) {
			if(arg0.getClass().isInstance(obj)) {
				deleted.add((T) obj);
			}
		}
		// can't remove in the loop, otherwise you get an exception
		objects.removeAll(deleted); 
		return deleted;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Model> retrieve(Class type, String fieldName, Object value) {
		List<Model> rv = new ArrayList<Model>();
		for(Object obj : objects) {
			if(!type.isInstance(obj)) {
				continue;
			}
			Method[] methods = obj.getClass().getMethods();
			for(Method method : methods) {
				if(method.getName().equalsIgnoreCase("get" + fieldName)) {
					try {
						if(method.invoke(obj).equals(value)) {
							rv.add((Model) obj);
						}
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
		return rv;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> retrieveAll(T arg0) {
		List<T> all = new ArrayList<T>();
		for(Object obj : objects) {
			if(arg0.getClass().isInstance(obj)) {
				all.add((T) obj);
			}
		}
		return all;
	}

	@Override
	public <T> boolean save(T arg0) {
		objects.add(arg0);
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void update(Class arg0, String arg1, Object arg2, String arg3,
			Object arg4) {
		// TODO Auto-generated method stub

	}

}

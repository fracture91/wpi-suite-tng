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

	private final Set<Model> models;
	
	public MockData(Set<Model> models) {
		this.models = models;
	}

	@Override
	public <T> T delete(T arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> deleteAll(T arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Model> retrieve(Class type, String fieldName, Object value) {
		List<Model> rv = new ArrayList<Model>();
		for(Model model : models) {
			if(!type.isInstance(model)) {
				continue;
			}
			Method[] methods = model.getClass().getMethods();
			for(Method method : methods) {
				if(method.getName().equalsIgnoreCase("get" + fieldName)) {
					try {
						if(method.invoke(model).equals(value)) {
							rv.add(model);
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

	@Override
	public <T> List<T> retrieveAll(T arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> boolean save(T arg0) {
		models.add((Model)arg0);
		return true;
	}

	@Override
	public void update(Class arg0, String arg1, Object arg2, String arg3,
			Object arg4) {
		// TODO Auto-generated method stub

	}

}

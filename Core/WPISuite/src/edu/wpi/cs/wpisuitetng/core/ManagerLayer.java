package edu.wpi.cs.wpisuitetng.core;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;

import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.ProjectManager;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;




/**
 * This singleton class responds to API requests directed at 
 * models by contacting their respective entity managers
 * 
 * eagerly initialized, the instance of this class is thread safe, provided all methods are thread safe
 * 
 * REMEMBER THREAD SAFETY
 * ALL METHODS MUST BE THREAD SAFE
 */
public class ManagerLayer {
	
	private static final ManagerLayer layer = new ManagerLayer();
	private DataStore data;
	private Gson gson;
	private Map<String, EntityManager> map;
	
	/**
	 * initializes the database
	 * initializes the JSON serializer
	 */
	@SuppressWarnings("rawtypes")
	private ManagerLayer()
	{
		data = DataStore.getDataStore();
		gson = new Gson();
		map = new HashMap<String, EntityManager>();
		
		
		//TODO pull these mappings from some config file and reflect them
		map.put("coreproject", new ProjectManager());
		map.put("coreuser", new UserManager());
		
	}
	
	/**
	 * initializes the database
	 * initializes the JSON serializer
	 * 
	 * Accepts a map that will be used instead of the automatically generated map
	 * 
	 * THIS IS FOR TESTING PURPOSES ONLY
	 */
	@SuppressWarnings("rawtypes")
	private ManagerLayer(Map<String, EntityManager> map)
	{
		gson = new Gson();
		this.map = map;		
	}
	
	/**
	 * Returns the single ManagerLayer instance
	 * 
	 * @return ManagerLayer
	 */
	public static ManagerLayer getInstance()
	{
		return layer;
	}
	
	/**
	 * Returns the single ManagerLayer instance
	 * This is a test managerlayer with dummy EntityManagers
	 * This call is not a true singleton call, and will return a new managerlayer everytime it is accessed
	 * @return ManagerLayer
	 */
	protected static ManagerLayer getTestInstance(@SuppressWarnings("rawtypes") Map<String, EntityManager> map)
	{
		return new ManagerLayer(map);
	}
	
	/**read()
	 * 
	 * @param args - a string array of the parameters, where args[length-1] == null
	 * @return a JSON String representing the requested data
	 */
	public synchronized String read(String[] args)
	{		
		Model[] m = map.get(args[0]+args[1]).getEntity(args[2]);
		
        return (m == null) ? "null" : gson.toJson(m, m.getClass());
	}
	
	/**create()
	 * 
	 * @param args - a string array of the parameters
	 * @param content - the content of the create request
	 * @return a JSON String of the newly created data if successful, null otherwise
	 */
	public synchronized String create(String[] args, String content)
	{
		Model m;
		
		m = (Model) map.get(args[0]+args[1]).makeEntity(content);
        
        return gson.toJson(m, m.getClass());
	}
	
	/**update
	 * 
	 * @param args - A string array of the parameters
	 * @param content - a JSON String of the content to update
	 * @return a JSON String of the updated element
	 */
	public synchronized String update(String[] args, String content)
	{
		String result = delete(args);
		
		if(result == "null")
		{
			result = create(args,content);
		}
		
		return result;
	
	}
	
	/**delete
	 * 
	 * @param args - A String array of the parameters 
	 * @return String "null" if the delete was successful, a message otherwise
	 */
	public synchronized String delete(String[] args)
	{
				
		
		
		boolean status = map.get(args[0]+args[1]).deleteEntity(args[2]);
		
        return (status) ? "null" : "problem";
        
	}
	
}

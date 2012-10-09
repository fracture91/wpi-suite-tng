package edu.wpi.cs.wpisuitetng.core;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.Model;
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
	private Map<String, Class<? extends Model>> map;
	
	/**
	 * initializes the database
	 * initializes the JSON serializer
	 */
	private ManagerLayer()
	{
		data = DataStore.getDataStore();
		gson = new Gson();
		map = new HashMap<String, Class<? extends Model>>();
		
		
		//TODO pull these mappings from some config file and reflect them
		map.put("project", Project.class);
		map.put("user", User.class);
		
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
	
	/**read()
	 * 
	 * @param args - a string array of the parameters, where args[length-1] == null
	 * @return a JSON String representing the requested data
	 */
	public synchronized String read(String[] args)
	{		
		//TODO - Reevaluate synchronization on this method, only need to protect writes
		//especially if DB40 already handles concurrency
		
		//Model[] m = data.retrieve(map.get(args[1]), args[2]);
		Model[] m = new Model[1];
		//TODO: Move the toArray inside the retrieve method
		data.retrieve(map.get(args[1]), "username", args[2]).toArray(m);
		
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
        
		if(args[0].equalsIgnoreCase("project")){
			m = data.addProject(content);
		}
		else{
			m = data.addUser(content, map.get(args[1]));
		}
        
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
		
		if(result == null)
		{
			result = create(args,content);
		}
		
		return result;
	
	}
	
	/**delete
	 * 
	 * @param args - A String array of the parameters 
	 * @return null if the delete was successful, a message otherwise
	 */
	public synchronized String delete(String[] args)
	{
		//String message = data.remove(map.get(args[1]), args[2]);
		User toBeDeleted = (User) data.retrieve(map.get(args[1]), "username", args[2]).get(0);		
		String message = data.delete(toBeDeleted);
        return message;
        
	}
	
}

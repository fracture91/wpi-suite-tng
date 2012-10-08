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
	private MockDataStore data;
	private Gson gson;
	private Map<String, Class<? extends Model>> map;
	
	/**
	 * initializes the database
	 * initializes the JSON serializer
	 */
	private ManagerLayer()
	{
		data = MockDataStore.getMockDataStore();
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
		
		Model[] m = data.retrieve(map.get(args[0]), args[1]);
		
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
        
		m = data.save(content, map.get(args[0]));
        
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
		String message = data.remove(map.get(args[0]), args[1]);
		
        return message;
        
	}
	
}

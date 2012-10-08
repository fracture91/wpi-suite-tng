package wpisuite.core;

import com.google.gson.Gson;

import wpisuite.models.MockDataStore;
import wpisuite.models.Model;


/**
 * This singleton class responds to API requests directed at 
 * models by contacting their respective entity managers
 * 
 * eagerly initialized
 * 
 * @author Mike Della Donna
 * REMEMBER THREAD SAFETY
 * ALL METHODS MUST BE THREAD SAFE
 */
public class ManagerLayer {
	
	private static final ManagerLayer layer = new ManagerLayer();
	private MockDataStore data;
	private Gson gson;
	
	/**
	 * initializes the database
	 * initializes the JSON serializer
	 */
	private ManagerLayer()
	{
		data = MockDataStore.getMockDataStore();
		gson = new Gson();
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
	 * 
	 * @param args - a string array of the parameters, where args[length-1] == null
	 * @return a JSON String representing the requested data
	 */
	public synchronized String read(String[] args)
	{
		new wpisuite.models.core.Project(null, 4);
		
		Model[] m = data.getModel(args);
		
        return gson.toJson(m, m.getClass());
	}
	
	/**
	 * 
	 * @param args - a string array of the parameters
	 * @param content - the content of the create request
	 * @return a JSON String of the newly created data if successful, null otherwise
	 */
	public synchronized String create(String[] args, String content)
	{
		Model m;
        if(args[1].equalsIgnoreCase("user"))
        {
    		m = data.addUser(content);

        }
        else
        {
    		m = data.addProject(content);

        }
        
        return gson.toJson(m, m.getClass());
	}
	
	/**
	 * 
	 * @param args - A string array of the parameters
	 * @param content - a JSON String of the content to update
	 * @return a JSON String of the updated element
	 */
	public synchronized String update(String[] args, String content)
	{
		
		return null;
	
	}
	
	/**
	 * 
	 * @param args - A String array of the parameters 
	 * @return null if the delete was successful, a message otherwise
	 */
	public synchronized String delete(String[] args)
	{
		return null;
	}
	
}

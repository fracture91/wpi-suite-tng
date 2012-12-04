/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mpdelladonna
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.exceptions.ForbiddenException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;

import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.ProjectManager;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.DefectManager;
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
	private Data data;
	private Gson gson;
	@SuppressWarnings("rawtypes")
	private Map<String, EntityManager> map;
	private SessionManager sessions;
	
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
		sessions = new SessionManager();
		
		//TODO pull these mappings from some config file and reflect them
		map.put("coreproject", new ProjectManager(data));
		map.put("coreuser", new UserManager(data));
		map.put("defectdefect", new DefectManager());
		
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
	
	/**
	 * Exposes the SessionManager for this ManagerLayer.
	 * @return	sessions
	 */
	public synchronized SessionManager getSessions()
	{
		return sessions;
	}
	
	/**
	 * Exposes the Users in the database for direct access.
	 * @return	The UserManager instance
	 */
	public User[] getUsers(String username)
	{
		UserManager u = (UserManager)map.get("coreuser");
		return u.getEntity(username);
	}
	
	/**
	 * Exposes the Users in the database for direct access.
	 * @return	The UserManager instance
	 */
	public UserManager getUsers()
	{
		UserManager u = (UserManager)map.get("coreuser");
		return u;
	}
	
	/**read()
	 * 
	 * String args[] - {module,model,identifier}
	 * 
	 * @param args - a string array of the parameters, where args[length-1] == null
	 * @return a JSON String representing the requested data
	 */
	public synchronized String read(String[] args,Cookie[] cook) throws WPISuiteException
	{		
		Session s = null;
		if(cook != null)
		{
			for(Cookie c : cook)
			{
				if(c.getName().startsWith("WPISUITE-"))
					s = sessions.getSession(c.getValue());
					
			}
		}
		else
		{
			throw new ForbiddenException();
		}
		Model[] m = map.get(args[0]+args[1]).getEntity(s,args[2]);
		
        return (m == null) ? "null" : gson.toJson(m, m.getClass());
	}
	
	/**create()
	 * 
	 * 	 * String args[] - {module,model,identifier}
	 * @param args - a string array of the parameters
	 * @param content - the content of the create request
	 * @return a JSON String of the newly created data if successful, null otherwise
	 */
	public synchronized String create(String[] args, String content,Cookie[] cook) throws WPISuiteException
	{
		Session s = null;
		if(cook != null)
		{
			for(Cookie c : cook)
			{
				if(c.getName().startsWith("WPISUITE-"))
					s = sessions.getSession(c.getValue());
			}
		}
		Model m;
		m = (Model) map.get(args[0]+args[1]).makeEntity(s,content);
        
        return gson.toJson(m, m.getClass());
	}
	
	/**update
	 * 
	 * 
	 * 	 * String args[] - {module,model,identifier}
	 * @param args - A string array of the parameters
	 * @param content - a JSON String of the content to update
	 * @return a JSON String of the updated element
	 */
	public synchronized String update(String[] args, String content,Cookie[] cook) throws WPISuiteException
	{
		String result = delete(args,cook);
		
		if(result == "null")
		{
			result = create(args,content,cook);
		}
		
		return result;
	
	}
	
	/**delete
	 * 	 * String args[] - {module,model,identifier}
	 * @param args - A String array of the parameters 
	 * @return String "null" if the delete was successful, a message otherwise
	 */
	public synchronized String delete(String[] args,Cookie[] cook) throws WPISuiteException
	{
		Session s = null;
		for(Cookie c : cook)
		{
			if(c.getName().startsWith("WPISUITE-"))
				s = sessions.getSession(c.getValue());
				
		}		
		
		
		boolean status = map.get(args[0]+args[1]).deleteEntity(s,args[2]);
		
        return (status) ? "null" : "problem";
        
	}
	
}

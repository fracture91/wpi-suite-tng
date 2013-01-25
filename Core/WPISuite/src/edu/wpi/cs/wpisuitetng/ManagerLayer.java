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
import edu.wpi.cs.wpisuitetng.exceptions.AuthenticationException;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;

import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.ProjectManager;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers.DefectManager;
import edu.wpi.cs.wpisuitetng.modules.postboard.model.PostBoardEntityManager;

/**
 * This singleton class responds to API requests directed at 
 * models by contacting their respective entity managers
 * 
 * eagerly initialized, the instance of this class is thread safe, provided all methods are thread safe
 * 
 * Remember Thread Safety
 * All methods must be thread safe
 */
public class ManagerLayer {
	
	private static final ManagerLayer layer = new ManagerLayer();
	private Data data;
	private Gson gson;
	@SuppressWarnings("rawtypes")
	private Map<String, EntityManager> map;
	private SessionManager sessions;
	public Cookie superCookie;
	
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
		map.put("defecttrackerdefect", new DefectManager(data));
		map.put("postboardpostboardmessage", new PostBoardEntityManager(data));
		Session s = null;
		try {
			s = sessions.createSession((User)map.get("coreuser").makeEntity(null, new User("Admin","admin", "password",0).toJSON()));
		} catch (BadRequestException e) {
			e.printStackTrace();
		} catch (ConflictException e) {
			try {
				s = sessions.createSession((User)map.get("coreuser").getEntity(null, "admin")[0]);
			} catch (NotFoundException e1) {
				e1.printStackTrace();
			} catch (WPISuiteException e1) {
				e1.printStackTrace();
			}
		} catch (WPISuiteException e) {
			e.printStackTrace();
		}
		
		superCookie = s.toCookie();
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
	private ManagerLayer(Map<String, EntityManager> map, SessionManager ses)
	{
		gson = new Gson();
		this.map = map;		
		this.sessions = ses;
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
	protected static ManagerLayer getTestInstance(@SuppressWarnings("rawtypes") Map<String, EntityManager> map, SessionManager ses)
	{
		return new ManagerLayer(map, ses);
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
	 * @throws WPISuiteException 
	 */
	public User[] getUsers(String username) throws WPISuiteException
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
	 * Reads the WPISuite cookie and returns the session associated with it in JSON form
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
			throw new AuthenticationException();
		}
		Model[] m = map.get(args[0]+args[1]).getEntity(s,args[2]);
		
        return (m == null) ? "null" : gson.toJson(m, m.getClass());
	}
	
	/**create()
	 * Creates a JSON representation of the model created from the content passed in
	 * in the arguments.
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
		else
		{
			throw new AuthenticationException();
		}
		Model m;
		m = (Model) map.get(args[0]+args[1]).makeEntity(s,content);
        
        return gson.toJson(m, m.getClass());
	}
	
	/**update
	 * 
	 * Updates the model with the content stored in the content argument
	 * 	 * String args[] - {module,model,identifier}
	 * @param args - A string array of the parameters
	 * @param content - a JSON String of the content to update
	 * @return a JSON String of the updated element
	 */
	public synchronized String update(String[] args, String content,Cookie[] cook) throws WPISuiteException
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
			throw new AuthenticationException();
		}
		Model m;
		m = (Model) map.get(args[0]+args[1]).update(s, content);
		
		return gson.toJson(m, m.getClass());
	
	}
	
	/**delete
	 * 	 * String args[] - {module,model,identifier}
	 * Deletes a model identified by the cookie and content of the model.  
	 * @param args - A String array of the parameters 
	 * @return String "null" if the delete was successful, a message otherwise
	 */
	public synchronized String delete(String[] args,Cookie[] cook) throws WPISuiteException
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
			throw new AuthenticationException();
		}
		
		
		boolean status = map.get(args[0]+args[1]).deleteEntity(s,args[2]);
		
        return (status) ? "success" : "failure";
        
	}
	
}

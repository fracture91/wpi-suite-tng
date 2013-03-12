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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.exceptions.AuthenticationException;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;

import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.ProjectManager;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers.CommentManager;
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
	@SuppressWarnings("rawtypes")
	private Map<String, EntityManager> map;
	private SessionManager sessions;
	public Cookie superCookie;
	
	private static final Logger logger = Logger.getLogger(ManagerLayer.class.getName());
	
	/**
	 * initializes the database
	 * initializes the JSON serializer
	 */
	@SuppressWarnings("rawtypes")
	private ManagerLayer()
	{
		data = DataStore.getDataStore();
		map = new HashMap<String, EntityManager>();
		sessions = new SessionManager();
		
		//TODO pull these mappings from some config file and reflect them
		map.put("coreproject", new ProjectManager(data));
		map.put("coreuser", new UserManager(data));
		map.put("defecttrackerdefect", new DefectManager(data));
		map.put("defecttrackercomment", new CommentManager(data));
		map.put("postboardpostboardmessage", new PostBoardEntityManager(data));

		//add just your module to this list
		String[] fullModuleList = {"core","defecttracker","postboard"};
		((ProjectManager)map.get("coreproject")).setAllModules(fullModuleList);
		String ssid = null;
		
		((UserManager)map.get("coreuser")).createAdmin();
		
		try {
			ssid = sessions.createSession((User)map.get("coreuser").getEntity(null, "admin")[0]);
		} catch (NotFoundException e1) {
			e1.printStackTrace();
		} catch (WPISuiteException e1) {
			e1.printStackTrace();
		}
		
		superCookie = sessions.getSession(ssid).toCookie();
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
		logger.log(Level.FINE, "ManagerLayer Instance Requested");
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
	
	/**
	 * Exposes the Projects in the database for direct access.
	 * @return	the ProjectManager instance
	 */
	public ProjectManager getProjects()
	{
		ProjectManager p = (ProjectManager)map.get("coreproject");
		return p;
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
		Session s = getSessionFromCookies(cook);
		   
		Model[] m;
		if(args[2] == null || args[2].equalsIgnoreCase(""))
		{
			m = map.get(args[0]+args[1]).getAll(s);
		}
		else
		{
			m = map.get(args[0]+args[1]).getEntity(s,args[2]);
		}
		
        //return (m == null) ? "null" : gson.toJson(m, m.getClass());
		
		String response = "null";
		
		if(m != null)
		{
			response = "[";
			for(Model n : m)
			{
				response = response.concat(n.toJSON()+",");
			}
			if(m.length > 0)
			{
				response = response.substring(0, response.length() - 1); // remove trailing comma
			}
			response = response.concat("]");
		}
		
		return response;
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
		Session s = getSessionFromCookies(cook);

		Model m;
		m = (Model) map.get(args[0]+args[1]).makeEntity(s,content);
        
        return m.toJSON();
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
		Session s = getSessionFromCookies(cook);

		Model m;
		m = (Model) map.get(args[0]+args[1]).update(s, content);
		
		return m.toJSON();
	
	}
	
	/**delete
	 * 	 * String args[] - {module,model,identifier}
	 * Deletes a model identified by the cookie and content of the model.  
	 * @param args - A String array of the parameters 
	 * @return String "null" if the delete was successful, a message otherwise
	 */
	public synchronized String delete(String[] args,Cookie[] cook) throws WPISuiteException
	{
		Session s = getSessionFromCookies(cook);
		
		boolean status = map.get(args[0]+args[1]).deleteEntity(s,args[2]);
		
        return (status) ? "success" : "failure";
        
	}
	
	/**Advanced Get
	 * 
	 * forwards advanced get requests to the correct entity manager
	 * @param args - A String array of the parameters
	 * @param cook - The cookie forward
	 * @return String - the returned value from the advancedGet call
	 * 
	 */
	public String advancedGet(String[] args, Cookie[] cook) throws WPISuiteException
	{
		Session s = getSessionFromCookies(cook);
		
        return map.get(args[0]+args[1]).advancedGet(s, args);
	}
	
	/**Advanced Put
	 * 
	 * **********************
	 * A note about advanced put.  the content body should not contain any line breaks.
	 * only the first line will be passed through to the function.
	 * **********************
	 * 
	 * does the same thing as advanced GET except that it also forwards
	 * as well as the path arguments
	 * @param args - the path arguments of the request
	 * @param content - the content body of the request
	 * @param cook - the cookie sent along with the request
	 * @return String - a string response to send back
	 */
	public String advancedPut(String[] args, String content, Cookie[] cook) throws WPISuiteException
	{
		Session s = getSessionFromCookies(cook);
		
        return map.get(args[0]+args[1]).advancedPut(s,args,content);
	}

	/**
	 * ADvanced Post
	 * 
	 * **********************
	 * A note about advanced post.  the content body should not contain any line breaks.
	 * only the first line will be passed through to the function.
	 * **********************
	 * 
	 * @param path
	 * @param readLine
	 * @param cookies
	 * @return
	 * @throws WPISuiteException
	 */
	public String advancedPost(String[] args, String content, Cookie[] cook) throws WPISuiteException
	{
		Session s = getSessionFromCookies(cook);
		
        return map.get(args[0]+args[1]).advancedPost(s,args[2],content);
	}
	
	private Session getSessionFromCookies(Cookie[] cook) throws AuthenticationException, UnauthorizedException
	{
		Session s = null;
		if(cook != null)
		{
			for(Cookie c : cook)
			{
				if(c.getName().startsWith("WPISUITE-"))
					s = sessions.getSession(c.getValue());	
			}
			
			if(s == null)
			{
				throw new UnauthorizedException();
			}
		}
		else
		{
			throw new AuthenticationException("Could not find WPISuite cookie. Please Login to recieve one.");
		}
		
		return s;	
	}
	
}

/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    twack
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Manages Sessions for the ManagerLayer. Wraps a Map of Sessions and provides
 * 	interaction and manipulation functionality
 * @author twack
 *
 */
public class SessionManager {
	
	private Map<String, Session> sessions; // key: cookie value, value: Session
	
	/**
	 * The default constructor. 
	 */
	public SessionManager()
	{
		sessions = new HashMap<String, Session>();
	}
	
	/**
	 * Given a user, determines if this user has an active session. 
	 * @param u	
	 * @return	True if the map contains a Session for this user, False otherwise.
	 */
	public boolean sessionExists(String sessionToken)
	{
		return sessions.containsKey(sessionToken);
	}
	
	/**
	 * wipes the sessions store.
	 */
	public void clearSessions()
	{
		sessions = new HashMap<String, Session>();
	}
	
	/**
	 * Retrieves the Session for the user with the given name.
	 * 
	 * @param sessionToken	the tokenize cookie given from the client
	 * @return	The session matching the token.
	 */
	public Session getSession(String sessionToken)
	{
		
		return sessions.get(sessionToken);
		//TODO: determine how to handle 'not found' case
	}
	
	/**
	 * Removes the session with the given username
	 * @param sessionToken	
	 */
	public void removeSession(String sessionToken)
	{
		sessions.remove(sessionToken); 
	}
	
	/**
	 * Returns a new Session for the given User. If a Session already exists for this user,
	 * 	then renew the Session with a new timestamp.
	 * @param username
	 * @return	the new Session for the user.
	 */
	public Session createSession(User user)
	{
		// ignore the possibility of duplicate sessions per-user.
		
		// add session
		Session ses = new Session(user);
		sessions.put(ses.toCookie().getValue(), ses);
		
		return ses;
	}
	
	/**
	 * @return	Retrieves the number of sessions currently in the Manager
	 */
	public int sessionCount()
	{
		return this.sessions.size();
	}
	
	/**
	 * Renews the Session for a given sessionToken.
	 * 	Parses the username from the token, then creates
	 * 		a new session for the given user.
	 * @param sessionToken
	 * @return	the new Session
	 * @throws WPISuiteException 
	 */
	public Session renewSession(String sessionToken) throws WPISuiteException
	{
		// remove the old session
		this.removeSession(sessionToken);
		
		// parse the username from the sessionToken
		Gson gson = new Gson();
		Session old = gson.fromJson(sessionToken, Session.class);
		String sessionUsername = old.getUsername();
		
		// retrieve the User
		ManagerLayer manager = ManagerLayer.getInstance();
		User sessionUser = manager.getUsers().getEntity(sessionUsername)[0]; // TODO: this looks ugly...
		
		return createSession(sessionUser);
	}

}
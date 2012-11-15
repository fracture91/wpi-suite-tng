package edu.wpi.cs.wpisuitetng;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.filters.ExpiresFilter;

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
		// TODO: find a practical way to handle duplicate sessions per-user
		
		// add session
		Session ses = new Session(user);
		sessions.put(ses.toCookie().getValue(), ses);
		
		return ses;
	}
	
	/*
	public Session renewSession(String sessionToken)
	{
		// parse username from sessionToken
		// retrieve user with given user name
		// return createSession(user);
	} */

}
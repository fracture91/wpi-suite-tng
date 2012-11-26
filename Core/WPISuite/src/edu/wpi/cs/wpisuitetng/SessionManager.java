package edu.wpi.cs.wpisuitetng;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Manages Sessions for the ManagerLayer. Wraps a Map of Sessions and provides
 * 	interaction and manipulation functionality
 * @author twack
 *
 */
public class SessionManager {
	
	private Map<User, Session> sessions;
	
	/**
	 * The default constructor. 
	 */
	public SessionManager()
	{
		sessions = new HashMap<User, Session>();
	}
	
	/**
	 * Given a user, determines if this user has an active session. 
	 * @param u	
	 * @return	True if the map contains a Session for this user, False otherwise.
	 */
	public boolean sessionExists(User u)
	{
		return sessions.containsKey(u.getUsername());
	}
	
	/**
	 * Retrieves the Session for the user with the given name.
	 * 
	 * 
	 * @param username
	 * @return
	 */
	public Session getSession(User username)
	{
		return sessions.get(username);
		//TODO: determine how to handle 'not found' case
	}
	
	/**
	 * Removes the session with the given username
	 * @param username	the username of the Session to be removed.
	 */
	public void removeSession(User username)
	{
		sessions.remove(username); 
	}
	
	/**
	 * Returns a new Session for the given User. If a Session already exists for this user,
	 * 	then renew the Session with a new timestamp.
	 * @param username
	 * @return	the new Session for the user.
	 */
	public Session createOrRenewSession(User user)
	{
		// clear the Session for this user, if it exists.
		if(sessionExists(user))
		{
			sessions.remove(user.getUsername());
		}
		
		// add session
		Session ses = new Session(user);
		sessions.put(user, ses);
		
		return ses;
	}
}
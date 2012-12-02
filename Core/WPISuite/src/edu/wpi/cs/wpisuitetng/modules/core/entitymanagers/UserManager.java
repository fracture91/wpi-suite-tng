package edu.wpi.cs.wpisuitetng.modules.core.entitymanagers;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class UserManager implements EntityManager<User> {

	Class<User> user = User.class;
	Gson gson;
	
	public UserManager()
	{
		gson = new Gson();
	}
	
	@Override
	public User makeEntity(Session s, String content) {
		
		User p;
		
		p = gson.fromJson(content, user);
		
		if(getEntity(s,p.getUsername())[0] == null)
		{
			save(s,p);
		}
		
		return p;
	}

	@Override
	public User[] getEntity(Session s,String id) 
	{
		User[] m = new User[1];
		if(id.equalsIgnoreCase(""))
		{
			return getAll(s);
		}
		else
		{
			return DataStore.getDataStore().retrieve(user, "username", id).toArray(m);
		}
	}
	
	/**
	 * returns a user without requiring a session, 
	 * specifically for the scenario where a session needs to be created.
	 * only ever returns one user, "" is not a valid argument;
	 * 
	 * @param id - the id of the user, in this case it's the username
	 * @return a list of matching users
	 */
	public User[] getEntity(String id) 
	{
		User[] m = new User[1];
		if(id.equalsIgnoreCase(""))
		{
			return m;
		}
		else
		{
			return DataStore.getDataStore().retrieve(user, "username", id).toArray(m);
		}
	}

	@Override
	public User[] getAll(Session s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Session s,User model) {
		DataStore.getDataStore().save(model);
		
	}

	@Override
	public boolean deleteEntity(Session s1 ,String id) {
		
		DataStore data = DataStore.getDataStore();
		
		String s = data.delete(data.retrieve(user, "username", id).get(0));
		
		return (s.startsWith("Deleted")) ? true : false;
		
	}

	@Override
	public void deleteAll(Session s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}

}

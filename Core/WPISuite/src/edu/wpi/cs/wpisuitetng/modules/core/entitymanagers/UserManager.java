package edu.wpi.cs.wpisuitetng.modules.core.entitymanagers;

import java.util.ArrayList;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.core.DataStore;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class UserManager implements EntityManager<User> {

	Class<User> user = User.class;
	Gson gson;
	
	public UserManager()
	{
		gson = new Gson();
	}
	
	@Override
	public User makeEntity(String content) {
		
		User p;
		
		p = gson.fromJson(content, user);
		save(p);
		
		return p;
	}

	@Override
	public User[] getEntity(String id) 
	{
		Model[] m = new Model[1];
		if(id.equalsIgnoreCase(""))
		{
			return getAll();
		}
		else
		{
			return (User[]) DataStore.getDataStore().retrieve(user, "username", id).toArray(m);
		}
	}

	@Override
	public User[] getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(User model) {
		DataStore.getDataStore().save(model);
		
	}

	@Override
	public void deleteEntity(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}

}

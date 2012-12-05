package edu.wpi.cs.wpisuitetng.mockobjects;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class MockUserManager extends UserManager {

	@Override
	public boolean deleteEntity(Session s1, String id) {
		if(id.equalsIgnoreCase("fake"))
			return true;
		else
			return false;
	}

	User fake;
	
	public MockUserManager(Data data, User fake) {
		super(data);
		this.fake = fake;
	}

	@Override
	public User[] getEntity(String id)
	{
		User[] u = new User[1];
		u[0] = fake;

		
		return u;
	}
	
	@Override
	public User[] getEntity(Session s, String id)
	{
		User[] u = new User[1];
		u[0] = fake;
		System.out.println("MockUserManager id: " + id);
		if(id.equalsIgnoreCase(""))
		{
			u = new User[2];
			u[0] = fake;
			u[1] = fake;
		}
		
		if(!(id.equalsIgnoreCase(fake.getUsername()) || id.equalsIgnoreCase("")))
			return null;
		System.out.println("MockUserManager retval: " + u[0]);
		return u;
	}
	
	
}

package edu.wpi.cs.wpisuitetng.mockobjects;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class MockUserManager extends UserManager {

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
}

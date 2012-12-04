package edu.wpi.cs.wpisuitetng.core.entitymanagers;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class MockUserManager extends UserManager {

	public MockUserManager(Data data) {
		super(data);
	}

	@Override
	public User[] getEntity(String id)
	{
		User[] u = new User[1];
		
		u[0] = new User("fake",id,id, 0);
		return u;
	}
}

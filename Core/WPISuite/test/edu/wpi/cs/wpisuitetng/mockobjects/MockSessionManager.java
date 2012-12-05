package edu.wpi.cs.wpisuitetng.mockobjects;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.SessionManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class MockSessionManager extends SessionManager {

	Session ses;
	
	public MockSessionManager(User u)
	{
		super();
		ses = createSession(u);
	}
	
	public Session getTestSession()
	{
		return ses;
	}

}

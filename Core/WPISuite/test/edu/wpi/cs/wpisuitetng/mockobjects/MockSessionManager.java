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

package edu.wpi.cs.wpisuitetng.mockobjects;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.SessionManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class MockSessionManager extends SessionManager {

	Session ses;
	
	public MockSessionManager(User u)
	{
		super();
		String ssid = createSession(u);
		ses = getSession(ssid);
	}
	
	public Session getTestSession()
	{
		return ses;
	}

}

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

import java.util.Date;
import javax.servlet.http.Cookie;

import com.google.gson.*;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Session class holds data for a user session cookie.
 * 
 * @author twack
 *
 */
public class Session {
	private User user;
	private Date loginTime;
	
	/**
	 * Default constructor. Initializes the loginTime field to the time of construction.
	 * @param user	the username field for the user.
	 */
	public Session(User user)
	{
		this.user = user;
		this.loginTime = new Date();
	}
	
	public String getUsername()
	{
		return user.getUsername();
	}
	
	public Date getLoginTime()
	{
		return loginTime;
	}
	
	@Override
	public String toString()
	{
		Gson gson = new Gson();
		
		return gson.toJson(this, Session.class);
	}
	
	/**
	 * Converts this Session into a Cookie object.
	 *	Cookie Format:
	 *			Header 	- 	WPISUITE-{username}
	 *			Body	-	JSON representation of this Session @see {@link Session#toString()}
	 * @return	a Cookie object representing this Session
	 */
	public Cookie toCookie()
	{
		String header = "WPISUITE-" + getUsername();
		return new Cookie(header, this.toString());
	}
}

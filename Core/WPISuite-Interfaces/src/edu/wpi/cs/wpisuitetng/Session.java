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
import java.util.Random;

import javax.servlet.http.Cookie;

import com.google.gson.*;
import com.google.gson.annotations.Expose;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Session class holds data for a user session cookie.
 * 
 * @author twack
 *
 */
public class Session {
	@Expose private User user;
	@Expose private Date loginTime;
	@Expose public String ssid;
	@Expose private Project project;
	
	/**
	 * Default constructor. Initializes the loginTime field to the time of construction.
	 * @param user	the username field for the user.
	 */
	public Session(User user)
	{
		this.user = user;
		this.loginTime = new Date();
		this.project = null;
		this.ssid = this.generateSessionId();
	}
	
	public Session(User user, Project p)
	{
		this.user = user;
		this.loginTime = new Date();
		this.project = p;
		this.ssid = this.generateSessionId();
	}
	
	public String getUsername()
	{
		return user.getUsername();
	}
	
	public User getUser()
	{
		return user;
	}
	
	public Date getLoginTime()
	{
		return loginTime;
	}
	
	public String getSessionId()
	{
		return this.ssid;
	}
	
	public Project getProject()
	{
		return this.project;
	}
	
	@Override
	public String toString()
	{
		String json;
		
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		json = gson.toJson(this, Session.class);
		
		return json;
	}
	
	/**
	 * Converts this Session into a Cookie object.
	 *	Cookie Format:
	 *			Header 	- 	WPISUITE-{username}
	 *			Body	-	a String containing a randomly generated long.
	 * @return	a Cookie object representing this Session
	 */
	public Cookie toCookie()
	{
		String header = "WPISUITE-" + getUsername();
		return new Cookie(header, this.ssid);
	}
	
	/**
	 * Generates the Session ID as a random long. If the SSID has already been
	 * 	instantiated then that value is returned.
	 * @return	a long
	 */
	public String generateSessionId()
	{
		if(this.ssid != null)
		{
			return this.ssid;
		}
		
		Random rand = new Random();

		long ssid = rand.nextLong();
		
		return String.valueOf(ssid);
	}
}

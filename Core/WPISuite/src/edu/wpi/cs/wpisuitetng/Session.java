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
		loginTime = new Date();
	}
	
	public String getUsername()
	{
		return user.getUsername();
	}
	
	public Date getLoginTime()
	{
		return loginTime;
	}
	
	/**
	 * Converts this Session into a Cookie object.
	 * @return	a Cookie object representing this Session
	 */
	public Cookie toCookie()
	{
		String json ="";
		
		Gson gson = new Gson();
		
		json = gson.toJson(this, Session.class);
		
		return new Cookie(getUsername(), json);
	}
}

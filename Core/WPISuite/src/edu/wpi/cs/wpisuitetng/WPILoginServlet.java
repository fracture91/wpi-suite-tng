package edu.wpi.cs.wpisuitetng;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wpi.cs.wpisuitetng.exceptions.*;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Servlet implementation class WPILoginServlet
 */
@WebServlet("/login")
public class WPILoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WPILoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// parse the POST body into a username:password string
		BufferedReader in = request.getReader();
		String[] decoded = decodeAuthToken(in); // String array [<username>, <password>]
		
		// TODO: find a way to avoid the BASIC_AUTH-specific 'username:password' decoded format.
		
		// Authentication
		try 
		{	
			Session ses = loginUser(decoded[0], decoded[1]);
			
			// post back the Session Cookie.
			Cookie userCookie = ses.toCookie();
			response.addCookie(userCookie);
			response.setStatus(HttpServletResponse.SC_CONTINUE);  //100 - Client can continue
		}
		catch(WPISuiteException e) // Authentication Failed.
		{
			//TODO: log error
			response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 - Forbidden, Authentication Failed.
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// sends back a test cookie
		Cookie userCookie = new Cookie("user","test");
		resp.addCookie(userCookie);
	}

	/**
	 * Evaluates user authentication credentials.
	 * 	Creates and returns a Session if authentication is successful
	 * @param username
	 * @param password
	 * @return	the Session for this user.
	 */
	private Session loginUser(String username, String password) throws WPISuiteException
	{
		ManagerLayer manager = ManagerLayer.getInstance();
		User[] u = manager.getUsers().getEntity(username);
		
		if(u.length == 0)
		{
			throw new AuthenticationException();	//"No user with the given username found");
		}

		User user = u[0];
		
		if(!user.matchPassword(password))
		{
			throw new AuthenticationException();
		}
		
		// create a Session mapping in the ManagerLayer
		Session ses = manager.getSessions().createSession(user);
		
		//TODO: handle Sessions that already exist. Renewing sessions, etc.
		
		return ses;
	}
	
	/**
	 * Handles Authentication Token decoding.
	 * 
	 * @param body	The raw POST body to be parsed. 
	 * @return	a String array containing login credentials where [0] username, [1] password
	 */
	private String[] decodeAuthToken(BufferedReader body)
	{
		String[] credentials = {"username", "password"};
		
		return credentials;
	}

	
}

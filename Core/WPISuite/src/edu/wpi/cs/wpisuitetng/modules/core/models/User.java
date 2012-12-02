package edu.wpi.cs.wpisuitetng.modules.core.models;

import com.google.gson.*;

import edu.wpi.cs.wpisuitetng.database.TNG;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.Model;

/**
 * The Data Model representation of a User. Implements
 * 	database interaction and serializing.
 * @author mdelladonna (sp?), twack
 */

public class User extends AbstractModel implements TNG
{
	private String name;
	private String username;
	private String password;
	private int idNum;
	private Role role;
	
	/**
	 * The primary constructor for a User
	 * @param name	User's full name
	 * @param username	User's username (nickname)
	 * @param idNum	User's ID number
	 */
	public User(String name, String username, String password, int idNum)
	{
		this.name = name;
		this.username = username;
		this.password = password;
		this.idNum = idNum;
		this.role = Role.USER;
	}
	
	/**
	 * Performs password checking logic. In the future, this may implement hashing.
	 * @param pass	the password String to compare
	 * @return	True if the password matches, False otherwise.
	 */
	public boolean matchPassword(String pass)
	{
		return password.equals(pass);
	}
	
	/* Accessors */
	public String getName()
	{
		return name;
	}
	
	public int getIdNum()
	{
		return idNum;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	/* database interaction */
	public void save()
	{
		return;
	}
	
	public void delete()
	{
		return;
	}
	
	/* Serializing */
	
	/**
	 * Serializes this User model into a JSON string.
	 * 
	 * @return	the JSON representation of this User
	 */
	public String toJSON()
	{
		String json;
		
		Gson gson = new Gson();
		
		json = gson.toJson(this, User.class);
		/*
		json = "{";
		json += "name:"+ name +",";
		json += "idNum:"+ idNum;
		json += "}";
		*/
		
		return json;
		
	}
	
	/**
	 * Static method offering comma-delimited JSON
	 * 	serializing of User lists
	 * @param u	an array of Users
	 * @return	the serialized array of Users
	 */
	public static String toJSON(User[] u)
	{
		String json ="";
		
		Gson gson = new Gson();
		
		json = gson.toJson(u, User[].class);
		/*
		for(User a : u)
		{
			json += gson.toJson(a, User.class);
		}
		
		/*
		json = "{";
		json += "name:"+ name +",";
		json += "idNum:"+ idNum;
		json += "}";
		*/
		
		return json;
		
	}
	
	/* Built-in overrides/overloads */
	
	/**
	 * Override of toString() to return a JSON string for now.
	 * 	May override in the future.
	 */
	public String toString()
	{
		return this.toJSON();
	}

	@Override
	public Boolean identify(Object o)
	{
		Boolean b  = false;
		
		if(o instanceof User)
			if(((User) o).username.equalsIgnoreCase(this.username))
				b = true;
		
		if(o instanceof String)
			if(((String) o).equalsIgnoreCase(this.username))
				b = true;
		return b;
	}
	
	public Role getRole()
	{
		return role;
	}
	
	public void setRole(Role r)
	{
		role = r;
	}
}

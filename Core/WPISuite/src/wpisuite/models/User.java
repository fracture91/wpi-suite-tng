package wpisuite.models;

import placeholderFiles.TNG;

import com.google.gson.*;
public class User implements TNG{

	private String name;
	private String username;
	private int idNum;
	
	public User(String name, String username, int idNum)
	{
		this.name = name;
		this.username = username;
		this.idNum = idNum;
	}
	
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
	
	public boolean equals(User aUser){
		return this.idNum == aUser.getIdNum() &&
				this.name.equalsIgnoreCase(aUser.getName()) &&
				this.username.equalsIgnoreCase(aUser.getUsername());
				
	}
}

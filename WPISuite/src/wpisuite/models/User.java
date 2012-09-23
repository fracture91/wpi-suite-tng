package wpisuite.models;

import com.google.gson.*;
public class User {

	private String name;
	private int idNum;
	
	public User(String name, int idNum)
	{
		this.name = name;
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
}

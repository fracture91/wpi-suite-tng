package wpisuite.models;

import com.google.gson.Gson;

public class Project 
{
	private String name;
	private int idNum;
	
	public Project(String name, int idNum)
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
		
		json = gson.toJson(this, Project.class);
		return json;
		
	}
	
	public static String toJSON(Project[] u)
	{
		String json ="";
		
		Gson gson = new Gson();
		
		for(Project a : u)
		{
			json += gson.toJson(a, Project.class);
		}
		
		/*
		json = "{";
		json += "name:"+ name +",";
		json += "idNum:"+ idNum;
		json += "}";
		*/
		
		return json;
		
	}
}

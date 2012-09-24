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
}

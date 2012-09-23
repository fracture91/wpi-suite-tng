package wpisuite.models;

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
		
		json = "{";
		json += "name:"+ name +",";
		json += "idNum:"+ idNum +",";
		json += "}";
		
		return json;
		
	}
}

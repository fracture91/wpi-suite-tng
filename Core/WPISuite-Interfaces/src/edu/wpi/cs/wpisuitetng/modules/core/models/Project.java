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
 *    mpdelladonna
 *    bgaffey
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.core.models;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.Model;

/**
 * The Data Model representation of a Project. Offers
 * 	serialization and database interaction.
 * @author mdelladonna, twack, bgaffey
 */


public class Project extends AbstractModel
 {

	private String name;
	private String idNum;
	private String[] supportedModules;
	private User owner;
	private ArrayList<User> team;
	
	/**
	 * Primary constructor for a Project
	 * @param name - the project name
	 * @param idNum - the project ID number as a string
	 * @param owner - The User who owns this project
	 * @param team - The User[] who are associated with the project
	 * @param supportedModules - the modules supported by this project
	 */
	public Project(String name, String idNum, User owner, User[] team, String[] supportedModules)
	{
		this.name = name;
		this.idNum = idNum;
		this.owner = owner;
		this.supportedModules = supportedModules;
		
		if(team != null)
		{
			this.team = new ArrayList<User>(Arrays.asList(team));
		}
		else
		{
			this.team = new ArrayList<User>();
		}
	}
	
	/**
	 * Secondary constructor for a Project
	 * @param name	the project name
	 * @param idNum	the ID number to associate with this Project.
	 */
	public Project(String name, String idNum)
	{
		this.name = name;
		this.idNum = idNum;
	}
	
	/* Accessors */
	public String getName()
	{
		return name;
	}
	
	public String getIdNum()
	{
		return idNum;
	}
	
	/* Mutators */
	public void setName(String newName)
	{
		this.name = newName;
	}
	
	private void setIdNum(String newId)
	{
		this.idNum = newId;
	}
	
	/* database interaction */
	
	/**
	 * Implements Model-specific save logic
	 */
	public void save()
	{
		return; // TODO: implement saving during API - DB Layer Link up
	}
	
	/**
	 * Implements Model-specific delete logic
	 */
	public void delete()
	{
		return; // TODO: implement deleting during API - DB Layer Link up
	}
	
	public String getProjectName() {
		return this.name;
	}
	
	/* Serializing */
	
	/**
	 * Serializes this Project's member variables into
	 * 	a JSON string.
	 * @return	the JSON string representation of this Project
	 */
	public String toJSON()
	{
		String json = null;
		
		json = "{";
		
		json += "\"name\":\"" + this.name +"\"";
		
		json += ",\"idNum\":\"" + this.idNum+"\"";
		
		if(this.owner != null)
		{
			json += ",\"owner\":" + this.owner.toJSON();
		}
		
		if(this.supportedModules != null && this.supportedModules.length > 0)
		{
			json += ",\"supportedModules\":[";
			
			for(String str : this.supportedModules)
			{
				json += "\"" + str + "\",";
			}
			
			//remove that last comma
			json = json.substring(0, json.length()-1);
			
			json += "]";
		}		
		
		if(this.team != null && this.team.size() > 0)
		{
			json += ",\"team\":[";
		
			for(User u : this.team)
			{
				json += u.toJSON() + ",";
			}
			//remove that last comma
			json = json.substring(0, json.length()-1);
		
			json += "]";
		}
		
		json += "}";
		
		return json;
	}
	
	/**
	 * Static Project method that serializes a list of Projects
	 * 	into JSON strings.
	 * @param u	The list of Projects to serialize
	 * @return	a comma delimited list of Project JSON strings.
	 */
	public static String toJSON(Project[] u)
	{
		String json ="";
		
		Gson gson = new Gson();
		
		json = gson.toJson(u, Project[].class);
		
		
		return json;
		
	}
	
	/**
	 * Deserializes the given JSON String into a Project's member variables
	 * @return	the Project from the given JSON string representation 
	 */
	public static Project fromJSON(String json)
	{
		Gson gson;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Project.class, new ProjectDeserializer());

		gson = builder.create();
		
		return gson.fromJson(json, Project.class);
	}
	
	/* Built-in overrides/overloads */
	
	/**
	 * Returns the JSON representation of this object as 
	 * 	the toString.
	 * 
	 * @return	the String returned by toJSON()
	 * @see		Project.toJSON()
	 */
	public String toString()
	{
		return this.toJSON();
	}
	
	@Override
	public Boolean identify(Object o)
	{
		Boolean b  = false;
		
		if(o instanceof Project)
		{
			if(((Project) o).getIdNum().equalsIgnoreCase(this.idNum))
				{
					b = true;
				}
		}
		
		if(o instanceof String)
			if(((String) o).equalsIgnoreCase((this.idNum)))
				b = true;
		
		
		return b;
	}
	
	@Override
	public boolean equals(Object anotherProject) {
		if(anotherProject instanceof Project)
		{
			if( ((Project)anotherProject).idNum.equals(this.idNum))
			{
				//things that can be null
				if(this.name != null && !this.name.equals(((Project)anotherProject).name))
				{
					return false;
				}
				
				if(this.idNum != null && !this.idNum.equals(((Project)anotherProject).idNum))
				{
					return false;
				}
				
				return true;
			}
		}
		return false;
	}

	public String[] getSupportedModules() {
		return supportedModules;
	}

	public void setSupportedModules(String[] supportedModules) {
		this.supportedModules = supportedModules;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public User[] getTeam() {
		User[] a = new User[1];
		return team.toArray(a);
	}
	
	/**
	 * adds a team member to the team
	 * @param u - the user to add to the team
	 * @return true if the user was added, false if the user was already in the team
	 */
	public boolean addTeamMember(User u)
	{
		if(!team.contains(u))
		{
			team.add(u);
			return true;
		}
		return false;
	}
	
	/**
	 * removes a team member from the team
	 * @param u - the team member to remove from the team
	 * @return - true if the member was removed, false if they were not in the team
	 */
	public boolean removeTeamMember(User u)
	{
		if(team.contains(u))
		{
			team.remove(u);
			return true;
		}
		return false;
	}

	
	@Override
	public Project getProject() {
		return null;
	}

	@Override
	public void setProject(Project aProject) {
		//Can't set a project's project
	}

}

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
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.core.models;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * The Data Model representation of a Project. Offers
 * 	serialization and database interaction.
 * @author mdelladonna, twack, bgaffey
 */


public class Project extends AbstractModel
 {

	private String name;
	private String idNum;
	private Role role;
	private Permission permission; 
	
	/**
	 * Primary constructor for a Project
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
	
	public void setIdNum(String newId)
	{
		this.idNum = newId;
	}
	
	/* database interaction */
	
	/**
	 * Implements Model-specific save logic
	 */
	public void save(Session s)
	{
		User theUser = s.getUser();
		if(getPermission(theUser).equals(permission.WRITE) || 
		   theUser.getRole().equals(role.ADMIN)){
			return; // TODO: implement saving during API - DB Layer Link up
		}
		else{
		return; 
		}
	}
	
	/**
	 * Implements Model-specific delete logic
	 */
	public void delete(Session s)
	{
		User theUser = s.getUser();
		if(getPermission(theUser).equals(permission.WRITE) || 
		   theUser.getRole().equals(role.ADMIN)){
			return; // TODO: implement saving during API - DB Layer Link up
		}
		else{
		return; 
		}
	}
	
	
	/* Serializing */
	
	/**
	 * Serializes this Project's member variables into
	 * 	a JSON string.
	 * @return	the JSON string representation of this Project
	 */
	public String toJSON()
	{
		String json;
		
		Gson gson = new Gson();
		
		json = gson.toJson(this, Project.class);
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
}

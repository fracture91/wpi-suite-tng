/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: mpdelladonna
 * rchamer
 * twack
 * bgaffey
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.core.entitymanagers;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.DatabaseException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class ProjectManager implements EntityManager<Project>{

	Class<Project> project = Project.class;
	Gson gson;
	Data data;
	
	public ProjectManager(Data data)
	{
		this.data = data;
		
		// hang the custom serializer/deserializer
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(this.project, new ProjectDeserializer());
		
		this.gson = builder.create();
	}
	
	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

	@Override
	public Project makeEntity(Session s, String content) throws WPISuiteException {	
		User theUser = s.getUser();
		if(theUser.getRole().equals(Role.ADMIN) ){
		
		Project p;
		try{
			p = gson.fromJson(content, project);
		} catch(JsonSyntaxException e){
			throw new BadRequestException("The entity creation string had invalid format. Entity String: " + content);
		}
		
		if(getEntity(s,p.getIdNum())[0] == null)
		{
			save(s,p);
		}
		else
		{
			throw new ConflictException("A project with the given ID already exists. Entity String: " + content); 
		}
		
		return p;
		}
		else{
			throw new UnauthorizedException("You do not have enough priveldges to create a project.");
		}
	}

	@Override
	public Project[] getEntity(Session s, String id) 
	{
		Project[] m = new Project[1];
		if(id.equalsIgnoreCase(""))
		{
			return getAll(s);
		}
		else
		{
			return data.retrieve(project, "idNum", id).toArray(m);
		}
	}
	
	/**
	 * returns a project without requiring a session, 
	 * specifically for the scenario where a session needs to be created.
	 * only ever returns one project, "" is not a valid argument;
	 * 
	 * @param id - the id of the user, in this case it's the idNum
	 * @return a list of matching projects
	 * @throws NotFoundException if the project cannot be found
	 */
	public Project[] getEntity(String id) throws NotFoundException
	{
		Project[] m = new Project[1];
		if(id.equalsIgnoreCase(""))
		{
			throw new NotFoundException("No (blank) Project id given.");
		}
		else
		{
			m = data.retrieve(project, "idNum", id).toArray(m);
			
			if(m[0] == null)
			{
				throw new NotFoundException("Project with id <" + id + "> not found.");
			}
			else
			{
				return m;
			}
		}
	}

	@Override
	public Project[] getAll(Session s) {
		Project[] ret = new Project[1];
		ret = data.retrieveAll(new Project("","")).toArray(ret);
		return ret;
	}

	@Override
	public void save(Session s, Project model) throws WPISuiteException {
		if(s == null){
			throw new WPISuiteException("Null Session.");
		}
		User theUser = s.getUser();
		if(theUser.getRole().equals(Role.ADMIN) || 
				model.getPermission(theUser).equals(Permission.WRITE)){
			if(data.save(model))
			{
				return ;
			}
			else
			{
				throw new DatabaseException("Save failure for Project."); // Session User: " + s.getUsername() + " Project: " + model.getName());
			}
		}
		else
			throw new UnauthorizedException("You do not have the requred permissions to perform this action.");
		
	}

	@Override
	public boolean deleteEntity(Session s1, String id) throws WPISuiteException
	{
		if(s1==null){
			throw new WPISuiteException("Null Session.");
		}
		User theUser = s1.getUser();
		Project[] model = this.getEntity(id);
		if(model[0].getPermission(theUser).equals(Permission.WRITE) || 
		   theUser.getRole().equals(Role.ADMIN)){
			Model m = data.delete(data.retrieve(project, "idNum", id).get(0));
			
			return (m != null) ? true : false;
		}
		else{
			throw new UnauthorizedException("You do not have the required permissions to perform this action.");
		}
	}
	
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		User theUser = s.getUser();
		if(theUser.getRole().equals(Role.ADMIN)){
		data.deleteAll(new Project("",""));
		}
		else
			throw new UnauthorizedException("You do not have the required permissions to perform this action.");
	}

	@Override
	public int Count() {
		
		return 0;
	}
	
	public Project update(Session s, Project toUpdate, String changeSet) throws WPISuiteException
	{
		if(s == null){
			throw new WPISuiteException("Null session.");
		}
		User theUser = s.getUser();
		if(toUpdate.getPermission(theUser).equals(Permission.WRITE) || 
		   theUser.getRole().equals(Role.ADMIN)){
		
			// convert updateString into a Map, then load into the User
			try
			{
				HashMap<String, Object> changeMap = new ObjectMapper().readValue(changeSet, HashMap.class);
			
				// check if the changeSet contains each field of User
				if(changeMap.containsKey("name"))
				{
					toUpdate.setName((String)changeMap.get("name"));
				}
				
				//probs shouldn't be able to change the idNum of a project once it's been created
				/*if(changeMap.containsKey("idNum"))
				{
					toUpdate.setIdNum((String)changeMap.get("idNum"));
				}*/
			}
			catch(Exception e)
			{
				throw new DatabaseException("Failure in the ProjectManager.update() changeset mapper."); // on Mapping failure
			}
			
			// save the changes back
			this.save(s, toUpdate);
			
			// check for changes in each field
			return toUpdate;
		}
		else
			throw new UnauthorizedException("You do not have the required permissions to perform this action.");
	}

	@Override
	public Project update(Session s, String content) throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedPut(Session s, String[] args, String content) throws WPISuiteException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPost(Session s, String string, String content) throws WPISuiteException {
		throw new NotImplementedException();
	}


	

}

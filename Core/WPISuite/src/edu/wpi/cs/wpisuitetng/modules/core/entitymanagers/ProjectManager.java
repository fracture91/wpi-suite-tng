/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.core.entitymanagers;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

public class ProjectManager implements EntityManager<Project>{

	Class<Project> project = Project.class;
	Gson gson;
	Data data;
	
	public ProjectManager(Data data)
	{
		gson = new Gson();
		this.data = data;
	}
	
	@Override
	public Project makeEntity(String content) {
		
		Project p;
		
		p = gson.fromJson(content, project);
		
		if(getEntity(p.getIdNum()).length == 0)
		{
			save(p);
		}
		
		return p;
	}

	@Override
	public Project[] getEntity(String id) 
	{
		Project[] m = new Project[1];
		if(id.equalsIgnoreCase(""))
		{
			return getAll();
		}
		else
		{
			return data.retrieve(project, "idNum", id).toArray(m);
		}
	}

	@Override
	public Project[] getAll() {
		// TODO Implement this feature in a later release (dependant on DB interface)
		return null;
	}

	@Override
	public void save(Project model) {
		data.save(model);
		
	}

	@Override
	public boolean deleteEntity(String id)
	{
		
		Model m = data.delete(data.retrieve(project, "idNum", id).get(0));
		
		return (m != null) ? true : false;
	}
	
	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}


	

}

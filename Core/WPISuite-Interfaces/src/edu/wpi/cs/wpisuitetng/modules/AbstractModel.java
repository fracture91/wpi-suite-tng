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

package edu.wpi.cs.wpisuitetng.modules;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Defines the behavior for permissions
 * @author mpdelladonna
 *
 */
public abstract class AbstractModel implements Model {

	private Map<User, Permission> permissionMap = new HashMap<User, Permission>(); // annotation for User serialization
	private Project project;
	
	@Override
	public Permission getPermission(User u) 
	{
		
		return permissionMap.get(u);
	}

	@Override
	public void setPermission(Permission p, User u) 
	{
		permissionMap.put(u, p);
	}
	
	@Override
	public Project getProject() {
		return project;
	}
	
	@Override
	public void setProject(Project p) {
		this.project = p;
	}


}

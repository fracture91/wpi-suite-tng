/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 *    Mike Della Donna
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * Persistent Model for a Tag - arbitrary strings you can attach to Defects
 * A wrapper class is necessary so we can easily search for existing tags,
 * like if we want to provide autocomplete functionality or make searching for tags easier. 
 */
public class Tag extends AbstractModel {
	
	private final String name;
	
	/**
	 * Construct a new Tag with the given name.
	 * 
	 *  @param name the name of the tag, must be neither null nor empty
	 */
	public Tag(String name) {
		this.name = name;
	}

	/**
	 * @return the name of this Tag
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, Tag.class);
		return json;
	}

	@Override
	public Boolean identify(Object o) {
		if(o instanceof String) {
			o = new Tag((String)o);
		}
		return equals(o);
	}
	
	@Override
	public boolean equals(Object other) {
		boolean returnValue = false;
		if(this == other) {
			returnValue = true;
		} else if(other instanceof Tag && name.equalsIgnoreCase(((Tag)other).name)) {
			returnValue = true;
		}
		return returnValue;
	}
	
	@Override
	public int hashCode() {
		if(name != null) {
			return name.hashCode();
		}
		return 0;
	}

}

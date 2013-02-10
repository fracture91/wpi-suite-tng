/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mpdelladonna
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules;

import com.google.gson.JsonParseException;

import edu.wpi.cs.wpisuitetng.database.Data;


public abstract class AbstractEntityManager implements EntityManager<Model> 
{
	Data data;
	
	public AbstractEntityManager(Data data)
	{
		this.data = data;
	}
	
	/**
	 * This static utility method takes a JSON string and attempts to
	 * 	retrieve a username field from it.
	 * @param serializedUser	a JSON string containing a password
	 * @return	the username field parsed.
	 */
	public static String parseFieldFromJSON(String serializedModel, String FieldName)
	{
		if(!serializedModel.contains(FieldName))
		{
			throw new JsonParseException("The given JSON string did not contain specified field.");
		}
		
		int fieldStartIndex = serializedModel.indexOf(FieldName);
		int separator = serializedModel.indexOf(':', fieldStartIndex);
		int startIndex = serializedModel.indexOf('"', separator) + 1;
		int endIndex = serializedModel.indexOf('"', startIndex);
		
		String field = serializedModel.substring(startIndex, endIndex);
		
		return field;
	}
}

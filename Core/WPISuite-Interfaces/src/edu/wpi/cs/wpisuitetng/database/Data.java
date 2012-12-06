/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    ??
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.database;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.Model;

public interface Data 
{
	public <T> boolean save(T aTNG);
	public List<Model> retrieve(@SuppressWarnings("rawtypes") final Class anObjectQueried, String aFieldName, final Object theGivenValue);
	public <T> T delete(T aTNG);
	public void update(final Class anObjectToBeModified, String fieldName, Object uniqueID, String changeField, Object changeValue);
	
}

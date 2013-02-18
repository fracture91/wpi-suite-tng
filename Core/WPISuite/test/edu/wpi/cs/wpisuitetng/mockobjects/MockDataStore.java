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

package edu.wpi.cs.wpisuitetng.mockobjects;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class MockDataStore implements Data {

	private ArrayList<Model> models;
	
	private static MockDataStore myself = null;
	
	public static MockDataStore getMockDataStore()
	{
		if(myself == null)
			myself = new MockDataStore();
		return myself;
	}
	
	private MockDataStore()
	{
		models = new ArrayList<Model>();
		models.add(new Project("test", "5"));
		models.add(new User("steve", "steve",null, 0));
		models.add(new User("fred","fred",null, 1));
		models.add(new User("jeff","jeff",null, 2));
		models.add(new User("tyler","tyler",null, 3));
		models.add(new Project("WPISUITE","0"));
		models.add(new Project("ANDROID:BEARCLAW","1"));
		models.add(new Project("WINDOWS9","2"));
		models.add(new Project("OSX:HOUSECAT","3"));
		models.add(new Project("UBUNTU_RABID_RHINO","4"));

	}
	
	public Model save(String json, Class<? extends Model> type)
	{
		Gson gson = new Gson();
		Model m = gson.fromJson(json, type);
		Model[] n = retrieve(type, m);
		if(n == null)
			models.add(m);
		return (n == null) ? m : n[0];
	}
	
	public Model[] retrieve(Class<? extends Model> type, Object id)
	{
		List<Model> list = new ArrayList<Model>();
		Model[] mlist = new Model[1];
		if(id != null)
		{
			for(Model m : models)
			{
				if(m.getClass() == type && m.identify(id))
				{
					list.add(m);
					return list.toArray(mlist);
				}
			}
			return null;
		}
		else
		{
			for(Model m : models)
			{
				if(m.getClass() == type)
					list.add(m);
			}
			return list.toArray(mlist);
		}
	}
	
	public Model[] retrieve(Class<? extends Model> type, Object id, Project aProject)
	{
		return retrieve(type, id);
	}
	public String remove(Class<? extends Model> type, Object id)
	{
		if(id != null)
		{
			for(Model m : models)
			{
				if(m.identify(id) && m.getClass() == type)
				{
					if(models.remove(m))
						return null;
					else
						return "entry not found";
				}
			}
			return "entry not found";
		}
		return "id not specified";
	}

	@Override
	public <T> boolean save(T aTNG) {
		System.out.println("DEBUG: Inside save");
		System.out.println("DEBUG aTNG: "+ aTNG);
		return true;
	}

	@Override
	public List<Model> retrieve(Class anObjectQueried, String aFieldName,
			Object theGivenValue) {
		System.out.println("DEBUG: Inside retreive");
		System.out.println("DEBUG anObjectQueried: "+ anObjectQueried);
		System.out.println("DEBUG aFieldName: "+ aFieldName);
		System.out.println("DEBUG theGivenValue: "+ theGivenValue);
		List<Model> list = new ArrayList<Model>();
		if(theGivenValue != null)
		{
			for(Model m : models)
			{
				if(m.getClass() == anObjectQueried && m.identify(theGivenValue))
				{
					list.add(m);
					return list;
				}
			}
			return list;
		}
		else
		{
			for(Model m : models)
			{
				if(m.getClass() == anObjectQueried)
					list.add(m);
			}
			return list;
		}
	}

	public List<Model> retrieve(Class anObjectQueried, String aFieldName,
			Object theGivenValue, Project aProject) {
		return retrieve(anObjectQueried, aFieldName, theGivenValue);
	}
	
	@Override
	public <T> T delete(T aTNG) {
		System.out.println("DEBUG: Inside delete");
		System.out.println("DEBUG aTNG: "+ aTNG);
		return aTNG;
	}

	@Override
	public void update(Class anObjectToBeModified, String fieldName,
			Object uniqueID, String changeField, Object changeValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> List<T> retrieveAll(T aSample) {
		List<T> tmp = new ArrayList<T>();
		tmp.add(aSample);
		System.out.println("DEBUG: Inside retrieveAll");
		System.out.println("DEBUG aTNG: "+ tmp);
		return tmp;
	}

	@Override
	public <T> List<T> deleteAll(T aSample) {
		List<T> tmp = new ArrayList<T>();
		tmp.add(aSample);
		System.out.println("DEBUG: Inside deleteAll");
		System.out.println("DEBUG aTNG: "+ tmp);
		return tmp;
	}

	@Override
	public <T> boolean save(T aModel, Project aProject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> List<Model> retrieveAll(T aSample, Project aProject) {
				// TODO Auto-generated method stub
				return null;
			}
	public List<Model> notRetrieve(Class anObjectQueried, String aFieldName,
			Object theGivenValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Model> andRetrieve(Class anObjectQueried,
			String[] aFieldNameList, List<Object> theGivenValueList)
			throws WPISuiteException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Model> orRetrieve(Class anObjectQueried,
			String[] aFieldNameList, List<Object> theGivenValueList)
			throws WPISuiteException, IllegalAccessException,
			InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<Model> deleteAll(T aSample, Project aProject) {
				// TODO Auto-generated method stub
				return null;
			}
	public List<Model> complexRetrieve(Class andanObjectQueried,
			String[] andFieldNameList, List<Object> andGivenValueList,
			Class orAnObjectQueried, String[] orFieldNameList,
			List<Object> orGivenValueList) throws WPISuiteException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}

}

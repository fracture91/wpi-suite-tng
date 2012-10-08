package edu.wpi.cs.wpisuitetng.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;

import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class MockDataStore {

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
		models.add(new User("steve", "steve",0));
		models.add(new User("fred","fred",1));
		models.add(new User("jeff","jeff",2));
		models.add(new User("tyler","tyler",3));
		models.add(new Project("WPISUITE",0));
		models.add(new Project("ANDROID:BEARCLAW",1));
		models.add(new Project("WINDOWS9",2));
		models.add(new Project("OSX:HOUSECAT",3));
		models.add(new Project("UBUNTU_RABID_RHINO",4));
	}
	
	public Model save(String json, Class<? extends Model> type)
	{
		Gson gson = new Gson();
		Model m = gson.fromJson(json, type);
		models.add(m);
		return m;
	}
	
	public Model[] retrieve(Class<? extends Model> type, Object id)
	{
		List<Model> list = new ArrayList<Model>();
		Model[] mlist = new Model[1];
		if(id != null)
		{
			for(Model m : models)
			{
				if(m.identify(id) && m.getClass() == type)
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
				return "entry not found";
			}
			return null;
		}
		return "id not specified";
	}
}

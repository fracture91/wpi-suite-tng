package edu.wpi.cs.wpisuitetng.modules.core.entitymanagers;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

public class ProjectManager implements EntityManager<Project>{

	Class<Project> project = Project.class;
	Gson gson;
	
	public ProjectManager()
	{
		gson = new Gson();
	}
	
	@Override
	public Project makeEntity(Session s, String content) {
		
		Project p;
		
		p = gson.fromJson(content, project);
		
		if(getEntity(s, ((Integer) p.getIdNum()).toString() ).length == 0)
		{
			save(s,p);
		}
		
		return p;
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
			return DataStore.getDataStore().retrieve(project, "idNum", id).toArray(m);
		}
	}

	@Override
	public Project[] getAll(Session s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Session s, Project model) {
		DataStore.getDataStore().save(model);
		
	}

	@Override
	public boolean deleteEntity(Session s1, String id)
	{
		DataStore data = DataStore.getDataStore();
		
		String s = data.delete(data.retrieve(project, "idNum", id).get(0));
		
		return (s.startsWith("Deleted")) ? true : false;
	}
	
	@Override
	public void deleteAll(Session s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int Count() {
		
		return 0;
	}


	

}

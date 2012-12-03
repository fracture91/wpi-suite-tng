package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import java.util.ArrayList;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;

public class DefectManager implements EntityManager<Defect> {

	Gson gson;
	
	public DefectManager() {
		gson = new Gson();
	}
	
	@Override
	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deleteEntity(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Defect[] getAll() {
		// TODO: gross hack, use DataStore.retrieveAll
		return DataStore.getDataStore().retrieve(Defect.class, "events",
		                                         new ArrayList<DefectEvent>()).toArray(new Defect[0]);
	}

	@Override
	public Defect[] getEntity(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Defect makeEntity(String content) {
		final Defect newDefect = gson.fromJson(content, Defect.class);
		
		// TODO: increment properly, ensure uniqueness using ID generator.  This is a gross hack.
		final Defect[] existingDefects = getAll();
		newDefect.setId(existingDefects.length + 1);
		
		// TODO: validation
		
		DataStore.getDataStore().save(newDefect);
		return newDefect;
	}

	@Override
	public void save(Defect arg0) {
		System.out.println("Save entity: " + arg0);
		
	}

}

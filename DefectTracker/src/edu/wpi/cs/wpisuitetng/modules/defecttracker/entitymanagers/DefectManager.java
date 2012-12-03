package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import java.util.ArrayList;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;

public class DefectManager implements EntityManager<Defect> {

	DataStore db;
	Gson gson;
	
	public DefectManager() {
		db = DataStore.getDataStore();
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
		return db.retrieve(Defect.class, "events", new ArrayList<DefectEvent>()).toArray(new Defect[0]);
	}

	@Override
	public Defect[] getEntity(String stringId) {
		int id = Integer.parseInt(stringId);
		if(id < 1) {
			throw new NumberFormatException("Defect ID cannot be negative");
		}
		return db.retrieve(Defect.class, "id", id).toArray(new Defect[0]);
	}

	@Override
	public Defect makeEntity(String content) {
		final Defect newDefect = gson.fromJson(content, Defect.class);
		
		// TODO: increment properly, ensure uniqueness using ID generator.  This is a gross hack.
		final Defect[] existingDefects = getAll();
		newDefect.setId(existingDefects.length + 1);
		
		// TODO: validation
		save(newDefect);
		return newDefect;
	}

	@Override
	public void save(Defect defect) {
		db.save(defect);
	}

}

package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import java.util.ArrayList;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.database.DataStore;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;

/**
 * Provides database interaction for Defect models.
 */
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

	/**
	 * Return the User with the given username if they already exist in the database.
	 * If they don't exist, throw an IllegalArgumentException.
	 * 
	 * @param username the username of the User
	 * @return The User with the given username
	 * @throws IllegalArgumentException if the user doesn't exist
	 */
	private User getExistingUser(String username) throws IllegalArgumentException {
		final User[] existingUsers = db.getUser(username);
		if(existingUsers.length > 0 && existingUsers[0] != null) {
			return existingUsers[0];
		} else {
			throw new IllegalArgumentException("User " + username + " does not exist");
		}
	}
	
	@Override
	public Defect makeEntity(String content) {
		final Defect newDefect = gson.fromJson(content, Defect.class);
		
		// TODO: increment properly, ensure uniqueness using ID generator.  This is a gross hack.
		final Defect[] existingDefects = getAll();
		newDefect.setId(existingDefects.length + 1);
		
		// make sure the creator and assignee exist
		newDefect.setCreator(getExistingUser(newDefect.getCreator().getUsername()));
		// assignee doesn't get sent yet
		//newDefect.setAssignee(getExistingUser(newDefect.getAssignee().getUsername()));

		// TODO: validation
		save(newDefect);
		return newDefect;
	}

	@Override
	public void save(Defect defect) {
		db.save(defect);
	}

}

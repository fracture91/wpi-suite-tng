package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;

/**
 * Provides database interaction for Defect models.
 */
public class DefectManager implements EntityManager<Defect> {

	Data db;
	Gson gson;
	
	public DefectManager(Data data) {
		db = data;
		gson = new Gson();
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
		List<Model> existingUsers = db.retrieve(User.class, "username", username);
		if(existingUsers.size() > 0 && existingUsers.get(0) != null) {
			return (User) existingUsers.get(0);
		} else {
			throw new IllegalArgumentException("User " + username + " does not exist");
		}
	}

	@Override
	public Defect makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		final Defect newDefect = gson.fromJson(content, Defect.class);
		
		// TODO: increment properly, ensure uniqueness using ID generator.  This is a gross hack.
		final Defect[] existingDefects = getAll(s);
		newDefect.setId(existingDefects.length + 1);
		
		// make sure the creator and assignee exist
		newDefect.setCreator(getExistingUser(newDefect.getCreator().getUsername()));
		// assignee doesn't get sent yet
		//newDefect.setAssignee(getExistingUser(newDefect.getAssignee().getUsername()));

		// TODO: validation
		save(s, newDefect);
		return newDefect;
	}

	@Override
	public Defect[] getEntity(Session s, String id) throws NotFoundException,
			WPISuiteException {
		int intId = Integer.parseInt(id);
		if(intId < 1) {
			throw new NumberFormatException("Defect ID cannot be negative");
		}
		return db.retrieve(Defect.class, "id", intId).toArray(new Defect[0]);
	}

	@Override
	public Defect[] getAll(Session s) throws WPISuiteException {
		// TODO: gross hack, use DataStore.retrieveAll
		return db.retrieve(Defect.class, "events", new ArrayList<DefectEvent>()).toArray(new Defect[0]);
	}

	@Override
	public void save(Session s, Defect model) throws WPISuiteException {
		db.save(model);
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void deleteAll(Session s) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}

}

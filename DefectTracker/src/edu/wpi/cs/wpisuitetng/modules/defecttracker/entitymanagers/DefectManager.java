package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;

/**
 * Provides database interaction for Defect models.
 */
public class DefectManager implements EntityManager<Defect> {

	Data db;
	Gson gson;
	
	/**
	 * Create a DefectManager
	 * @param data The Data instance to use
	 */
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
	 * @throws BadRequestException if the user doesn't exist
	 */
	private User getExistingUser(String username) throws IllegalArgumentException, BadRequestException {
		final List<Model> existingUsers = db.retrieve(User.class, "username", username);
		if(existingUsers.size() > 0 && existingUsers.get(0) != null) {
			return (User) existingUsers.get(0);
		} else {
			throw new BadRequestException();
		}
	}

	@Override
	public Defect makeEntity(Session s, String content) throws BadRequestException {
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
	public Defect[] getEntity(Session s, String id) throws NotFoundException {
		if(id == null || id.equals("")) {
			// TODO: getAll should be called from the servlet directly
			return getAll(s);
		}
		final int intId = Integer.parseInt(id);
		if(intId < 1) {
			throw new NotFoundException();
		}
		final Defect[] defects = db.retrieve(Defect.class, "id", intId).toArray(new Defect[0]);
		if(defects.length < 1 || defects[0] == null) {
			throw new NotFoundException();
		}
		return defects;
	}

	@Override
	public Defect[] getAll(Session s) {
		return db.retrieveAll(new Defect()).toArray(new Defect[0]);
	}

	@Override
	public void save(Session s, Defect model) {
		db.save(model);
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
	}
	
	@Override
	public void deleteAll(Session s) {
		db.deleteAll(new Defect());
	}
	
	@Override
	public int Count() {
		return getAll(null).length;
	}

}

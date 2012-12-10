package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectStatus;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Tag;

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
	public Defect makeEntity(Session s, String content) throws WPISuiteException {
		final Defect newDefect = gson.fromJson(content, Defect.class);
		
		// TODO: increment properly, ensure uniqueness using ID generator.  This is a gross hack.
		newDefect.setId(Count() + 1);
		
		// new defects should always have new status
		newDefect.setStatus(DefectStatus.NEW);
		
		// make sure title and description size are within constraints
		if(newDefect.getTitle() == null || newDefect.getTitle().length() > 150
				|| newDefect.getTitle().length() <= 5) {
			throw new BadRequestException();
		}
		if(newDefect.getDescription() == null) {
			// empty descriptions are okay
			newDefect.setDescription("");
		}else if(newDefect.getDescription().length() > 5000) {
			throw new BadRequestException();
		}
		
		// make sure the creator and assignee exist and aren't duplicated
		newDefect.setCreator(getExistingUser(newDefect.getCreator().getUsername()));
		if(newDefect.getAssignee() != null) { // defects can be missing an assignee
			newDefect.setAssignee(getExistingUser(newDefect.getAssignee().getUsername()));
		}
		
		// make sure we don't insert duplicate tags
		final Set<Tag> tags = newDefect.getTags();
		final List<Tag> existingTags = db.retrieveAll(new Tag("blah"));
		for(Tag tag : tags) {
			int existingIndex = existingTags.indexOf(tag);
			if(existingIndex != -1) {
				tags.remove(tag);
				tags.add(existingTags.get(existingIndex));
			} else if(tag.getName() == null || tag.getName().length() < 1) {
				// tags with empty names aren't allowed
				// TODO: this validation should probably happen in Tag's EntityManager
				throw new BadRequestException();
			}
		}
		
		// make sure we're not being spoofed with some weird date
		final Date creationDate = new Date();
		newDefect.setCreationDate(creationDate);
		newDefect.setLastModifiedDate((Date)creationDate.clone());
		
		// new defects should never have any events
		newDefect.setEvents(new ArrayList<DefectEvent>());

		if(!db.save(newDefect)) {
			throw new WPISuiteException();
		}
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
		// TODO: validate updates
		db.save(model);
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws NotFoundException {
		// TODO: are nested objects deleted?  Dates should be, but Users shouldn't!
		return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
	}
	
	@Override
	public void deleteAll(Session s) {
		db.deleteAll(new Defect());
	}
	
	@Override
	public int Count() {
		// TODO: there must be a faster way to do this with db4o
		return getAll(null).length;
	}

}

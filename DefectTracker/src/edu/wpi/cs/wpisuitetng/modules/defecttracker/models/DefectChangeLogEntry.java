package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Persistent Model that holds information about a set of changes to a Defect.
 * Every time a Defect is changed by a user, a DefectChangeLogEntry should be created
 * containing the changes and the user responsible for making them.
 */
public class DefectChangeLogEntry implements Model, DefectEvent {

	private Date date;
	private User user;
	private Map<String, FieldChange> changes;
	
	/**
	 * Construct a DefectChangeLogEntry with default properties.
	 */
	public DefectChangeLogEntry() {
		date = new Date();
		user = new User("", "", -1);
		changes = new HashMap<String, FieldChange>();
	}
	
	/**
	 * Construct a DefectChangeLogEntry with the given properties.
	 * Other properties are the same as in the default constructor.
	 * 
	 * @param user the User responsible for this change
	 */
	public DefectChangeLogEntry(User user) {
		this();
		this.user = user;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the map of field names to changes (Assignee -> (Bob, Joe))
	 */
	public Map<String, FieldChange> getChanges() {
		return changes;
	}

	/**
	 * @param changes the changes to set
	 */
	public void setChanges(Map<String, FieldChange> changes) {
		this.changes = changes;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, DefectChangeLogEntry.class);
		return json;
	}

	// this model will only be created server side and then retrieved as part of a Defect in the future
	// so I'm not sure if this is necessary
	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

}

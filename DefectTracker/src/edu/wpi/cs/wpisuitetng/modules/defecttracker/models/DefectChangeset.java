package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Persistent Model that holds information about a set of changes to a Defect.
 * Every time a Defect is changed by a user, a DefectChangeset should be created
 * containing the changes and the user responsible for making them.
 */
public class DefectChangeset extends DefectEvent {

	private Map<String, FieldChange> changes;
	
	/**
	 * Construct a DefectChangeset with default properties.
	 */
	public DefectChangeset() {
		type = EventType.CHANGESET;
		changes = new HashMap<String, FieldChange>();
	}
	
	/**
	 * Construct a DefectChangeset with the given properties.
	 * Other properties are the same as in the default constructor.
	 * 
	 * @param user the User responsible for this change
	 */
	public DefectChangeset(User user) {
		this();
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
		json = gson.toJson(this, DefectChangeset.class);
		return json;
	}

	// this model will only be created server side and then retrieved as part of a Defect in the future
	// so I'm not sure if this is necessary
	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Permission getPermission(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPermission(Permission p, User u) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Project getProject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectName() {
		// TODO Auto-generated method stub
		return null;
	}

}

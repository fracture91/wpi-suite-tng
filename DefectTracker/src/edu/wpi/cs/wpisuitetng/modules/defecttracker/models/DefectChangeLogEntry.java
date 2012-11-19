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
public class DefectChangeLogEntry implements Model {

	private int defectId;
	private Date changeDate;
	private User editor;
	private Map<String, FieldChange> changes;
	
	/**
	 * Construct a DefectChangeLogEntry with default properties.
	 */
	public DefectChangeLogEntry() {
		defectId = -1;
		changeDate = new Date();
		editor = new User("", "", -1);
		changes = new HashMap<String, FieldChange>();
	}
	
	/**
	 * Construct a DefectChangeLogEntry with the given properties.
	 * Other properties are the same as in the default constructor.
	 * 
	 * @param defectId the id of the Defect this entry is associated with
	 * @param editor the User responsible for this change
	 */
	public DefectChangeLogEntry(int defectId, User editor) {
		this();
		this.defectId = defectId;
		this.editor = editor;
	}
	
	/**
	 * @return the id of the Defect this entry is associated with
	 */
	public int getDefectId() {
		return defectId;
	}

	/**
	 * @param defectId the defectId to set
	 */
	public void setDefectId(int defectId) {
		this.defectId = defectId;
	}

	/**
	 * @return the date this change occurred
	 */
	public Date getChangeDate() {
		return changeDate;
	}

	/**
	 * @param changeDate the changeDate to set
	 */
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	/**
	 * @return the User responsible for this change
	 */
	public User getEditor() {
		return editor;
	}

	/**
	 * @param editor the editor to set
	 */
	public void setEditor(User editor) {
		this.editor = editor;
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

	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

}

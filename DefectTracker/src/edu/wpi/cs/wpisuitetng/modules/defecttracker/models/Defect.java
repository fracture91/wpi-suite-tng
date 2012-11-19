package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.Model;

/**
 * Represents a defect
 *
 * TODO: Implement core model interface once the dev-core branch is merged
 */
public class Defect implements Model {
	protected int id;
	protected String description, title, user;
	
	/**
	 * Constructs a new Defect with the given properties
	 * @param id the unique id of the defect
	 * @param description the description of the defect
	 * @param title the title of the defect
	 * @param user the name of the user that created the defect
	 */
	public Defect(int id, String description, String title, String user) {
		this.id = id;
		this.description = description;
		this.title = title;
		this.user = user;
	}
	
	/**
	 * Converts the Defect to a JSON string
	 * @return a string in JSON representing the Defect
	 */
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, Defect.class);
		return json;
	}
	
	/**
	 * Converts the given list of Defects to a JSON string
	 * @param dlist a list of Defects
	 * @return a string in JSON representing the list of Defects
	 */
	public static String toJSON(Defect[] dlist) {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(dlist, Defect.class);
		return json;
	}

	/**
	 * Returns the id of the Defect
	 * @return the id of the Defect
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id of the defect
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the description of the Defect
	 * @return the description of the Defect
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the defect
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the title of the defect
	 * @return the title of the defect
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the defect
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the name of the user that created this defect
	 * @return the name of the user that created this defect
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the name of the user that created the defect
	 * @param user the name of the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	// note that save and delete don't do anything even in the core's models at the moment
	
	@Override
	public void save() {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String toString() {
		return toJSON();
	}

	// interface documentation says this is necessary for the mock database
	// not sure if this is still needed otherwise
	@Override
	public Boolean identify(Object o) {
		Boolean returnValue = false;
		if(o instanceof Defect && id == ((Defect) o).getId()) {
			returnValue = true;
		}
		if(o instanceof String && Integer.toString(id).equals(o)) {
			returnValue = true;
		}
		return returnValue;
	}
	
}

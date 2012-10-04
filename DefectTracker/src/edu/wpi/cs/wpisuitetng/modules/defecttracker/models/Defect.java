package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import com.google.gson.Gson;

/**
 * Represents a defect
 *
 * TODO: Implement core model interface once the dev-core branch is merged
 */
public class Defect {
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
	
}

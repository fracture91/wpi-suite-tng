package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Persistent Model that represents a Defect.
 */
public class Defect implements Model {
	private int id;
	private String title, description, status;
	private User creator, assignee;
	private Set<String> tags;
	private Date creationDate, lastModifiedDate;
	
	/**
	 * Constructs a new Defect with default properties.
	 */
	public Defect() {
		id = -1;
		title = description = status = "";
		tags = new HashSet<String>();
		creationDate = new Date();
		lastModifiedDate = new Date();
	}
	
	/**
	 * Constructs a new Defect with the given properties
	 * Other properties are the same as the default constructor
	 * 
	 * @param id the unique id of the Defect
	 * @param title the title of the Defect
	 * @param description the description of the Defect
	 * @param creator the User who created the Defect
	 */
	public Defect(int id, String title, String description, User creator) {
		this();
		this.id = id;
		this.title = title;
		this.description = description;
		this.creator = creator;
	}

	/**
	 * @return the unique id of this Defect (-1 if this is a new Defect)
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id of this Defect
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the title of this Defect
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title of this Defect
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the description of this Defect
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description of this Defect
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	// TODO: enum?  how do we define acceptable statuses?
	/**
	 * @return the status of this Defect
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * @param status the status of this Defect
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the user who created this Defect
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator the user who created this Defect
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	/**
	 * @return the user who is assigned to this Defect
	 */
	public User getAssignee() {
		return assignee;
	}

	/**
	 * @param assignee the user who is assigned to this Defect
	 */
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}
	
	/**
	 * @return the set of tags for this Defect
	 */
	public Set<String> getTags() {
		return tags;
	}
	
	/**
	 * @param tags the tags for this Defect
	 */
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
	
	/**
	 * @return the Date this Defect was created on
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the Date this Defect was created on
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the Date this Defect was last modified on
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate the Date this Defect was last modified on
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	// note that save and delete don't do anything at the moment, even in the core's models
	@Override
	public void save() {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Converts this Defect to a JSON string
	 * @return a string in JSON representing this Defect
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

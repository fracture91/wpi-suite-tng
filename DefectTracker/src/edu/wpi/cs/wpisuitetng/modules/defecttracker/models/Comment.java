package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import java.util.Date;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Persistent Model that represents a Comment on a Defect 
 */
public class Comment implements Model {

	private int id, defectId;
	private Date creationDate;
	private User creator;
	private String body;
	
	/**
	 * Create a Comment with default properties.
	 */
	public Comment() {
		id = defectId = -1;
		creationDate = new Date();
		creator = new User("", "", -1);
		body = "";
	}
	
	/**
	 * Create a Comment with the given properties.
	 * Other properties are the same as the default constructor.
	 * 
	 * @param defectId the id of the Defect the Comment is associated with
	 * @param creator the User who created the Comment
	 * @param body the message body of the Comment
	 */
	public Comment(int defectId, User creator, String body) {
		this.defectId = defectId;
		this.creator = creator;
		this.body = body;
	}
	
	/**
	 * @return the unique id of this Comment (-1 if this is a new Comment)
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the id of the Defect this Comment is associated with
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
	 * @return the Date this Comment was created on
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the User who created this comment
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * @return the message body of this Comment
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
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
		json = gson.toJson(this, Comment.class);
		return json;
	}

	@Override
	public Boolean identify(Object o) {
		Boolean returnValue = false;
		if(o instanceof Comment && id == ((Comment) o).getId()) {
			returnValue = true;
		}
		if(o instanceof String && Integer.toString(id).equals(o)) {
			returnValue = true;
		}
		return returnValue;
	}

}

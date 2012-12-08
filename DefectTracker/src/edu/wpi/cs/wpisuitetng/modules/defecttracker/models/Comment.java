package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import java.util.Date;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Persistent Model that represents a Comment on a Defect 
 */
public class Comment implements Model, DefectEvent {

	private int defectId;
	private Date date;
	private User user;
	private String body;
	
	/**
	 * Create a Comment with default properties.
	 */
	public Comment() {
		defectId = -1;
		date = new Date();
		user = new User("", "", "", -1);
		body = "";
	}
	
	/**
	 * Create a Comment with the given properties.
	 * Other properties are the same as the default constructor.
	 * 
	 * @param defectId the id of the Defect the Comment is associated with
	 * @param user the User who created the Comment
	 * @param body the message body of the Comment
	 */
	public Comment(int defectId, User user, String body) {
		this.defectId = defectId;
		this.user = user;
		this.body = body;
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
		// TODO
		return false;
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

}

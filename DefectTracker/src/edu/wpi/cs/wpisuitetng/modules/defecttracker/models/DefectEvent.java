package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import java.util.Date;

import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Implementations of this interface represent some kind of event in a defect.
 * For example, the addition of a comment or the modification of fields.
 */
public abstract class DefectEvent implements Model {
	
	public enum EventType {
		COMMENT,
		CHANGESET
	};
	
	protected Date date = new Date();
	protected User user = new User("", "", "", -1);
	
	/**
	 * The type of event this is.  Subclasses must specify this in order to be deserialized properly.
	 */
	protected EventType type;
	
	/**
	 * @return The Date when this event happened
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * @param date The Date of the Event to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * @return The User responsible for this event
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * @param user The User responsible for the event to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Given a builder, add anything to it that's necessary for Gson to interact with this class.
	 * 
	 * @param builder The builder to modify
	 */
	public static void addGsonDependencies(GsonBuilder builder) {
		builder.registerTypeAdapter(DefectEvent.class, new DefectEventDeserializer());
		builder.registerTypeAdapter(DefectChangeset.class, new DefectChangesetDeserializer());
	}
	
}

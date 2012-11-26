package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

import java.util.Date;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Implementations of this interface represent some kind of event in a defect.
 * For example, the addition of a comment or the modification of fields.
 */
public interface DefectEvent {
	
	/**
	 * @return The Date when this event happened
	 */
	public Date getDate();
	
	/**
	 * @param date The Date of the Event to set
	 */
	public void setDate(Date date);
	
	/**
	 * @return The User responsible for this event
	 */
	public User getUser();
	
	/**
	 * @param user The User responsible for the event to set
	 */
	public void setUser(User user);
}

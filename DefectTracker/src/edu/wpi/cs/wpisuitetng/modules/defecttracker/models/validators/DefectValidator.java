/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 *    Mike Della Donna
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.models.validators;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectStatus;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Tag;

/**
 * Validates Defects so that they fit in with the given Data implementation.
 * 
 * Note that Data could be something used client-side (e.g. a wrapper around a local cache of
 * Users so you can check assignee usernames as-you-type).
 */
public class DefectValidator {
	
	private Data data;
	private Defect lastExistingDefect;
	
	/**
	 * Create a DefectValidator
	 * 
	 * @param data The Data implementation to use
	 */
	public DefectValidator(Data data) {
		//TODO: "strict" mode for returning *all* issues, rather than ignoring and overwriting?
		this.data = data;
	}
	
	/**
	 * @return the data
	 */
	public Data getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Data data) {
		this.data = data;
	}

	/**
	 * Return the User with the given username if they already exist in the database.
	 * 
	 * @param username the username of the User
	 * @param issues list of errors to add to if user doesn't exist
	 * @param fieldName name of field to use in error if necessary
	 * @return The User with the given username, or null if they don't exist
	 * @throws WPISuiteException 
	 */
	User getExistingUser(String username, List<ValidationIssue> issues, String fieldName) throws WPISuiteException {
		final List<Model> existingUsers = data.retrieve(User.class, "username", username);
		if(existingUsers.size() > 0 && existingUsers.get(0) != null) {
			return (User) existingUsers.get(0);
		} else {
			issues.add(new ValidationIssue("User doesn't exist", fieldName));
			return null;
		}
	}
	
	/**
	 * Return the Defect with the given id if it already exists in the database.
	 * 
	 * @param id the id of the Defect
	 * @param project the project this defect belongs to
	 * @param issues list of errors to add to if defect doesn't exist
	 * @param fieldName name of field to use in error if necessary
	 * @return The Defect with the given id, or null if it doesn't exist
	 * @throws WPISuiteException 
	 */
	Defect getExistingDefect(int id, Project project, List<ValidationIssue> issues, String fieldName)
			throws WPISuiteException {
		List<Model> oldDefects = data.retrieve(Defect.class, "id", id, project);
		if(oldDefects.size() < 1 || oldDefects.get(0) == null) {
			issues.add(new ValidationIssue("Defect with id does not exist in project", fieldName));
			return null;
		} else {
			return (Defect) oldDefects.get(0);
		}
	}
	
	/**
	 * Validate the given model such that any nested models point to appropriate existing models
	 * from the Data given in the constructor.
	 * 
	 * @param session The session to validate against
	 * @param defect The defect model to validate
	 * @param mode The mode to validate for
	 * @return A list of ValidationIssues (possibly empty)
	 * @throws WPISuiteException 
	 */
	public List<ValidationIssue> validate(Session session, Defect defect, Mode mode) throws WPISuiteException {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		if(defect == null) {
			issues.add(new ValidationIssue("Defect cannot be null"));
			return issues;
		}
		
		Defect oldDefect = null;
		if(mode == Mode.EDIT) {
			oldDefect = getExistingDefect(defect.getId(), session.getProject(), issues, "id");
		}
		lastExistingDefect = oldDefect;
		
		if(mode == Mode.CREATE) {
			// new defects should always have new status
			defect.setStatus(DefectStatus.NEW);
		} else if(defect.getStatus() == null) {
			issues.add(new ValidationIssue("Cannot be null", "status"));
		}
		
		// make sure title and description size are within constraints
		if(defect.getTitle() == null || defect.getTitle().length() > 150
				|| defect.getTitle().length() < 5) {
			issues.add(new ValidationIssue("Required, must be 5-150 characters", "title"));
		}
		if(defect.getDescription() == null) {
			// empty descriptions are okay
			defect.setDescription("");
		} else if(defect.getDescription().length() > 5000) {
			issues.add(new ValidationIssue("Cannot be greater than 5000 characters", "description"));
		}
		
		// make sure the creator and assignee exist and aren't duplicated
		if(mode == Mode.EDIT) {
			if(oldDefect != null) {
				defect.setCreator(oldDefect.getCreator());
			}
		} else if(defect.getCreator() == null) {
			issues.add(new ValidationIssue("Required", "creator"));
		} else {
			User creator = getExistingUser(defect.getCreator().getUsername(), issues, "creator");
			if(creator != null) {
				if(!creator.getUsername().equals(session.getUsername())) {
					issues.add(new ValidationIssue("Must match currently logged in user", "creator"));
				} else {
					defect.setCreator(creator);
				}
			}
		}
		
		if(defect.getAssignee() != null) { // defects can be missing an assignee
			User assignee = getExistingUser(defect.getAssignee().getUsername(), issues, "assignee");
			if(assignee != null) {
				defect.setAssignee(assignee);
			}
		}
		
		
		if(defect.getTags() == null) {
			defect.setTags(new HashSet<Tag>());
		}
		final Set<Tag> tags = defect.getTags();
		if(tags.size() > 100) {
			issues.add(new ValidationIssue("Cannot have more than 100 tags", "tags"));
		} else {
			// need to make a new set because we can't modify existing set as we iterate over it
			final Set<Tag> newTags = new HashSet<Tag>();
			// validate each tag
			for(Tag tag : tags) {
				if(tag == null) {
					issues.add(new ValidationIssue("Cannot be null", "tags"));
					break;
				}
				List<Model> existingModels = data.retrieve(Tag.class, "name", tag.getName(),
						session.getProject());
				if(existingModels.size() > 0 && existingModels.get(0) != null) {
					// make sure we don't insert duplicate tags
					newTags.add((Tag) existingModels.get(0));
				} else if(tag.getName() == null || tag.getName().length() < 1) {
					// tags with empty names aren't allowed
					// TODO: this validation should probably happen in Tag's EntityManager
					issues.add(new ValidationIssue("Names can't be empty", "tags"));
					break;
				} else { //doesn't already exist, is valid
					newTags.add(tag);
				}
			}
			defect.setTags(newTags);
		}
		
		// make sure we're not being spoofed with some weird date
		final Date now = new Date();
		if(oldDefect != null) {
			defect.setCreationDate(oldDefect.getCreationDate());
		} else {
			defect.setCreationDate(now);
		}
		defect.setLastModifiedDate((Date)now.clone());
		
		if(oldDefect != null) {
			defect.setEvents(oldDefect.getEvents());
		} else {
			// new defects should never have any events
			defect.setEvents(new ArrayList<DefectEvent>());
		}
		
		return issues;
	}

	/**
	 * @return The last existing defect the validator fetched if in edit mode
	 */
	public Defect getLastExistingDefect() {
		return lastExistingDefect;
	}
	
}

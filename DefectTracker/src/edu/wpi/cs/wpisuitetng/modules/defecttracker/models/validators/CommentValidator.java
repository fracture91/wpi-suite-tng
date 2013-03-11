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
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.models.validators;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;

/**
 * Responsible for validating a comment model.
 */
public class CommentValidator {

	private Defect lastExistingDefect;
	private DefectValidator defectValidator;
	
	/**
	 * Create a DefectValidator
	 * 
	 * @param data The Data implementation to use
	 */
	public CommentValidator(Data data) {
		defectValidator = new DefectValidator(data);
	}
	
	/**
	 * Validate the given model such that any nested models point to appropriate existing models
	 * from the Data given in the constructor.
	 * 
	 * @param session The session to validate against
	 * @param comment The Comment model to validate
	 * @return A list of ValidationIssues (possibly empty)
	 * @throws WPISuiteException 
	 */
	public List<ValidationIssue> validate(Session session, Comment comment) throws WPISuiteException {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		if(comment == null) {
			issues.add(new ValidationIssue("Comment cannot be null"));
			return issues;
		}
		
		if(comment.getUser() == null) {
			issues.add(new ValidationIssue("Required", "user"));
		} else {
			User author = defectValidator.getExistingUser(comment.getUser().getUsername(), issues, "user");
			if(author != null) {
				if(!author.getUsername().equals(session.getUsername())) {
					issues.add(new ValidationIssue("Must match currently logged in user", "user"));
				} else {
					comment.setUser(author);
				}
			}
		}
		
		lastExistingDefect = defectValidator.getExistingDefect(comment.getDefectId(), session.getProject(),
				issues, "defectId");
		
		String body = comment.getBody();
		if(body == null || body.length() < 1 || body.length() > 10000) {
			issues.add(new ValidationIssue("Required, must be 1-10000 characters", "body"));
		}
		
		comment.setDate(new Date());
		
		return issues;
	}
	
	/**
	 * @return The last existing defect the validator fetched
	 */
	public Defect getLastExistingDefect() {
		return lastExistingDefect;
	}
	
}

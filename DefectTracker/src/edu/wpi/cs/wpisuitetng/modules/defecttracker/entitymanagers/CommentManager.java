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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.validators.CommentValidator;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.validators.ValidationIssue;

/**
 * Responsible for handling requests that interact with the Comment model.
 */
public class CommentManager implements EntityManager<Comment> {

	private final Data db;
	private final Gson gson;
	private final CommentValidator validator;
	
	/**
	 * Create a CommentManager that uses the given Data instance
	 * @param data The Data instance to interact with
	 */
	public CommentManager(Data data) {
		db = data;
		gson = new Gson();
		validator = new CommentValidator(data);
	}
	
	@Override
	public Comment makeEntity(Session s, String content) throws WPISuiteException {
		Comment newComment = gson.fromJson(content, Comment.class);
		
		List<ValidationIssue> issues = validator.validate(s, newComment);
		if(issues.size() > 0) {
			// TODO: pass errors to client through exception
			throw new BadRequestException();
		}
		
		Defect defect = validator.getLastExistingDefect();
		defect.getEvents().add(newComment);
		db.save(defect, s.getProject());
		db.save(defect.getEvents()); // TODO: remove this when we can change save depth

		return newComment;
	}

	@Override
	public Comment[] getEntity(Session s, String id) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public Comment[] getAll(Session s) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public Comment update(Session s, String content) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public void save(Session s, Comment model) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedGet(Session s, String[] args) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public void deleteAll(Session s) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public int Count() throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPut(Session s, String[] args, String content) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPost(Session s, String string, String content) throws NotImplementedException {
		throw new NotImplementedException();
	}

}

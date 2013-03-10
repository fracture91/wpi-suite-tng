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
 *    Mike Della Donna
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import java.util.Date;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectChangeset;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.validators.DefectValidator;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.validators.ValidationIssue;

/**
 * Provides database interaction for Defect models.
 */
public class DefectManager implements EntityManager<Defect> {

	Data db;
	DefectValidator validator;
	ModelMapper updateMapper;
	
	/**
	 * Create a DefectManager
	 * @param data The Data instance to use
	 */
	public DefectManager(Data data) {
		db = data;
		validator = new DefectValidator(db);
		updateMapper = new ModelMapper();
		updateMapper.getBlacklist().add("project"); // never allow project changing
	}

	@Override
	public Defect makeEntity(Session s, String content) throws WPISuiteException {
		final Defect newDefect = Defect.fromJSON(content);
		
		// TODO: increment properly, ensure uniqueness using ID generator.  This is a gross hack.
		newDefect.setId(Count() + 1);
		
		List<ValidationIssue> issues = validator.validate(s, newDefect, Mode.CREATE);
		if(issues.size() > 0) {
			// TODO: pass errors to client through exception
			for (ValidationIssue issue : issues) {
				System.out.println("Validation issue: " + issue.getMessage());
			}
			throw new BadRequestException();
		}

		if(!db.save(newDefect, s.getProject())) {
			throw new WPISuiteException();
		}
		return newDefect;
	}

	@Override
	public Defect[] getEntity(Session s, String id) throws NotFoundException {
		final int intId = Integer.parseInt(id);
		if(intId < 1) {
			throw new NotFoundException();
		}
		Defect[] defects = null;
		try {
			defects = db.retrieve(Defect.class, "id", intId, s.getProject()).toArray(new Defect[0]);
		} catch (WPISuiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(defects.length < 1 || defects[0] == null) {
			throw new NotFoundException();
		}
		return defects;
	}

	@Override
	public Defect[] getAll(Session s) {
		return db.retrieveAll(new Defect(), s.getProject()).toArray(new Defect[0]);
	}

	@Override
	public void save(Session s, Defect model) {
		db.save(model, s.getProject());
	}

	private void ensureRole(Session session, Role role) throws WPISuiteException {
		User user = (User) db.retrieve(User.class, "username", session.getUsername()).get(0);
		if(!user.getRole().equals(role)) {
			throw new UnauthorizedException();
		}
	}
	
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// TODO: are nested objects deleted?  Dates should be, but Users shouldn't!
		ensureRole(s, Role.ADMIN);
		return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
	}
	
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		ensureRole(s, Role.ADMIN);
		db.deleteAll(new Defect(), s.getProject());
	}
	
	@Override
	public int Count() {
		// TODO: there must be a faster way to do this with db4o
		// note that this is not project-specific - ids are unique across projects
		return db.retrieveAll(new Defect()).size();
	}

	@Override
	public Defect update(Session session, String content) throws WPISuiteException {
		Defect updatedDefect = Defect.fromJSON(content);
		
		List<ValidationIssue> issues = validator.validate(session, updatedDefect, Mode.EDIT);
		if(issues.size() > 0) {
			// TODO: pass errors to client through exception
			throw new BadRequestException();
		}

		/*
		 * Because of the disconnected objects problem in db4o, we can't just save updatedDefect.
		 * We have to get the original defect from db4o, copy properties from updatedDefect,
		 * then save the original defect again.
		 */
		Defect existingDefect = validator.getLastExistingDefect();
		Date originalLastModified = existingDefect.getLastModifiedDate();
		
		DefectChangeset changeset = new DefectChangeset();
		// core should make sure the session user exists
		// if this can't find the user, something's horribly wrong
		changeset.setUser((User) db.retrieve(User.class, "username", session.getUsername()).get(0));
		ChangesetCallback callback = new ChangesetCallback(changeset);
		
		// copy values to old defect and fill in our changeset appropriately
		updateMapper.map(updatedDefect, existingDefect, callback);
		
		if(changeset.getChanges().size() == 0) {
			// stupid user didn't even change anything!
			// don't bother saving to database, reset last modified date
			existingDefect.setLastModifiedDate(originalLastModified);
		} else {
			// add changeset to Defect events, save to database
			existingDefect.getEvents().add(changeset);
			// TODO: events field doesn't persist without explicit save - is this a bug?
			if(!db.save(existingDefect, session.getProject()) || !db.save(existingDefect.getEvents())) {
				throw new WPISuiteException();
			}
		}
		
		return existingDefect;
	}

	@Override
	public String advancedGet(Session arg0, String[] arg1) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPost(Session arg0, String arg1, String arg2) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPut(Session arg0, String[] arg1, String arg2) throws NotImplementedException {
		throw new NotImplementedException();
	}

}

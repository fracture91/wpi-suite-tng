package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.validators.DefectValidator;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.validators.ValidationIssue;

/**
 * Provides database interaction for Defect models.
 */
public class DefectManager implements EntityManager<Defect> {

	Data db;
	Gson gson;
	DefectValidator validator;
	
	/**
	 * Create a DefectManager
	 * @param data The Data instance to use
	 */
	public DefectManager(Data data) {
		db = data;
		gson = new Gson();
		validator = new DefectValidator(db);
	}

	@Override
	public Defect makeEntity(Session s, String content) throws WPISuiteException {
		final Defect newDefect = gson.fromJson(content, Defect.class);
		
		// TODO: increment properly, ensure uniqueness using ID generator.  This is a gross hack.
		newDefect.setId(Count() + 1);
		
		List<ValidationIssue> issues = validator.validate(newDefect, Mode.CREATE);
		if(issues.size() > 0) {
			// TODO: pass errors to client through exception
			throw new BadRequestException();
		}

		if(!db.save(newDefect)) {
			throw new WPISuiteException();
		}
		return newDefect;
	}

	@Override
	public Defect[] getEntity(Session s, String id) throws NotFoundException {
		if(id == null || id.equals("")) {
			// TODO: getAll should be called from the servlet directly
			return getAll(s);
		}
		final int intId = Integer.parseInt(id);
		if(intId < 1) {
			throw new NotFoundException();
		}
		final Defect[] defects = db.retrieve(Defect.class, "id", intId).toArray(new Defect[0]);
		if(defects.length < 1 || defects[0] == null) {
			throw new NotFoundException();
		}
		return defects;
	}

	@Override
	public Defect[] getAll(Session s) {
		return db.retrieveAll(new Defect()).toArray(new Defect[0]);
	}

	@Override
	public void save(Session s, Defect model) {
		// TODO: validate updates
		db.save(model);
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws NotFoundException {
		// TODO: are nested objects deleted?  Dates should be, but Users shouldn't!
		return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
	}
	
	@Override
	public void deleteAll(Session s) {
		db.deleteAll(new Defect());
	}
	
	@Override
	public int Count() {
		// TODO: there must be a faster way to do this with db4o
		return getAll(null).length;
	}

}

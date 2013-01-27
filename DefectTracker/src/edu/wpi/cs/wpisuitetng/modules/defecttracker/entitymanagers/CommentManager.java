package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;

public class CommentManager implements EntityManager<Comment> {

	private Data db;
	private Gson gson;
	private DefectManager defectManager;
	
	public CommentManager(Data data) {
		db = data;
		gson = new Gson();
		defectManager = new DefectManager(data);
	}
	
	@Override
	public Comment makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		Comment newComment = gson.fromJson(content, Comment.class);
		Defect[] defects = defectManager.getEntity(s, Integer.toString(newComment.getDefectId()));
		defects[0].getEvents().add(newComment);
		db.save(defects[0]);
		System.out.println("Created Comment");
		return newComment;
	}

	@Override
	public Comment[] getEntity(Session s, String id) throws NotFoundException,
			WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comment[] getAll(Session s) throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comment update(Session s, String content) throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Session s, Comment model) throws WPISuiteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int Count() throws WPISuiteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

}

package edu.wpi.cs.wpisuitetng.modules.postboard.model;

import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;

/**
 * This is the entity manager for the PostBoardMessage in the
 * PostBoard module.
 * 
 * @author Chris Casola
 *
 */
public class PostBoardEntityManager implements EntityManager<PostBoardMessage> {

	/** The database */
	Data db;

	
	/**
	 * Constructs the entity manager. This constructor is called by
	 * {@link edu.wpi.cs.wpisuitetng.ManagerLayer#ManagerLayer()}. To make sure
	 * this happens, be sure to place add this entity manager to the map in
	 * the ManagerLayer file.
	 * 
	 * @param db a reference to the persistent database
	 */
	public PostBoardEntityManager(Data db) {
		this.db = db;
	}

	/*
	 * Saves a PostBoardMessage when it is received from a client
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#makeEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public PostBoardMessage makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {

		// Parse the message from JSON
		final PostBoardMessage newMessage = PostBoardMessage.fromJson(content);

		// Save the message in the database if possible, otherwise throw an exception
		if (!db.save(newMessage)) {
			throw new WPISuiteException();
		}

		// Return the newly created message (this gets passed back to the client)
		return newMessage;
	}

	/*
	 * Individual messages cannot be retrieved. This message always throws an exception.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public PostBoardMessage[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {

		// If no specific id was requested (as is always the case with this module)
		// pass the request on to the getAll() method.
		if(id == null || id.equals("")) {
			return getAll(s);
		}

		// Throw an exception if an ID was specified, as this module does not support
		// retrieving specific PostBoardMessages.
		throw new WPISuiteException();
	}

	/* 
	 * Returns all of the messages that have been stored.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(edu.wpi.cs.wpisuitetng.Session)
	 */
	@Override
	public PostBoardMessage[] getAll(Session s) throws WPISuiteException {
		// Ask the database to retrieve all objects of the type PostBoardMessage.
		// Passing a dummy PostBoardMessage lets the db know what type of object to retrieve
		List<PostBoardMessage> messages = db.retrieveAll(new PostBoardMessage(null));

		// Return the list of messages as an array
		return messages.toArray(new PostBoardMessage[0]);
	}

	/*
	 * Message cannot be updated. This method always throws an exception.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public PostBoardMessage update(Session s, String content)
			throws WPISuiteException {

		// This module does not allow PostBoardMessages to be modified, so throw an exception
		throw new WPISuiteException();
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#save(edu.wpi.cs.wpisuitetng.Session, edu.wpi.cs.wpisuitetng.modules.Model)
	 */
	@Override
	public void save(Session s, PostBoardMessage model)
			throws WPISuiteException {

		// Save the given defect in the database
		db.save(model);
	}

	/*
	 * Messages cannot be deleted
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {

		// This module does not all PostBoardMessages to be deleted, so throw
		// an exception
		throw new WPISuiteException();
	}

	/*
	 * Messages cannot be deleted
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteAll(edu.wpi.cs.wpisuitetng.Session)
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {

		// This module does not all PostBoardMessages to be deleted, so throw
		// an exception
		throw new WPISuiteException();
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#Count()
	 */
	@Override
	public int Count() throws WPISuiteException {
		// Return the number of PostBoardMessages currently in the database
		return getAll(null).length;
	}

}

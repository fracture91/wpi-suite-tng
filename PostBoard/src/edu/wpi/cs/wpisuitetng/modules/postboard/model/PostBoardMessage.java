/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Chris Casola
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.postboard.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * Model to contain a single message on the PostBoard
 * 
 * @author Chris Casola
 *
 */
public class PostBoardMessage extends AbstractModel {

	/** The message */
	private final String message;

	/** The date-time stamp */
	private final Date date;

	/**
	 * Constructs a PostBoardMessage for the given string message
	 * @param message
	 */
	public PostBoardMessage(String message) {
		this.message = message;
		date = new Date();
	}

	/**
	 * Returns a JSON-encoded string representation of this message object
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, PostBoardMessage.class);
	}

	/**
	 * Returns an instance of PostBoardMessage constructed using the given
	 * PostBoardMessage encoded as a JSON string.
	 * 
	 * @param json the json-encoded PostBoardMessage to deserialize
	 * @return the PostBoardMessage contained in the given JSON
	 */
	public static PostBoardMessage fromJson(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, PostBoardMessage.class);
	}
	
	/**
	 * Returns an array of PostBoardMessage parsed from the given JSON-encoded
	 * string.
	 * 
	 * @param json a string containing a JSON-encoded array of PostBoardMessage
	 * @return an array of PostBoardMessage deserialzied from the given json string
	 */
	public static PostBoardMessage[] fromJsonArray(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, PostBoardMessage[].class);
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// Format the date-time stamp
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm a");
		
		return dateFormat.format(date) + ":    " + message;
	}

	/*
	 * The methods below are required by the model interface, however they
	 * do not need to be implemented for a basic model like PostBoardMessage. 
	 */

	@Override
	public void save() {}

	@Override
	public void delete() {}

	@Override
	public Boolean identify(Object o) {return null;}

}

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
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.postboard.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.postboard.model.PostBoardMessage;
import edu.wpi.cs.wpisuitetng.modules.postboard.model.PostBoardModel;
import edu.wpi.cs.wpisuitetng.modules.postboard.view.BoardPanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This controller responds when the user clicks the Submit button by
 * adding the contents of the message text field to the model as a new
 * message.
 * 
 * @author Chris Casola
 *
 */
public class AddMessageController implements ActionListener {
	
	private final PostBoardModel model;
	private final BoardPanel view;
	
	/**
	 * Construct an AddMessageController for the given model, view pair
	 * @param model the model containing the messages
	 * @param view the view where the user enters new messages
	 */
	public AddMessageController(PostBoardModel model, BoardPanel view) {
		this.model = model;
		this.view = view;
	}

	/* 
	 * This method is called when the user clicks the Submit button
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		// Get the text that was entered
		String message = view.getTxtNewMessage().getText();
		
		// Make sure there is text
		if (message.length() > 0) {
			// Clear the text field
			view.getTxtNewMessage().setText("");
			
			// Send a request to the core to save this message
			final Request request = Network.getInstance().makeRequest("postboard/postboardmessage", HttpMethod.PUT); // PUT == create
			request.setBody(new PostBoardMessage(message).toJSON()); // put the new message in the body of the request
			request.addObserver(new AddMessageRequestObserver(this)); // add an observer to process the response
			request.send(); // send the request
		}
	}

	/**
	 * When the new message is received back from the server, add it to the local model.
	 * @param message
	 */
	public void addMessageToModel(PostBoardMessage message) {
		model.addMessage(message);
	}
}

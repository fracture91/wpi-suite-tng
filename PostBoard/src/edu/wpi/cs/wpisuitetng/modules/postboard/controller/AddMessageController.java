/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.postboard.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.postboard.model.PostBoardModel;
import edu.wpi.cs.wpisuitetng.modules.postboard.view.BoardPanel;

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
			// Add the message to the model
			model.addMessage(message);
			
			// Clear the text field
			view.getTxtNewMessage().setText("");
		}
	}

}

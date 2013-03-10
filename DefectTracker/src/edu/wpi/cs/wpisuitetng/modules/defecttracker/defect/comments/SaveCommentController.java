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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.comments;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * This controller handles saving defect comments to the server
 */
public class SaveCommentController {

	private final NewCommentPanel view;
	private final Defect model;
	private final DefectPanel parentView;

	/**
	 * Construct the controller
	 * @param view the NewCommentPanel containing the comment field
	 * @param model the Defect model being commented on
	 * @param parentView the DefectPanel displaying the defect
	 */
	public SaveCommentController(NewCommentPanel view, Defect model, DefectPanel parentView) {
		this.view = view;
		this.model = model;
		this.parentView = parentView;
	}
	
	/**
	 * Save a comment to the server
	 */
	public void saveComment() {
		final String commentText = view.getCommentField().getText();
		if (commentText.length() > 0) {
			final RequestObserver requestObserver = new SaveCommentObserver(this);
			final Request request = Network.getInstance().makeRequest(
					"defecttracker/comment", HttpMethod.PUT);
			final Comment comment = new Comment(model.getId(), model.getCreator(), view.getCommentField().getText());
			view.getCommentField().setText("");
			request.setBody(comment.toJSON());
			request.addObserver(requestObserver);
			request.send();
		}
	}
	
	/**
	 * Add the comment to the view if the server responded with a success message
	 * @param response the response from the server
	 */
	public void success(final ResponseModel response) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				parentView.getDefectEventListModel().addElement(Comment.fromJson(response.getBody()));
			}
		});
	}
	
	/**
	 * Alert the user that an error occurred sending the comment to the server
	 * @param response the response from the server
	 */
	public void failure(ResponseModel response) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(parentView, "An error occurred sending your comment to the server.", "Error sending comment!", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}

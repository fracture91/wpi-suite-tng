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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.comments;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Action that calls {@link edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.comments.SaveCommentController#saveComment()}
 */
@SuppressWarnings("serial")
public class SaveCommentAction extends AbstractAction {
	
	public final SaveCommentController controller;

	/**
	 * Construct the action
	 * @param controller the controller to trigger
	 */
	public SaveCommentAction(SaveCommentController controller) {
		super("Add Comment");
		this.controller = controller;
	}
	
	
	/*
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.saveComment();
	}

}

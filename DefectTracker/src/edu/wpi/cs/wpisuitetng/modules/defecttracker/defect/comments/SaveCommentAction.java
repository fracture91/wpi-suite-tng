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

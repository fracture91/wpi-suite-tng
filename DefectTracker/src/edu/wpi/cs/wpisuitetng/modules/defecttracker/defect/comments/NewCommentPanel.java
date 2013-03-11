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

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;

/**
 * A panel containing a form for adding a new comment to a defect
 */
@SuppressWarnings("serial")
public class NewCommentPanel extends JPanel {

	
	private final JTextArea commentField;
	private final JButton addComment;
	private final JLabel addCommentLabel;

	private static final int VERTICAL_PADDING = 5;
	private static final int COMMENT_FIELD_HEIGHT = 50;

	/**
	 * Construct the panel, add and layout components.
	 * @param model the defect model
	 * @param parentView the parent view
	 */
	public NewCommentPanel(Defect model, DefectPanel parentView) {
		commentField = new JTextArea();
		commentField.setLineWrap(true);
		commentField.setWrapStyleWord(true);
		commentField.setBorder((new JTextField()).getBorder());

		addComment = new JButton("Add Comment");
		addCommentLabel = new JLabel("Add a new comment:");
		
		addComment.setAction(new SaveCommentAction(new SaveCommentController(this, model, parentView)));
		
		this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		
		final JScrollPane commentFieldPane = new JScrollPane(commentField);
		
		layout.putConstraint(SpringLayout.NORTH, addCommentLabel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, addCommentLabel, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, commentFieldPane, VERTICAL_PADDING, SpringLayout.SOUTH, addCommentLabel);
		layout.putConstraint(SpringLayout.WEST, commentFieldPane, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, commentFieldPane, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, commentFieldPane, COMMENT_FIELD_HEIGHT, SpringLayout.NORTH, commentFieldPane);
		layout.putConstraint(SpringLayout.NORTH, addComment, VERTICAL_PADDING, SpringLayout.SOUTH, commentFieldPane);
		layout.putConstraint(SpringLayout.EAST, addComment, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, this, VERTICAL_PADDING, SpringLayout.SOUTH, addComment);
		
		this.add(addCommentLabel);
		this.add(commentFieldPane);
		this.add(addComment);
	}
	
	/**
	 * @return the comment JTextArea
	 */
	public JTextArea getCommentField() {
		return commentField;
	}
	
	/**
	 * Enables and disables input on this panel.
	 * @param value if value is true, input is enabled, otherwise input is disabled.
	 */
	public void setInputEnabled(boolean value) {
		commentField.setEnabled(value);
		addComment.setEnabled(value);
		if (value) {
			addCommentLabel.setForeground(Color.black);
		}
		else {
			addCommentLabel.setForeground(Color.gray);
		}
	}
}

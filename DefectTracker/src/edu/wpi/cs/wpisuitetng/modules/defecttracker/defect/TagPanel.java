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
 *    JPage
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Tag;

/**
 * Panel to manage adding and removing tags
 *
 */
@SuppressWarnings("serial")
public class TagPanel extends JPanel {

	protected JTextField txtNewTag;
	protected DefaultListModel lmTags;
	protected JList lstTags;
	protected JButton btnAddTag;
	protected JButton btnRemoveTag;

	private final Defect model;

	private Border defaultBorder;

	protected boolean inputEnabled;

	protected static final int HORIZONTAL_PADDING = 5;
	protected static final int VERTICAL_PADDING = 15;
	protected static final int LABEL_ALIGNMENT = JLabel.TRAILING;

	/**
	 * Creates a new TagPanel.
	 * 
	 * @param defect	The Defect to use to populate the Tag list and to which the Tag list will be compared.
	 */
	protected TagPanel(Defect defect) {
		inputEnabled = true;

		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		this.setBorder(BorderFactory.createTitledBorder("Tags"));

		this.model = defect;

		addComponents(layout);

		// Populate the list of tags
		updateFields();

		addEventListeners();
	}

	/**
	 * Adds the components to the panel and places constraints on them
	 * for SpringLayout.
	 * @param layout the layout manager
	 */
	protected void addComponents(SpringLayout layout) {
		txtNewTag = new JTextField(20);
		lmTags = new DefaultListModel();
		lstTags = new JList(lmTags);
		lstTags.setBorder(txtNewTag.getBorder());
		btnAddTag = new JButton("Add");
		btnRemoveTag = new JButton("Remove");

		defaultBorder = lstTags.getBorder();

		JLabel lblNewTag = new JLabel("Enter a new tag:");
		int labelWidth = lblNewTag.getPreferredSize().width;
		
		layout.putConstraint(SpringLayout.NORTH, lblNewTag, VERTICAL_PADDING, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, lblNewTag, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, lblNewTag, labelWidth, SpringLayout.WEST, lblNewTag);
		layout.putConstraint(SpringLayout.WEST, txtNewTag, HORIZONTAL_PADDING, SpringLayout.EAST, lblNewTag);

		layout.putConstraint(SpringLayout.NORTH, txtNewTag, VERTICAL_PADDING, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, lstTags, VERTICAL_PADDING, SpringLayout.SOUTH, txtNewTag);

		layout.putConstraint(SpringLayout.VERTICAL_CENTER, btnAddTag, 0, SpringLayout.VERTICAL_CENTER, txtNewTag);
		layout.putConstraint(SpringLayout.WEST, btnAddTag, HORIZONTAL_PADDING * 5, SpringLayout.EAST, txtNewTag);

		layout.putConstraint(SpringLayout.NORTH, lstTags, VERTICAL_PADDING, SpringLayout.SOUTH, txtNewTag);
		layout.putConstraint(SpringLayout.WEST, lstTags, 0, SpringLayout.WEST, txtNewTag);
		layout.putConstraint(SpringLayout.EAST, lstTags, 0, SpringLayout.EAST, txtNewTag);
		layout.putConstraint(SpringLayout.SOUTH, lstTags, txtNewTag.getPreferredSize().height * 6, SpringLayout.NORTH, lstTags);

		layout.putConstraint(SpringLayout.WEST, btnRemoveTag, 0, SpringLayout.WEST, btnAddTag);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, btnRemoveTag, 0, SpringLayout.VERTICAL_CENTER, lstTags);
		layout.putConstraint(SpringLayout.EAST, btnAddTag, 0, SpringLayout.EAST, btnRemoveTag);

		layout.putConstraint(SpringLayout.SOUTH, this, 15, SpringLayout.SOUTH, lstTags);

		add(lblNewTag);
		add(txtNewTag);
		add(btnAddTag);
		add(lstTags);
		add(btnRemoveTag);
	}

	/**
	 * Checks if the Tags in this TagPanel differ from the model and highlights the Tag list accordingly.
	 */
	protected void checkIfUpdated() {
		lstTags.setBackground(Color.WHITE);
		lstTags.setBorder(defaultBorder);

		if (lmTags.size() == model.getTags().size()) {
			for (Tag tag : model.getTags()) {
				if (!lmTags.contains(tag.getName())) {
					lstTags.setBackground(new Color(243, 243, 209));
					lstTags.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
					break;
				}
			}
		}
		else {
			lstTags.setBackground(new Color(243, 243, 209));
			lstTags.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}
	}

	/**
	 * Adds event listeners to the buttons
	 */
	protected void addEventListeners() {

		// Listener for btnAddTag
		btnAddTag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (txtNewTag.getText().length() > 0) {
					lmTags.addElement(txtNewTag.getText());
					txtNewTag.setText("");

					checkIfUpdated();
				}
			}
		});

		// Listener for btnRemoveTag
		btnRemoveTag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = lstTags.getSelectedIndex();
				if (index > -1) {
					lmTags.removeElementAt(index);

					checkIfUpdated();
				}
			}
		});
	}

	/**
	 * Sets whether input is enabled for this panel and its children. This should be used instead of 
	 * JComponent#setEnabled because setEnabled does not affect its children.
	 * 
	 * @param enabled	Whether or not input is enabled.
	 */
	public void setInputEnabled(boolean enabled) {
		inputEnabled = enabled;

		txtNewTag.setEnabled(enabled);
		lstTags.setEnabled(enabled);
		btnAddTag.setEnabled(enabled);
		btnRemoveTag.setEnabled(enabled);
	}

	/**
	 * Returns a boolean representing whether or not input is enabled for the TagPanel and its children.
	 * 
	 * @return	A boolean representing whether or not input is enabled for the TagPanel and its children.
	 */
	public boolean getInputEnabled() {
		return inputEnabled;
	}

	/**
	 * Updates the TagPanel's model to contain the values of the given Defect.
	 * 
	 * @param	defect	The Defect which contains the new values for the model.
	 */
	protected void updateModel(Defect defect) {
		model.setId(defect.getId());
		model.setTitle(defect.getTitle());
		model.setDescription(defect.getDescription());
		model.setAssignee(defect.getAssignee());
		model.setCreator(defect.getCreator());
		model.setCreationDate(defect.getCreationDate());
		model.setLastModifiedDate(defect.getLastModifiedDate());
		model.setStatus(defect.getStatus());
		model.setTags(defect.getTags());

		updateFields();
	}

	/**
	 * Updates the TagPanel's fields to match those in the current model.
	 */
	private void updateFields() {
		if (model.getTags() != null) {
			lmTags.clear();
			Iterator<Tag> tagsI = model.getTags().iterator();
			Tag nextTag;
			while (tagsI.hasNext()) {
				nextTag = tagsI.next();
				lmTags.addElement(nextTag.getName());
			}
		}

		checkIfUpdated();
	}
}

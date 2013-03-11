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

package edu.wpi.cs.wpisuitetng.janeway.gui.login;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;

/**
 * The login form for the Janeway login window
 *
 */
@SuppressWarnings("serial")
public class LoginFieldPanel extends JPanel {

	/** The username field */
	protected JTextField txtUsername;

	/** The password field */
	protected JPasswordField txtPassword;

	/** The project field */
	protected JTextField txtProject;

	/** The URL field */
	protected JTextField txtUrl;

	/** The layout manager for this panel */
	protected final SpringLayout layout;

	/** The width of each label */
	protected int labelWidth = 75;

	/** The vertical padding of components */
	protected static final int VERTICAL_PADDING = 10;

	/** The horizontal padding of components */
	protected static final int HORIZONTAL_PADDING = 5;

	/** The alignment of labels */
	protected static final int LABEL_ALIGNMENT = JLabel.TRAILING;

	/**
	 * Constructs a new LoginFieldPanel that contains labels
	 * and fields for the login form.
	 */
	public LoginFieldPanel() {

		// Use the SpringLayout layout manager
		layout = new SpringLayout();
		this.setLayout(layout);

		// Add the components to the panel
		addComponents();
	}

	/**
	 * Constructs all the components, creates constraints for the layout manager, and
	 * adds the components to the panel
	 */
	public void addComponents() {
		// Construct the labels
		JLabel lblUsername = new JLabel("Username:", LABEL_ALIGNMENT);
		JLabel lblPassword = new JLabel("Password:", LABEL_ALIGNMENT);
		JLabel lblProject = new JLabel("Project:", LABEL_ALIGNMENT);
		JLabel lblUrl = new JLabel("Server URL:", LABEL_ALIGNMENT);
		
		// Set the label width to the width of the longest label
		// This is necessary because fonts vary across platforms
		labelWidth = lblUrl.getPreferredSize().width;

		// Construct the text fields
		txtUsername = new JTextField(15);
		txtPassword = new JPasswordField(15);
		txtProject = new JTextField(15);
		txtUrl = new JTextField(50);
		
		// Load saved field values
		txtUsername.setText(ConfigManager.getConfig().getUserName());
		txtUrl.setText(ConfigManager.getConfig().getCoreUrl().toString());
		txtProject.setText(ConfigManager.getConfig().getProjectName());

		// Change font of text fields
		Font txtFont = txtUsername.getFont().deriveFont(14f);
		txtUsername.setFont(txtFont);
		txtProject.setFont(txtFont);
		txtUrl.setFont(txtFont);
		txtPassword.setFont(txtFont);

		// Constrain user name label/field
		layout.putConstraint(SpringLayout.WEST, lblUsername, HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, lblUsername, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, lblUsername, labelWidth, SpringLayout.WEST, lblUsername);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, txtUsername, 0, SpringLayout.VERTICAL_CENTER, lblUsername);
		layout.putConstraint(SpringLayout.WEST, txtUsername, HORIZONTAL_PADDING, SpringLayout.EAST, lblUsername);
		layout.putConstraint(SpringLayout.EAST, this, HORIZONTAL_PADDING, SpringLayout.EAST, txtUsername);

		// Constrain password label/field
		layout.putConstraint(SpringLayout.NORTH, txtPassword, VERTICAL_PADDING, SpringLayout.SOUTH, txtUsername);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblPassword, 0, SpringLayout.VERTICAL_CENTER, txtPassword);
		layout.putConstraint(SpringLayout.WEST, lblPassword, HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, lblPassword, labelWidth, SpringLayout.WEST, lblPassword);
		layout.putConstraint(SpringLayout.WEST, txtPassword, HORIZONTAL_PADDING, SpringLayout.EAST, lblPassword);
		layout.putConstraint(SpringLayout.EAST, txtPassword, 0, SpringLayout.EAST, txtUsername);

		// Constrain project label/field
		layout.putConstraint(SpringLayout.NORTH, txtProject, VERTICAL_PADDING * 3, SpringLayout.SOUTH, txtPassword);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblProject, 0, SpringLayout.VERTICAL_CENTER, txtProject);
		layout.putConstraint(SpringLayout.WEST, lblProject, HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, lblProject, labelWidth, SpringLayout.WEST, lblProject);
		layout.putConstraint(SpringLayout.WEST, txtProject, HORIZONTAL_PADDING, SpringLayout.EAST, lblProject);
		layout.putConstraint(SpringLayout.EAST, txtProject, 0, SpringLayout.EAST, txtUsername);

		// Constrain server url label/field
		layout.putConstraint(SpringLayout.NORTH, txtUrl, VERTICAL_PADDING * 3, SpringLayout.SOUTH, txtProject);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblUrl, 0, SpringLayout.VERTICAL_CENTER, txtUrl);
		layout.putConstraint(SpringLayout.WEST, lblUrl, HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, lblUrl, labelWidth, SpringLayout.WEST, lblUrl);
		layout.putConstraint(SpringLayout.WEST, txtUrl, HORIZONTAL_PADDING, SpringLayout.EAST, lblUrl);
		layout.putConstraint(SpringLayout.EAST, txtUrl, 0, SpringLayout.EAST, txtUsername);

		// Constrain the south edge of the panel to the south edge of the last text field
		layout.putConstraint(SpringLayout.SOUTH, this, 5, SpringLayout.SOUTH, txtUrl);

		// Add components
		add(lblUsername);
		add(txtUsername);
		add(lblPassword);
		add(txtPassword);
		add(lblProject);
		add(txtProject);
		add(lblUrl);
		add(txtUrl);

		validate();
	}
}

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

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The header panel for the Janeway login frame
 *
 */
@SuppressWarnings("serial")
public class LoginHeaderPanel extends JPanel {
	
	/**
	 * Constructs a new LoginHeaderPanel containing a main
	 * label and sub label.
	 */
	public LoginHeaderPanel() {
		
		// Use the BoxLayout layout manager
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		
		// Add the main label
		JLabel mainLabel = new JLabel("Janeway Client for WPI Suite");
		mainLabel.setFont(mainLabel.getFont().deriveFont(20f));
		mainLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(mainLabel);
		
		// Add spacing between the labels
		add(Box.createRigidArea(new Dimension(0, 20)));
		
		// Add the sub label
		JLabel subLabel = new JLabel("Login");
		subLabel.setFont(subLabel.getFont().deriveFont(16f));
		subLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(subLabel);
		
		validate();
	}
}

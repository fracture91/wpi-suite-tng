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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.search.views;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel to contain the filter builder for defect searching
 */
@SuppressWarnings("serial")
public class FilterBuilderPanel extends JPanel {

	/**
	 * Construct the panel
	 */
	public FilterBuilderPanel() {
		// Set the layout of the panel and give it a border
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(BorderFactory.createTitledBorder("Filter Builder"));
		
		// TODO implement the rest of the controls for building
		this.add(Box.createRigidArea(new Dimension(30, 0)));
		this.add(new JLabel("Filter builder will go here."));
	}
}

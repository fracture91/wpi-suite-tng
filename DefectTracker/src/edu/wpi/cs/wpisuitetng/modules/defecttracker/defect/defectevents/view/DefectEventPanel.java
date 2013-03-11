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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.defectevents.view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DefectEventPanel extends JPanel {
	
	protected JLabel title;
	protected JLabel content;

	public DefectEventPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		title = new JLabel("Title of Defect Event");
		title.setFont(title.getFont().deriveFont(9));
		title.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		content = new JLabel("Content goes here.");
		content.setFont(content.getFont().deriveFont(9));
		content.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		this.add(title);
		this.add(content);
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0), BorderFactory.createLineBorder(Color.black, 1)), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
	}
	
	protected void setTitle(String title) {
		this.title.setText(title);
	}
	
	protected void setContent(String content) {
		this.content.setText(content);
	}
}

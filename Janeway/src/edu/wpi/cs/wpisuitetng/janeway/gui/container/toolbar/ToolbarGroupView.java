/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.Hoverable;

/**
 * Holds a view for a group of controls on the toolbar.
 * The group has a name, which is displayed in a label on the bottom, and content, which is displayed above.
 * Clients should make sure to set preferred/minimum/maximum sizes appropriately in order to
 * best fit their contents.
 */
@SuppressWarnings("serial")
public class ToolbarGroupView extends JPanel implements Hoverable {
	
	private JLabel label;
	private JPanel content;
	private static final int DEFAULT_WIDTH = 300;
	
	/**
	 * Construct a group with the given name
	 * @param name The name to use for the group and display on the bottom.
	 */
	public ToolbarGroupView(String name) {
		setLayout(new BorderLayout());
		setOpaque(false);
		content = new JPanel();
		content.setLayout(new FlowLayout());
		content.setOpaque(false);
		label = new JLabel();
		label.setHorizontalAlignment(JLabel.CENTER);		
		setName(name);
		add(content, BorderLayout.CENTER);
		add(label, BorderLayout.SOUTH);
		setPreferredWidth(DEFAULT_WIDTH); // default, should be changed
		setMaximumSize(new Dimension(1, Integer.MAX_VALUE)); // don't stretch horizontally
		setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
		this.addMouseListener(new MouseHoverListener(this));
	}
	
	/**
	 * Construct a group with the given name and the given content panel
	 * @param name the name to use for the group and display on the bottom
	 * @param content a custom content panel to use for the group
	 */
	public ToolbarGroupView(String name, JPanel content) {
		this(name);
		this.remove(content);
		this.add(content, BorderLayout.CENTER);
	}
	
	/**
	 * Set the preferred width of the group.
	 * @param width Width to set to
	 */
	public void setPreferredWidth(int width) {
		// height is ignored in the layout we're using, abstracting that away 
		setPreferredSize(new Dimension(width, -1));
	}
	
	/**
	 * Set the displayed name
	 */
	public void setName(String name) {
		super.setName(name);
		label.setText(name);
	}
	
	/**
	 * @return The JPanel holding the content of this group - controls should be added in here
	 */
	public JPanel getContent() {
		return content;
	}

	/**
	 * Called when the mouse enters this toolbar group
	 */
	@Override
	public void mouseEntered() {
		this.setOpaque(true);
		this.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.gray));
		this.repaint();
	}

	/**
	 * Called when the mouse exits this toolbar group
	 */
	@Override
	public void mouseExited() {
		this.setOpaque(false);
		this.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
		this.repaint();
	}
	
}

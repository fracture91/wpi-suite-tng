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

package edu.wpi.cs.wpisuitetng.modules.postboard.view;

import javax.swing.JToolBar;

import edu.wpi.cs.wpisuitetng.modules.postboard.model.PostBoardModel;

/**
 * This is the toolbar for the PostBoard module
 * 
 * @author Chris Casola
 * 
 */
@SuppressWarnings("serial")
public class ToolbarView extends JToolBar {
	
	/** The panel containing toolbar buttons */
	private final ToolbarPanel toolbarPanel;

	/**
	 * Construct this view and all components in it.
	 * @param boardModel 
	 */
	public ToolbarView(PostBoardModel boardModel) {
		
		// Prevent this toolbar from being moved
		setFloatable(false);
		
		// Add the panel containing the toolbar buttons
		toolbarPanel = new ToolbarPanel(boardModel);
		add(toolbarPanel);
	}
}

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

package edu.wpi.cs.wpisuitetng.modules.postboard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.postboard.model.PostBoardModel;
import edu.wpi.cs.wpisuitetng.modules.postboard.view.MainView;
import edu.wpi.cs.wpisuitetng.modules.postboard.view.ToolbarView;

/**
 * This is a module for WPISuiteTNG that provides a post board. A post
 * board is simply a place where messages can be posted that are viewable
 * by all users. Think of it as a cork board on your wall, but only for text.
 * 
 * @author Chris Casola
 *
 */
public class PostBoard implements IJanewayModule {
	
	/**
	 * A list of tabs owned by this module
	 */
	List<JanewayTabModel> tabs;
	
	/**
	 * Constructs the main views for this module. Namely one tab, containing
	 * a toolbar and a main content panel.
	 */
	public PostBoard() {

		// Initialize the model that holds the messages
		PostBoardModel boardModel = new PostBoardModel();
		
		// Initialize the list of tabs (however, this module has only one tab)
		tabs = new ArrayList<JanewayTabModel>();
		
		// Create a JPanel to hold the toolbar for the tab
		ToolbarView toolbarView = new ToolbarView(boardModel);
		
		// Constructs and adds the MainPanel
		MainView mainView = new MainView(boardModel);
		
		// Create a tab model that contains the toolbar panel and the main content panel
		JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), toolbarView, mainView);
		
		// Add the tab to the list of tabs owned by this module
		tabs.add(tab1);
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "PostBoard";
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}

}

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
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.search.views;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.controllers.RefreshDefectsAction;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.controllers.RetrieveAllDefectsController;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.Tab;

/**
 * View that contains the entire defect searching interface
 */
@SuppressWarnings("serial")
public class SearchDefectsView extends JPanel implements IToolbarGroupProvider {
	
	/** Panel containing the search interface */
	protected SearchPanel mainPanel;
	
	/** The layout manager for this panel */
	protected SpringLayout layout;
	
	/** The panel containing buttons for the toolbar */
	protected ToolbarGroupView buttonGroup;
	
	/** The refresh button that reloads the results of the search/filter */
	protected JButton btnRefresh;
	
	/** Controller to handle search and filter requests from the user */
	protected RetrieveAllDefectsController controller;
	
	/** The main tab controller */
	protected MainTabController tabController;
	
	/**
	 * Construct the view
	 * @param tabController The main tab controller
	 * @param tab The Tab containing this view
	 */
	public SearchDefectsView(MainTabController tabController, Tab tab) {
		this.tabController = tabController;
		
		if(tab != null) {
			tab.setTitle("Search Defects");
			tab.setIcon(new ImageIcon());
			tab.setToolTipText("Search for defects");
		}
		
		mainPanel = new SearchPanel(tabController);
		
		// Construct the layout manager and add constraints
		layout = new SpringLayout();
		this.setLayout(layout);
		layout.putConstraint(SpringLayout.NORTH, mainPanel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, mainPanel, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.WEST, mainPanel, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, mainPanel, 0, SpringLayout.EAST, this);
		
		// Add the mainPanel to this view
		this.add(mainPanel);
		
		// Instantiate the controller
		controller = new RetrieveAllDefectsController(this);
		
		// Instantiate the button panel
		buttonGroup = new ToolbarGroupView("Search/Filter");
		
		// Instantiate the refresh button
		btnRefresh = new JButton();
		btnRefresh.setAction(new RefreshDefectsAction(controller));
		buttonGroup.getContent().add(btnRefresh);
		buttonGroup.setPreferredWidth(150);
		
		// Load initial data
		controller.refreshData();
	}
	
	public SearchPanel getSearchPanel() {
		return mainPanel;
	}

	@Override
	public ToolbarGroupView getGroup() {
		return buttonGroup;
	}
}

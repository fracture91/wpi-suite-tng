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

import java.awt.BorderLayout;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.controllers.RetrieveDefectController;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.models.ResultsTableModel;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabController;

/**
 * Panel to hold the results of a search for defects
 */
@SuppressWarnings("serial")
public class ResultsPanel extends JPanel {
	
	/** The table of results */
	protected JTable resultsTable;
	
	/** The model containing the data to be displayed in the results table */
	protected ResultsTableModel resultsTableModel;
	
	/** The main tab controller */
	protected final MainTabController tabController;

	/**
	 * Construct the panel
	 * @param tabController The main tab controller
	 */
	public ResultsPanel(MainTabController tabController) {
		this.tabController = tabController;
		
		// Set the layout
		this.setLayout(new BorderLayout());
		
		// Construct the table model
		resultsTableModel = new ResultsTableModel();
		
		// Construct the table and configure it
		resultsTable = new JTable(resultsTableModel);
		resultsTable.setAutoCreateRowSorter(true);
		resultsTable.setFillsViewportHeight(true);
		resultsTable.setDefaultRenderer(Date.class, new DateTableCellRenderer());
		
		// Add a listener for row clicks
		resultsTable.addMouseListener(new RetrieveDefectController(this));
		
		// Put the table in a scroll pane
		JScrollPane resultsScrollPane = new JScrollPane(resultsTable);
		
		this.add(resultsScrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * @return the main tab controller
	 */
	public MainTabController getTabController() {
		return tabController;
	}
	
	/**
	 * @return the data model for the table
	 */
	public ResultsTableModel getModel() {
		return resultsTableModel;
	}
	
	/**
	 * @return the results table
	 */
	public JTable getResultsTable() {
		return resultsTable;
	}
	
	/**
	 * Replace the results table with the given table
	 * @param newTable the new results table
	 */
	public void setResultsTable(JTable newTable) {
		this.resultsTable = newTable;
	}
}

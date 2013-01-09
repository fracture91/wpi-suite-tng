package edu.wpi.cs.wpisuitetng.modules.defecttracker.search;

import java.awt.BorderLayout;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Panel to hold the results of a search for defects
 */
@SuppressWarnings("serial")
public class ResultsPanel extends JPanel {
	
	/** The table of results */
	protected JTable resultsTable;
	
	/** The model containing the data to be displayed in the results table */
	protected ResultsTableModel resultsTableModel;

	/**
	 * Construct the panel
	 */
	public ResultsPanel() {
		// Set the layout
		this.setLayout(new BorderLayout());
		
		// Construct the table model
		resultsTableModel = new ResultsTableModel();
		
		// Construct the table and configure it
		resultsTable = new JTable(resultsTableModel);
		resultsTable.setAutoCreateRowSorter(true);
		resultsTable.setFillsViewportHeight(true);
		resultsTable.setDefaultRenderer(Date.class, new DateTableCellRenderer());
		JScrollPane resultsScrollPane = new JScrollPane(resultsTable);
		
		this.add(resultsScrollPane, BorderLayout.CENTER);
	}
	
	public ResultsTableModel getModel() {
		return resultsTableModel;
	}
}

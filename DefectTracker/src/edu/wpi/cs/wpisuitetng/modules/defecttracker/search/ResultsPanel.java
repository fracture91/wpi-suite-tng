package edu.wpi.cs.wpisuitetng.modules.defecttracker.search;

import java.awt.BorderLayout;

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

	/**
	 * Construct the panel
	 */
	public ResultsPanel() {
		// Set the layout
		this.setLayout(new BorderLayout());
		
		// Construct the table and configure it
		resultsTable = new JTable(new ResultsTableModel());
		resultsTable.setAutoCreateRowSorter(true);
		resultsTable.setFillsViewportHeight(true);
		JScrollPane resultsScrollPane = new JScrollPane(resultsTable);
		
		this.add(resultsScrollPane, BorderLayout.CENTER);
	}
}

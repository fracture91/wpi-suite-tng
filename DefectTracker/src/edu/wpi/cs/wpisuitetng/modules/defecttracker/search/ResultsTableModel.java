package edu.wpi.cs.wpisuitetng.modules.defecttracker.search;

import javax.swing.table.AbstractTableModel;

/**
 * A model to manage the data displayed in the {@link ResultsPanel}
 * 
 * TODO Actually make requests to get this data from the core
 */
@SuppressWarnings("serial")
public class ResultsTableModel extends AbstractTableModel {

	/** The names to be displayed in the column headers */
	String[] columnNames = {"Title", "Creator", "Assignee"};
	
	/** The data to be displayed in the table */
	Object[][] data = {
			{"Defect 1", "Chris Casola", "Andrew"},
			{"Defect 2", "Chris Page", "Chris Casola"},
			{"Defect 3", "Andrew", "Chris Page"},
	};
	
	/**
	 * Construct the model and populate it with data
	 */
	public ResultsTableModel() {
		
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}
}

package edu.wpi.cs.wpisuitetng.modules.defecttracker.search.controllers;

import java.awt.Point;

import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.models.ResultsTableModel;

@SuppressWarnings("serial")
public class MockJTable extends JTable {

	public MockJTable(ResultsTableModel model) {
		super(model);
	}

	@Override
	public int rowAtPoint(Point p) {
		return new Double(p.getX()).intValue();
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		return this.getModel().getValueAt(row, col);
	}
}

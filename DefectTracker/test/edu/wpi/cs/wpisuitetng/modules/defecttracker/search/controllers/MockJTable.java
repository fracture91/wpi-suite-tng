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

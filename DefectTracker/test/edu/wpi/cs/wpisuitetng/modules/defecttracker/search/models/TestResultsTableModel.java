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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.search.models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the ResultsTableModel that contains the data to be
 * displayed in the defects JTable.
 */
public class TestResultsTableModel {

	ResultsTableModel rtm1;

	@Before
	public void setUp() throws Exception {
		rtm1 = new ResultsTableModel();
	}

	@Test
	public void dataAndHeaderFieldsInitialized() {
		assertTrue(rtm1.columnNames != null);
		assertTrue(rtm1.data != null);
		assertEquals(0, rtm1.columnNames.length);
		assertEquals(0, rtm1.data.length);
	}

	@Test
	public void columnNamesCanBeSet() {
		String[] columnNames = {"Col A", "Col B", "Col C"};
		rtm1.setColumnNames(columnNames);

		assertEquals(3, rtm1.getColumnCount());
		assertEquals("Col B", rtm1.getColumnName(1));
	}

	@Test
	public void dataCanBeReplaced() {
		insertTestData();

		assertEquals(3, rtm1.getRowCount());
		assertEquals("0,0", rtm1.getValueAt(0, 0));
		assertEquals("1,1", rtm1.getValueAt(1, 1));
		assertEquals(3, rtm1.getValueAt(2, 2));
	}

	@Test
	public void cellsCannotBeEdited() {		
		insertTestData();
		
		for (int i = 0; i < rtm1.getRowCount(); i++) {
			for (int j = 0; j < rtm1.getColumnCount(); j++) {
				assertFalse(rtm1.isCellEditable(i, j));
			}
		}
	}
	
	@Test
	public void columnClassTypesAreMaintained() {
		insertTestData();
		
		assertEquals(String.class, rtm1.getColumnClass(0));
		assertEquals(String.class, rtm1.getColumnClass(1));
		assertEquals(Integer.class, rtm1.getColumnClass(2));
	}
	
	private void insertTestData() {
		String[] columnNames = {"Col A", "Col B", "Col C"};

		Object[][] newData = {
				{"0,0", "0,1", new Integer(1)},
				{"1,0", "1,1", new Integer(2)},
				{"2,0", "2,1", new Integer(3)}
		};

		rtm1.setColumnNames(columnNames);
		rtm1.setData(newData);
	}
}

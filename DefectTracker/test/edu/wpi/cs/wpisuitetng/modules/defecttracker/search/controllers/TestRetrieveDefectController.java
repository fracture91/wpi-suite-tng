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
 *    JPage
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.search.controllers;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.MockNetwork;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.MockRequest;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.views.DateTableCellRenderer;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.views.ResultsPanel;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabView;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class TestRetrieveDefectController {

	RetrieveDefectController controller;

	ResultsPanel view;

	@Before
	public void setUp() throws Exception {
		Network.initNetwork(new MockNetwork());
		Network.getInstance().setDefaultNetworkConfiguration(new NetworkConfiguration("http://wpisuitetng"));
		view = new ResultsPanel(new MainTabController(new MainTabView()));
		controller = new RetrieveDefectController(view);
	}

	@Test
	public void contructorSetsViewFieldCorrectly() {
		assertEquals(view, controller.view);

	}

	@Test
	public void ensureMockJTableIsValid() {
		setupMockJTable();
		assertEquals(3, view.getModel().getRowCount());
		assertEquals(1, view.getResultsTable().rowAtPoint(new Point(1, 0)));
		assertEquals("2", view.getModel().getValueAt(1, 0));
		assertEquals("2", view.getResultsTable().getModel().getValueAt(1, 0));
		assertEquals("2", (String) view.getResultsTable().getValueAt(1, 0));
	}

	@Test
	public void requestBuiltCorrectly() {
		// row to "click"
		int row = 1;

		// Replace results table with a mock table so we can fake the rowAtPoint() method
		setupMockJTable();
		assertEquals(3, view.getModel().getRowCount());

		// the id of the defect we are selecting
		int defectId = Integer.valueOf((String) view.getResultsTable().getValueAt(row, 0));

		// Construct dummy mouse click
		MouseEvent me = new MouseEvent(view.getResultsTable(), 0, 0, 0, row, 0, 2, false);

		// Click the mouse on the second row
		controller.mouseClicked(me);

		// See if the request was sent
		MockRequest request = ((MockNetwork)Network.getInstance()).getLastRequestMade();
		if (request == null) {
			fail("request not sent");
		}
		assertTrue(request.isSent());

		// Validate the request
		assertEquals("/defecttracker/defect/" + defectId, request.getUrl().getPath());
		assertEquals(HttpMethod.GET, request.getHttpMethod());
	}

	@Test
	public void invalidMouseClicksIgnoredProperly() {
		// invalid row click
		int row = -1;

		// Replace results table with a mock table so we can fake the rowAtPoint() method
		setupMockJTable();
		assertEquals(3, view.getModel().getRowCount());

		// Construct dummy mouse click
		MouseEvent me = new MouseEvent(view.getResultsTable(), 0, 0, 0, row, 0, 2, false);

		// Click the mouse on the second row
		controller.mouseClicked(me);

		// Request should not have been sent
		assertNull(((MockNetwork)Network.getInstance()).getLastRequestMade());
	}

	@Test
	public void singleClicksIgnoredProperly() {
		// Construct dummy mouse click
		MouseEvent me = new MouseEvent(view.getResultsTable(), 0, 0, 0, 0, 0, 1, false);

		// Click the mouse on the second row
		controller.mouseClicked(me);

		// Request should not have been sent
		assertNull(((MockNetwork)Network.getInstance()).getLastRequestMade());
	}

	private void setupMockJTable() {
		MockJTable mockResultsTable = new MockJTable(view.getModel());
		mockResultsTable.setDefaultRenderer(Date.class, new DateTableCellRenderer());
		mockResultsTable.addMouseListener(controller);
		view.setResultsTable(mockResultsTable);
		insertTestData();
	}

	private void insertTestData() {
		String[] columnNames = {"ID", "Title", "Description", "Creator", "Created", "Last Modified"};

		Object[][] newData = {
				{"1", "Defect 1", "Description", "ccasola", new Date(), new Date()},
				{"2", "Defect 2", "Description", "ccasola", new Date(), new Date()},
				{"3", "Defect 3", "Description", "ccasola", new Date(), new Date()},
		};

		view.getModel().setColumnNames(columnNames);
		view.getModel().setData(newData);
	}
}

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
 *    JPage
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.search.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.observers.RetrieveDefectRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.views.ResultsPanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller to handle retrieving one defect from the server
 */
public class RetrieveDefectController extends MouseAdapter {

	/** The results panel */
	protected ResultsPanel view;

	/**
	 * Construct the controller
	 * 
	 * @param view the parent view 
	 */
	public RetrieveDefectController(ResultsPanel view) {
		this.view = view;
	}

	/**
	 * @see java.awt.event.MouseAdapter#mouseClicked(MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent me) {
		if (me.getClickCount() == 2) { /* only respond to double clicks */

			// Get a reference to the results JTable from the mouse event
			JTable resultsTable = (JTable) me.getSource();

			// Determine the row the user clicked on
			int row = resultsTable.rowAtPoint(me.getPoint());

			// make sure the user actually clicked on a row
			if (row > -1) {
				String defectId = (String) resultsTable.getValueAt(row, 0);

				// Create and send a request for the defect with the given ID
				Request request;
				request = Network.getInstance().makeRequest("defecttracker/defect/" + defectId, HttpMethod.GET);
				request.addObserver(new RetrieveDefectRequestObserver(this));
				request.send();
			}
		}
	}

	/**
	 * Called by {@link RetrieveDefectRequestObserver} when the response
	 * is received from the server.
	 * @param defect the defect that was retrieved
	 */
	public void showDefect(Defect defect) {
		// Make a new defect view to display the defect that was received
		view.getTabController().addEditDefectTab(defect);
	}

	/**
	 * Called by {@link RetrieveDefectRequestObserver} when an error
	 * occurred retrieving the defect from the server.
	 */
	public void errorRetrievingDefect(String error) {
		JOptionPane.showMessageDialog(view, 
				"An error occurred opening the defect you selected. " + error, "Error opening defect", 
				JOptionPane.ERROR_MESSAGE);
	}
}

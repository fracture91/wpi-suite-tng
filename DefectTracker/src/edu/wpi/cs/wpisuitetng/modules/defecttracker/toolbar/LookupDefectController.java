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
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.JPlaceholderTextField;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller to handle retrieving a defect from the core
 */
public class LookupDefectController implements ActionListener {

	/** The main tab controller */
	protected MainTabController tabController;

	/** The parent view */
	protected ToolbarView parentView;

	/** The search field on the main toolbar */
	protected JPlaceholderTextField searchField;

	/** A flag to prevent multiple lookups from occurring at the same time */
	protected boolean waitingForResponse = false;

	/**
	 * Constructs the controller
	 * @param tabController the tab controller, to be used to add a view defect tab to the window
	 * @param searchField the search field in the main toolbar
	 */
	public LookupDefectController(MainTabController tabController, JPlaceholderTextField searchField, ToolbarView parentView) {
		this.tabController = tabController;
		this.searchField = searchField;
		this.parentView = parentView;
	}

	/**
	 * Send a request when the user hits the enter key while typing in the
	 * search defect field
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JTextField source = (JTextField) e.getSource();
		if (!waitingForResponse) { /* proceed if there is not already a request in progress */
			waitingForResponse = true; // we are now in the process of making a request

			// Validate the defect ID that was entered into the search field
			Integer id;
			try {
				id = Integer.parseInt(source.getText());
			}
			catch (NumberFormatException nfe) { // Invalid id, alert the user
				searchField.clearText();
				JOptionPane.showMessageDialog(parentView, "The defect ID you entered is not valid.", "Invalid Defect ID", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Generate the request
			Request request;
			request = Network.getInstance().makeRequest("defecttracker/defect/" + id, HttpMethod.GET);
			request.addObserver(new LookupRequestObserver(this));
			request.send();
		}
	}

	/**
	 * Method called by the observer when the response is received
	 * @param defect the defect that was received
	 */
	public void receivedResponse(Defect defect) {
		// Make a new defect view to display the defect that was received
		tabController.addEditDefectTab(defect);
		
		// Reset the search field
		searchField.clearText();

		// Clear the waiting flag
		waitingForResponse = false;
	}

	/**
	 * Method called by the observer if no defect was received
	 */
	public void requestFailed() {
		searchField.clearText();
		waitingForResponse = false;
		JOptionPane.showMessageDialog(parentView, "A defect with the ID you provided was not found.", "Defect Not Found", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Returns the waiting flag
	 * @return the waiting flag
	 */
	public boolean getWaitingFlag() {
		return waitingForResponse;
	}
}

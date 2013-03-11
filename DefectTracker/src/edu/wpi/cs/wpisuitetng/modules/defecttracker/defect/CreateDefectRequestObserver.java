/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    JPage
 *    Chris Casola
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * An Observer for a Request to create a Defect.
 */
public class CreateDefectRequestObserver implements RequestObserver {

	private final DefectView view;

	/**
	 * Constructs a new CreateDefectRequestObserver
	 * 
	 * @param view	The DefectView that will be affected by any updates.
	 */
	public CreateDefectRequestObserver(DefectView view) {
		this.view = view;
	}

	@Override
	public void responseSuccess(IRequest iReq) {
		// cast observable to a Request
		Request request = (Request) iReq;

		// get the response from the request
		ResponseModel response = request.getResponse();


		// print the body
		System.out.println("Received response: " + response.getBody()); //TODO change this to logger

		if (response.getStatusCode() == 201) {
			// parse the defect from the body
			final Defect defect = Defect.fromJSON(response.getBody());

			// make sure the defect isn't null
			if (defect != null) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						((DefectPanel) view.getDefectPanel()).updateModel(defect);
						view.setEditModeDescriptors(defect);
					}
				});
			}
			else {
				JOptionPane.showMessageDialog(view,	"Unable to parse defect received from server.", 
						"Save Defect Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			JOptionPane.showMessageDialog(view, 
					"Received " + iReq.getResponse().getStatusCode() + " status from server: " + iReq.getResponse().getStatusMessage(), 
					"Save Defect Error", JOptionPane.ERROR_MESSAGE);
		}

		always();
	}

	@Override
	public void responseError(IRequest iReq) {
		System.out.println("Error: " + iReq.getResponse().getBody());
		JOptionPane.showMessageDialog(view, 
				"Received " + iReq.getResponse().getStatusCode() + " error from server: " + iReq.getResponse().getStatusMessage(), 
				"Save Defect Error", JOptionPane.ERROR_MESSAGE);
		always();
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.out.println("Fail: " + iReq.getResponse().getBody());
		JOptionPane.showMessageDialog(view, "Unable to complete request: " + exception.getMessage(), 
				"Save Defect Error", JOptionPane.ERROR_MESSAGE);
		always();
	}

	/**
	 * Should always be run when an update method is called.
	 */
	private void always() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				view.setInputEnabled(true);				
			}
		});
	}
}

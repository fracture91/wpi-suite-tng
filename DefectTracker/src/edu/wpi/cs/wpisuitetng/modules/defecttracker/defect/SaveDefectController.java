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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel.Mode;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller to handle the saving of a defect
 *
 */
public class SaveDefectController {

	/** The view object containing the request fields */
	protected DefectView view;

	/**
	 * Construct a new handler for the given view
	 * @param view the view containing the request fields
	 */
	public SaveDefectController(DefectView view) {
		this.view = view;
	}

	/**
	 * Save the view's Defect model to the server (asynchronous).
	 */
	public void save() {
		final DefectPanel panel = (DefectPanel) view.getDefectPanel();
		final RequestObserver requestObserver = (panel.getEditMode() == Mode.CREATE) ? new CreateDefectRequestObserver(view) : new UpdateDefectRequestObserver(view);
		Request request;
		panel.getParent().setInputEnabled(false);
		request = Network.getInstance().makeRequest("defecttracker/defect", (panel.getEditMode() == Mode.CREATE) ? HttpMethod.PUT : HttpMethod.POST);
		request.setBody(panel.getEditedModel().toJSON());
		request.addObserver(requestObserver);
		request.send();
	}

}

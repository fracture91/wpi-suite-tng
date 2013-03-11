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

package edu.wpi.cs.wpisuitetng.janeway.gui.login;

import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

public class ProjectSelectRequestObserver implements RequestObserver {
	protected LoginController controller;

	public ProjectSelectRequestObserver(LoginController controller) {
		this.controller = controller;
	}

	@Override
	public void responseSuccess(IRequest iReq) {
		// cast observable to a Request
		Request request = (Request) iReq;

		// get the response from the request
		ResponseModel response = request.getResponse();

		// check the response code
		if (response.getStatusCode() == 200) {
			controller.projectSelectSuccessful(response);
		}
		else { // login failed
			controller.projectSelectFailed("Received " + iReq.getResponse().getStatusCode() + " error from server: " + iReq.getResponse().getStatusMessage());
		}
	}

	@Override
	public void responseError(IRequest iReq) {
		if (iReq.getResponse().getStatusCode() == 403) {
			controller.projectSelectFailed("Incorrect username, password or project.");

		} else {
			controller.projectSelectFailed("Received " + iReq.getResponse().getStatusCode() + " error from server: " + iReq.getResponse().getStatusMessage());
		}
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		controller.projectSelectFailed("Unable to complete request: " + exception.getMessage());
	}
}

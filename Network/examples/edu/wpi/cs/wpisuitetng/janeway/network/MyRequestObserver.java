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
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.janeway.network;

import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * A RequestObserver for the Request class.
 * 
 * TODO Make this example more thorough.
 */
public class MyRequestObserver implements RequestObserver {
	@Override
	public void responseSuccess(IRequest iReq) {
			// get the response from the request
			ResponseModel response = iReq.getResponse();

			// print the body
			System.out.println("Received response: " + response.getBody());

	}

	@Override
	public void responseError(IRequest iReq) {
		// TODO Auto-generated method stub
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		// TODO Auto-generated method stub
	}
}

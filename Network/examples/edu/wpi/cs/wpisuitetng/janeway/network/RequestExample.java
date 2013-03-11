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
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.janeway.network;

import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This is a preliminary example for making request using the Request class.
 * @author jenny
 * 
 */
public class RequestExample {
	private static String host = "localhost";
	private static int port = 8080;

	/**
	 * Main method.
	 */
	public static void main(String[] args) {
		// Make a new instance of the MyRequestObserver class.
		final MyRequestObserver requestObserver = new MyRequestObserver();

		final NetworkConfiguration config = new NetworkConfiguration("http://" + host + ":" + port);





		/* ~~~ Below shows how to send a request manually. ~~~ */

		// The first Request's body
		String body1 = "The request body of the manually configured Request";

		// Make a new POST Request.
		Request manualRequest = new Request(config, "subpath", HttpMethod.POST);	// construct the Request

		// Configure the request
		manualRequest.setBody(body1);	// set the request body to send to the server
		manualRequest.addObserver(requestObserver);	// Add the requestObserver to the request's set of Observers

		// Send the request!
		manualRequest.send();

		// This should print first.
		System.out.println("This was printed after sending the manually created Request!");





		/* ~~~ Below shows how to send a request using the Network. ~~~ */

		// Set the default network configuration. This step will likely already have been completed by Janeway, so you don't have to do it.
		Network.getInstance().setDefaultNetworkConfiguration(config);

		// The first Request's body
		String body2 = "The request body of the Request created by Network";

		// Make a new POST Request with the default network configuration.
		Request networkRequest = Network.getInstance().makeRequest("subpath", HttpMethod.POST);

		// Configure the request
		networkRequest.setBody(body2);	// set the request body to send to the server
		networkRequest.addObserver(requestObserver);	// Add the requestObserver to the request's set of Observers

		// Send the request!
		networkRequest.send();

		// This should print first.
		System.out.println("This was printed after sending the Request created using Network!");
	}
}

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
 *    Andrew Hurle
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.network;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Represents a Response to a Request.
 * 
 * TODO Think about just taking a HTTPURLConnection in the constructor.
 * TODO Convert response body String into a Model object.
 */
public class Response extends ResponseModel {
	/**
	 * Constructor.
	 * 
	 * @param responseCode
	 * @param responseMessage
	 * @param headers
	 * @param responseBody
	 */
	public Response(int responseCode, String responseMessage, Map<String, List<String>> headers, String responseBody) {
		super();
		
		setStatusCode(responseCode);
		setStatusMessage(responseMessage);
		
		// Copy headers into this.headers
		Iterator<String> headerKeysI = headers.keySet().iterator();
		while (headerKeysI.hasNext()) {
			String key = headerKeysI.next();
			for (String header : headers.get(key)) {
				if (key != null) {
					addHeader(key, header);
				}
			}
		}
		
		setBody(responseBody);
	}
}

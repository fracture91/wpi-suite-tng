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

package edu.wpi.cs.wpisuitetng.network.models;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * An interface for providing information regarding a network request. This 
 * interface allows for RequestObserver to be a superclass of Request while 
 * preventing the need for developers to cast a RequestObserver to a Request 
 * inside their responseSuccess, responseError and fail methods.
 */
public interface IRequest {
	/**
	 * Returns a String containing the request body.
	 * 
	 * @return	A String containing the request body.
	 */
	public String getBody();
	
	/**
	 * Returns a Map of request header keys to Lists of request header values.
	 * 
	 * @return	A Map of request header keys to Lists of request header values.
	 */
	public Map<String, List<String>> getHeaders();
	
	/**
	 * Returns a HttpMethod representing the HTTP request method.
	 * 
	 * @return	A HttpMethod representing the request method.
	 */
	public HttpMethod getHttpMethod();
	
	/**
	 * Returns a Map containing the query data.
	 * 
	 * @return a Map containing the query data.
	 */
	public Map<String, String> getQueryData();
	
	/**
	 * Returns the server's Response to the Request.
	 * 
	 * @return	The server's Response to the Request.
	 */
	public ResponseModel getResponse();

	/**
	 * Returns a URL pointing to the server.
	 * 
	 * @return	A URL pointing to the server.
	 */
	public URL getUrl();
	
}

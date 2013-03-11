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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.RequestModel;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * This class represents a Request. It can be observed by one or more RequestObservers.
 * 
 * A Request can be sent synchronously or asynchronously. By default, a Request is asynchronous upon 
 * construction. When a synchronous Request is sent, it will block, causing the current thread to pause 
 * while the Request is sent and while waiting for a Response. The RequestObservers that have been added to 
 * the Request will not be notified. In most cases, you will not want to send a synchronous Request. When a 
 * Request is sent asynchronously, a new thread is created which sends the Request, generates a Response, 
 * and notifies any RequestObservers that have been added to the Request.
 * 
 * TODO add equals method
 */
public class Request extends RequestModel {
	private List<RequestObserver> observers;
	protected int readTimeout = 5000;
	protected int connectTimeout = 5000;
	protected boolean running = false;
	protected boolean isAsynchronous = true;

	/**
	 * Constructor.
	 * 
	 * @param networkConfiguration	The NetworkConfiguration to use.
	 * @param path					The path to append to the API URL.
	 * @param httpMethod			The HttpMethod to use.
	 * 
	 * @throw RuntimeException		If a MalformedURLException is received while constructing the URL.
	 * @throw NullPointerException	If the networkConfiguration or requestMethod is null.
	 */
	public Request(NetworkConfiguration networkConfiguration, String path, HttpMethod httpMethod) {
		// check to see if the networkConfiguration is null
		if (networkConfiguration == null) {
			throw new NullPointerException("The networkConfiguration must not be null.");
		}

		// check to see if the httpMethod is null
		if (httpMethod == null) {
			throw new NullPointerException("The httpMethod must not be null.");
		}

		try {
			// set requestURL
			// TODO improve code
			if (path == null || path.length() == 0) {
				setUrl(new URL(networkConfiguration.getApiUrl()));
			}
			else if (networkConfiguration.getApiUrl().charAt(networkConfiguration.getApiUrl().length() - 1) == '/') {
				setUrl(new URL(networkConfiguration.getApiUrl() + path));
			}
			else {
				setUrl(new URL(networkConfiguration.getApiUrl() + '/' + path));
			}

			// set the HttpMethod
			setHttpMethod(httpMethod);

			// Copy request headers from networkConfiguration
			for (String currentKey : networkConfiguration.getRequestHeaders().keySet()) {
				for (String currentValue : networkConfiguration.getRequestHeaders().get(currentKey)) {
					addHeader(currentKey, currentValue);
				}
			}

			// Copy observers from networkConfiguration
			for (RequestObserver observer : networkConfiguration.getObservers()) {
				addObserver(observer);
			}
		}
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

		observers = new ArrayList<RequestObserver>();
	}

	/**
	 * Sends the Request by creating a new RequestActor and starting it as a new Thread.
	 * Note: If Request.isAsynchronous is false, RequestObservers for this Request will not be updated.
	 * 
	 * @throws IllegalStateException	If the Request is being sent.
	 */
	public void send() throws IllegalStateException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		RequestActor requestActor = new RequestActor(this);

		if (isAsynchronous) {
			requestActor.start();
		}
		else {
			requestActor.run();
		}
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.models.RequestModel#addHeader(String, String)
	 * 
	 * @throws IllegalStateException	If the Request is being sent.
	 */
	public void addHeader(String key, String value) throws IllegalStateException, NullPointerException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		super.addHeader(key, value);
	}
	
	/**
	 * @see edu.wpi.cs.wpisuitetng.network.models.RequestModel#addQueryData(String, String)
	 * 
	 * @throws IllegalStateException	If the Request is being sent.
	 */
	public void addQueryData(String key, String value) throws IllegalStateException, NullPointerException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		super.addHeader(key, value);
	}

	/**
	 * Makes this Request synchronous.
	 * 
	 * @throws IllegalStateException	If the Request is being sent.
	 */
	public void clearAsynchronous() throws IllegalStateException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		isAsynchronous = false;
	}

	/**
	 * Makes this Request asynchronous.
	 * 
	 * @throws IllegalStateException	If the Request is being sent.
	 */
	public void setAsynchronous() throws IllegalStateException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		isAsynchronous = true;
	}

	/**
	 * Sets the timeout (in milliseconds) for connecting to the server.
	 * 
	 * @param connectTimeout the timeout (in milliseconds) for connecting to the server.
	 * 
	 * @throws IllegalStateException	If the Request is being sent.
	 */
	public void setConnectTimeout(int connectTimeout) throws IllegalStateException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		this.connectTimeout = connectTimeout;
	}
	
	/**
	 * Sets the timeout (in milliseconds) for reading the response body.
	 * 
	 * @param readTimeout the timeout (in milliseconds) for reading the response body
	 * 
	 * @throws IllegalStateException	If the Request is being sent.
	 */
	public void setReadTimeout(int readTimeout) throws IllegalStateException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		this.readTimeout = readTimeout;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.models.RequestModel#setBody(String)
	 * 
	 * @throws IllegalStateException	If the Request is being sent.
	 */
	public void setBody(String body) throws IllegalStateException, NullPointerException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		super.setBody(body);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.models.RequestModel#setHttpMethod(HttpMethod)
	 * 
	 * @throws IllegalStateException	If the Request is being sent.
	 */
	public void setHttpMethod(HttpMethod httpMethod) throws IllegalStateException, NullPointerException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		super.setHttpMethod(httpMethod);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.models.RequestModel#setResponse(ResponseModel)
	 * 
	 * @throws	IllegalStateException	If the Request is being sent.
	 */
	protected void setResponse(ResponseModel response) throws IllegalStateException {
		// check to see if the request has been sent yet
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		super.setResponse(response);
	}

	/**
	 * Returns the timeout (in milliseconds) for connecting to the server.
	 * 
	 * @return the timeout (in milliseconds) for connecting to the server.
	 */
	public int getConnectTimeout() {
		return readTimeout;
	}
	
	/**
	 * Returns the timeout (in milliseconds) for reading the response body.
	 * 
	 * @return the timeout (in milliseconds) for reading the response body.
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * Returns a boolean indicating whether or not this Request is asynchronous.
	 * 
	 * @return a boolean indicating whether or not this Request is asynchronous.
	 */
	public boolean isAsynchronous() {
		return isAsynchronous;
	}















	/**
	 * Adds a RequestObserver to this Observable.
	 * 
	 * @param o The RequestObserver to add.
	 * 
	 * @throws	IllegalStateException	If the Request is being sent.
	 */
	public void addObserver(RequestObserver o) throws IllegalStateException {
		// check to see if the request has been sent yet
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		observers.add(o);
	}

	/**
	 * Returns the number of RequestObservers for this Observable.
	 * 
	 * @return The number of RequestObservers for this Observable.
	 */
	public int countObservers() {
		return observers.size();
	}

	/**
	 * Notifies RequestObservers of a response with a status code indicating success (2xx).
	 * 
	 * @throws	IllegalStateException	If the Request is being sent.
	 */
	public void notifyObserversResponseSuccess() throws IllegalStateException {
		// check to see if the request has been sent yet
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		for (RequestObserver obs : observers) {
			obs.responseSuccess((IRequest) this);
		}
	}

	/**
	 * Notifies RequestObservers of response with a status code indicating a client error (4xx) for server error (5xx).
	 * 
	 * @throws	IllegalStateException	If the Request is being sent.
	 */
	public void notifyObserversResponseError() throws IllegalStateException {
		// check to see if the request has been sent yet
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		for (RequestObserver obs : observers) {
			obs.responseError((IRequest) this);
		}
	}

	/**
	 * Notifies RequestObservers of a failure in sending a request.
	 * 
	 * @param exception An exception.
	 * 
	 * @throws	IllegalStateException	If the Request is being sent.
	 */
	public void notifyObserversFail(Exception exception) throws IllegalStateException {
		// check to see if the request has been sent yet
		if (running) {
			throw new IllegalStateException("Request is being sent.");
		}

		for (RequestObserver obs : observers) {
			obs.fail((IRequest) this, exception);
		}
	}
}

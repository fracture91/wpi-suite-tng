package edu.wpi.cs.wpisuitetng.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;

/**
 * This class represents a Request. It can be observed by one or more RequestObservers.
 * 
 * A Request can be sent synchronously or asynchronously. By default, a Request is asynchronous upon 
 * construction. When a synchronous Request is sent, it will block, causing the current thread to pause 
 * while the Request is sent and while waiting for a Response. The RequestObservers that have been added to 
 * the Request will not be notified. In most cases, you will not want to send a synchronous Request. When a 
 * Request is sent asynchronously, a new thread is created which sends the Request, generates a Response, 
 * and notifies any RequestObservers that have been added to the Request.
 */
public class Request extends Observable implements IRequest {
	/**
	 * Represents an HTTP request method.
	 */
	public static enum RequestMethod {
		GET, POST, PUT, DELETE
	}

	protected boolean running = false;

	protected String requestBody;
	protected Map<String, List<String>> requestHeaders;
	protected URL requestURL;
	protected RequestMethod requestMethod;
	protected Response response;
	protected boolean isAsynchronous;

	/**
	 * Constructor.
	 * 
	 * @param networkConfiguration	The NetworkConfiguration to use.
	 * @param path					The path to append to the API URL.
	 * @param requestMethod			The HTTP RequestMethod to use.
	 * 
	 * @throw RuntimeException		If a MalformedURLException is received while constructing the URL.
	 * @throw NullPointerException	If the networkConfiguration or requestMethod is null.
	 */
	public Request(NetworkConfiguration networkConfiguration, String path, RequestMethod requestMethod) {
		// check to see if the networkConfiguration is null
		if (networkConfiguration == null) {
			throw new NullPointerException("The networkConfiguration must not be null.");
		}

		// check to see if the requestMethod is null
		if (requestMethod == null) {
			throw new NullPointerException("The requestMethod must not be null.");
		}

		try {
			// set requestURL
			// TODO improve code
			if (path == null || path.length() == 0) {
				this.requestURL = new URL(networkConfiguration.getApiUrl());
			}
			else if (networkConfiguration.getApiUrl().charAt(networkConfiguration.getApiUrl().length() - 1) == '/') {
				this.requestURL = new URL(networkConfiguration.getApiUrl() + path);
			}
			else {
				this.requestURL = new URL(networkConfiguration.getApiUrl() + '/' + path);
			}
			this.requestMethod = requestMethod;

			// Copy request headers from networkConfiguration
			requestHeaders = new HashMap<String, List<String>>();
			Iterator<String> keysI = networkConfiguration.getRequestHeaders().keySet().iterator();
			Iterator<String> valuesI;
			String currentKey;
			while (keysI.hasNext()) {
				currentKey = keysI.next();
				valuesI = networkConfiguration.getRequestHeaders().get(currentKey).iterator();

				while (valuesI.hasNext()) {
					this.addRequestHeader(currentKey, valuesI.next());
				}
			}

			// Copy observers from networkConfiguration
			Iterator<RequestObserver> observersI = networkConfiguration.getObservers().iterator();
			while (observersI.hasNext()) {
				this.addObserver(observersI.next());
			}
		}
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

		setAsynchronous();
	}

	/**
	 * Sends the Request by creating a new RequestActor and starting it as a new Thread.
	 * Note: If Request.isAsynchronous is false, RequestObservers for this Request will not be updated.
	 * 
	 * @throws IllegalStateException	If the request has already been sent.
	 */
	public void send() throws IllegalStateException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request already sent.");
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
	 * Adds a header to the request.
	 * 
	 * @param key		A String representing the header key.
	 * @param value		A String representing the header value.
	 * 
	 * @throws IllegalStateException	If the request has already been sent.
	 * @throws NullPointerException		If the key is null.
	 */
	public void addRequestHeader(String key, String value) throws IllegalStateException, NullPointerException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request already sent.");
		}

		// check to see if the key is null
		if (key == null) {
			throw new NullPointerException("The key must not be null.");
		}

		// get the List of current values from the requestHeaders Map
		List<String> currentValues = requestHeaders.get(key);

		// if the List of current values is null, create a new list of current values
		if (currentValues == null) {
			currentValues = new ArrayList<String>();
		}

		// add the new value to the list of current values
		currentValues.add(value);

		// store the updated List of current values in the Map
		requestHeaders.put(key, currentValues);
	}

	/**
	 * Makes this Request synchronous.
	 */
	public void clearAsynchronous() {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request already sent.");
		}
		
		isAsynchronous = false;
	}

	/**
	 * Makes this Request asynchronous.
	 */
	public void setAsynchronous() {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request already sent.");
		}

		isAsynchronous = true;
	}

	/**
	 * Sets the body of the request.
	 * TODO elaborate
	 * 
	 * @param requestBody	The body of the request to send to the server.
	 * 
	 * @throws IllegalStateException	If the request has already been sent.
	 * @throws NullPointerException		If the requestBody is null.
	 */
	public void setRequestBody(String requestBody) throws IllegalStateException, NullPointerException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request already sent.");
		}

		// check to see if the key is null
		if (requestBody == null) {
			throw new NullPointerException("The requestBody parameter must not be null.");
		}

		this.requestBody = requestBody;
	}

	/**
	 * Sets the HTTP method for the Request.
	 * TODO elaborate
	 * 
	 * @param requestMethod
	 * 
	 * @throws IllegalStateException	If the request has already been sent.
	 * @throws NullPointerException		If the requestMethod is null.
	 */
	public void setRequestMethod(RequestMethod requestMethod) throws IllegalStateException, NullPointerException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request already sent.");
		}

		// check to see if the requestMethod is null
		if (requestMethod == null) {
			throw new NullPointerException("The requestMethod parameter must not be null.");
		}

		this.requestMethod = requestMethod;
	}

	/**
	 * Sets the server's Response to the Request.
	 * 
	 * @param response	The server's Response to the Request.
	 * 
	 * @throws	IllegalStateException	If the Request has not been sent yet.
	 */
	protected void setResponse(Response response) {
		// check to see if the request has been sent yet
		if (running) {
			throw new IllegalStateException("Request has not been sent yet.");
		}

		// set the Response to this Request
		this.response = response;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.IRequest#getRequestBody
	 */
	public String getRequestBody() {
		return requestBody;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.IRequest#getRequestHeaders
	 */
	public Map<String, List<String>> getRequestHeaders() {
		return requestHeaders;
	}

	/**
	 * Returns a String representing the HTTP request method. Ex: "GET", "POST", "PUT", "DELETE"
	 * 
	 * @return	A String representing the request method.
	 */
	public String getRequestMethod() {
		if (requestMethod == RequestMethod.GET) {
			return "GET";
		} else if (requestMethod == RequestMethod.POST) {
			return "POST";
		} else if (requestMethod == RequestMethod.PUT) {
			return "PUT";
		} else if (requestMethod == RequestMethod.DELETE) {
			return "DELETE";
		}

		// default request method is GET
		return "GET";
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.IRequest#getResponse
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.IRequest#getURL
	 */
	public URL getURL() {
		return requestURL;
	}

	/**
	 * Returns a boolean indicating whether or not this Request is asynchronous.
	 * 
	 * @return a boolean indicating whether or not this Request is asynchronous.
	 */
	public boolean isAsynchronous() {
		return isAsynchronous;
	}
}

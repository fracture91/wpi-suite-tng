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
 * This class represents a Request. It can be observed by one or more Observers.
 * 
 * TODO add setRequestData (for url data, etc)
 */
public class Request extends Observable implements IRequest {
	/**
	 * Represents an HTTP request method.
	 */
	public static enum RequestMethod {
		GET, POST, PUT, DELETE
	}

	// TODO add RequestStatus to replace running with?
	private boolean running = false;

	private String requestBody;
	private Map<String, List<String>> requestHeaders;
	private URL requestURL;
	private RequestMethod requestMethod;
	private Response response;

	/**
	 * Constructor.
	 * 
	 * @param networkConfiguration	The NetworkConfiguration to use.
	 * @param path					The path to append to the API URL.
	 * @param requestMethod			The HTTP RequestMethod to use.
	 * 
	 * @throws MalformedURLException 	
	 * @throw NullPointerException		If the networkConfiguration or requestMethod is null.
	 */
	public Request(NetworkConfiguration networkConfiguration, String path, RequestMethod requestMethod) throws NullPointerException, MalformedURLException {
		// check to see if the networkConfiguration is null
		if (networkConfiguration == null) {
			throw new NullPointerException("The networkConfiguration must not be null.");
		}

		// check to see if the requestMethod is null
		if (requestMethod == null) {
			throw new NullPointerException("The requestMethod must not be null.");
		}

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

	/**
	 * Sends the Request by creating a new RequestActor and starting it as a new Thread.
	 * 
	 * @throws IllegalStateException	If the request has already been sent.
	 */
	public void send() throws IllegalStateException {
		// check to see if the request has already been sent
		if (running) {
			throw new IllegalStateException("Request already sent.");
		}

		RequestActor requestActor = new RequestActor(this);
		requestActor.start();
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
	 * Returns a String containing the request body.
	 * 
	 * @return	A String containing the request body.
	 */
	public String getRequestBody() {
		return requestBody;
	}

	/**
	 * Returns a Map of request header keys to Lists of request header values.
	 * 
	 * @return	A Map of request header keys to Lists of request header values.
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
	 * Returns the server's Response to the Request.
	 * 
	 * @return	The server's Response to the Request.
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * Returns a URL pointing to the server.
	 * 
	 * TODO Make this javadoc better
	 * 
	 * @return	A URL pointing to the server.
	 */
	public URL getURL() {
		return requestURL;
	}
}

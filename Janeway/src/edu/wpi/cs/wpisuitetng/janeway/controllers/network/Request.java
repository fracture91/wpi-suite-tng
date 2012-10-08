package edu.wpi.cs.wpisuitetng.janeway.controllers.network;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * This class represents a Request. It can be observed by one or more Observers.
 * 
 * TODO add setRequestData (for url data, etc)
 */
public class Request extends Observable {
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
	 * @param requestURL		The URL to make the HTTP request to.
	 * @param requestMethod		The HTTP RequestMethod to use.
	 * 
	 * @throw NullPointerException	If the requestURL is null.
	 */
	public Request(URL requestURL) throws NullPointerException {
		// check to see if the requestURL is null
		if (requestURL == null) {
			throw new NullPointerException("The requestURL must not be null.");
		}
		
		this.requestURL = requestURL;
		this.requestMethod = RequestMethod.GET; // default is GET
		
		requestHeaders = new HashMap<String, List<String>>();
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
		
		// set the Request as changed
		this.setChanged();
		
		// notify Observers
		this.notifyObservers();
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

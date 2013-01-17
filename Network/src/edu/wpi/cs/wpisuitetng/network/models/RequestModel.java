package edu.wpi.cs.wpisuitetng.network.models;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestModel implements IRequest {
	protected String requestBody;
	protected Map<String, List<String>> requestHeaders;
	protected URL requestURL;
	protected RequestMethod requestMethod;
	protected ResponseModel response;
	
	public RequestModel() {
		requestHeaders = new HashMap<String, List<String>>();
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
		// check to see if the requestMethod is null
		if (requestMethod == null) {
			throw new NullPointerException("The requestMethod parameter must not be null.");
		}

		this.requestMethod = requestMethod;
	}

	/**
	 * Sets the server's Response to the Request.
	 * 
	 * @param response	The server's ResponseModel to the Request.
	 * 
	 * @throws	IllegalStateException	If the Request has not been sent yet.
	 */
	protected void setResponse(ResponseModel response) {
		// set the Response to this Request
		this.response = response;
	}
	
	/**
	 * TODO
	 */
	public void setURL(URL url) {
		requestURL = url;
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
	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.IRequest#getResponse
	 */
	public ResponseModel getResponse() {
		return response;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.IRequest#getURL
	 */
	public URL getURL() {
		return requestURL;
	}
}

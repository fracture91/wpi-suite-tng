package edu.wpi.cs.wpisuitetng.network.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a response to a request.
 * 
 * TODO Think about just taking a HTTPURLConnection in the constructor.
 * TODO Convert response body String into a Model object.
 */
public class ResponseModel {
	private int statusCode;
	private String statusMessage;
	private Map<String, List<String>> headers;
	private String body;
	
	public ResponseModel() {
		this.headers = new HashMap<String, List<String>>();
	}
	
	public void setStatusCode(int code) {
		statusCode = code;
	}
	
	public void setStatusMessage(String message) {
		statusMessage = message;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	/**
	 * Adds a header to the response.
	 * 
	 * @param key		A String representing the header key.
	 * @param value		A String representing the header value.
	 * 
	 * @throws NullPointerException		If the key is null.
	 */
	public void addHeader(String key, String value) throws NullPointerException {
		// check to see if the key is null
		if (key == null) {
			throw new NullPointerException("The key must not be null.");
		}

		// get the List of current values from the requestHeaders Map
		List<String> currentValues = headers.get(key);

		// if the List of current values is null, create a new list of current values
		if (currentValues == null) {
			currentValues = new ArrayList<String>();
		}

		// add the new value to the list of current values
		currentValues.add(value);

		// store the updated List of current values in the Map
		headers.put(key, currentValues);
	}
	
	/**
	 * Returns a String containing the response body.
	 * 
	 * @return	A String containing the response body.
	 */
	public String getBody() {
		return this.body;
	}
	
	/**
	 * Returns an integer representing the status code received in the response from the server.
	 * 
	 * @return	An integer representing the status code received in the response.
	 */
	public int getStatusCode() {
		return statusCode;
	}
	
	/**
	 * Returns a Map<String, List<String>> containing the response headers.
	 * 
	 * @return	A Map<String, List<String>> containing the response headers.
	 */
	public Map<String, List<String>> getHeaders() {
		return this.headers;
	}
	
	/**
	 * Returns a String representing the status message received in the response from the server.
	 * 
	 * @return	A String representing the status message received in the response.
	 */
	public String getStatusMessage() {
		return statusMessage;
	}
}

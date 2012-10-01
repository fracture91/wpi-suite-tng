package edu.wpi.cs.wpisuitetng.janeway.controllers.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Represents a Response to a Request.
 * 
 * TODO Think about just taking a HTTPURLConnection in the constructor.
 * TODO Convert response body String into a Model object.
 */
public class Response {
	private int responseCode;
	private String responseMessage;
	private Map<String, List<String>> headers;
	private String body;
	
	/**
	 * Constructor.
	 * 
	 * @param responseCode
	 * @param responseMessage
	 * @param headers
	 * @param responseBody
	 */
	public Response(int responseCode, String responseMessage, Map<String, List<String>> headers, String responseBody) {
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		
		this.headers = new HashMap<String, List<String>>();
		
		// Copy headers into this.headers
		Iterator<String> headerKeysI = headers.keySet().iterator();
		while (headerKeysI.hasNext()) {
			String key = headerKeysI.next();
			this.headers.put(key, headers.get(key));
		}
		
		this.body = responseBody;
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
	 * Returns an integer representing the response code received in the response from the server.
	 * 
	 * @return	An integer representing the response code received in the response.
	 */
	public int getResponseCode() {
		return this.responseCode;
	}
	
	/**
	 * Returns a Map<String, List<String>> containing the response headers.
	 * 
	 * @return	A Map<String, List<String>> containing the response headers.
	 */
	public Map<String, List<String>> getResponseHeaders() {
		return this.headers;
	}
	
	/**
	 * Returns a String representing the response message received in the response from the server.
	 * 
	 * @return	A String representing the response message received in the response.
	 */
	public String getResponseMessage() {
		return this.responseMessage;
	}
}

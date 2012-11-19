package edu.wpi.cs.wpisuitetng.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NetworkConfiguration {
	protected URL apiUrl;			/** The URL of the core server API */
	protected String username;		/** The username. TODO This may not be needed, depending on how the API works. */
	protected String password;		/** The password. TODO This may not be needed, depending on how the API works. */
	protected Map<String, List<String>> defaultRequestHeaders;	/** The default request headers. */

	/**
	 * Constructs a NetworkConfiguration.
	 * 
	 * @param apiUrl	The url to the core server API.
	 */
	public NetworkConfiguration(URL apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	/**
	 * Adds a header to the default request headers.
	 * 
	 * @param key		A String representing the header key.
	 * @param value		A String representing the header value.
	 * 
	 * @throws NullPointerException		If the key or value is null.
	 */
	public void addRequestHeader(String key, String value) throws NullPointerException {
		// check to see if the key is null
		if (key == null) {
			throw new NullPointerException("The key must not be null.");
		}
		
		// check to see if the value is null
		if (value == null) {
			throw new NullPointerException("The value must not be null.");
		}
		
		// get the List of current values from the requestHeaders Map
		List<String> currentValues = defaultRequestHeaders.get(key);
		
		// if the List of current values is null, create a new list of current values
		if (currentValues == null) {
			currentValues = new ArrayList<String>();
		}
		
		// add the new value to the list of current values
		currentValues.add(value);
		
		// store the updated List of current values in the Map
		defaultRequestHeaders.put(key, currentValues);
	}
	
	/**
	 * @return A URL to the core server API.
	 */
	public URL getApiUrl() {
		return apiUrl;
	}
	
	/**
	 * Returns a Map of the default request headers. The return value is not safe for modification.
	 * 
	 * @return A Map of the default request headers.
	 */
	public Map<String, List<String>> getRequestHeaders() {
		return defaultRequestHeaders;
	}
}

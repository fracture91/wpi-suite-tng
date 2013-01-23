package edu.wpi.cs.wpisuitetng.network.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.network.RequestObserver;

/**
 * Stores configuration information for connecting to the core server API.
 */
public class NetworkConfiguration {
	protected String apiUrl;									/** The URL of the core server API */
	protected Map<String, List<String>> defaultRequestHeaders;	/** The default request headers. */
	protected List<RequestObserver> observers;					/** The default observers. */

	/**
	 * Constructs a NetworkConfiguration.
	 * 
	 * @param apiUrl	The url to the core server API.
	 */
	public NetworkConfiguration(String apiUrl) {
		this.apiUrl = apiUrl;
		this.defaultRequestHeaders = new HashMap<String, List<String>>();
		this.observers = new ArrayList<RequestObserver>();
	}

	/**
	 * Creates a new NetworkConfiguration which duplicates the given NetworkConfiguration.
	 * 
	 * @param networkConfiguration	The NetworkConfiguration to duplicate.
	 */
	public NetworkConfiguration(NetworkConfiguration networkConfiguration) {
		this(networkConfiguration.getApiUrl());

		// Copy request headers from networkConfiguration
		for (String currentKey : networkConfiguration.getRequestHeaders().keySet()) {
			for (String currentValue : networkConfiguration.getRequestHeaders().get(currentKey)) {
				this.addRequestHeader(currentKey, currentValue);
			}
		}

		// Copy observers from networkConfiguration
		for (RequestObserver observer : networkConfiguration.observers) {
			this.addObserver(observer);
		}
	}

	/**
	 * Adds an observer to the observers.
	 * 
	 * @param observer	The observer to add to the observers.
	 * 
	 * @throws NullPointerException		If the observer is null.
	 */
	public void addObserver(RequestObserver observer) throws NullPointerException {
		// check to see if the observer is null
		if (observer == null) {
			throw new NullPointerException("The observer must not be null.");
		}

		observers.add(observer);
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
	public String getApiUrl() {
		return apiUrl;
	}

	/**
	 * @return	A List of Observers.
	 */
	public List<RequestObserver> getObservers() {
		return observers;
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

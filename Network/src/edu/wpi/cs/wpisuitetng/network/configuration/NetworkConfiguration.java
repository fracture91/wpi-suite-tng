package edu.wpi.cs.wpisuitetng.network.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observer;

/**
 * Stores configuration information for connecting to the core server API.
 */
public class NetworkConfiguration {
	protected String apiUrl;									/** The URL of the core server API */
	protected Map<String, List<String>> defaultRequestHeaders;	/** The default request headers. */
	protected List<Observer> observers;						/** The default observers. */
	
	/**
	 * Constructs a NetworkConfiguration.
	 * 
	 * @param apiUrl	The url to the core server API.
	 */
	public NetworkConfiguration(String apiUrl) {
		this.apiUrl = apiUrl;
		this.defaultRequestHeaders = new HashMap<String, List<String>>();
		this.observers = new ArrayList<Observer>();
	}
	
	/**
	 * Creates a new NetworkConfiguration which duplicates the given NetworkConfiguration.
	 * 
	 * @param networkConfiguration	The NetworkConfiguration to duplicate.
	 */
	public NetworkConfiguration(NetworkConfiguration networkConfiguration) {
		this(networkConfiguration.getApiUrl());
		
		// Copy request headers from networkConfiguration
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
		Iterator<Observer> observersI = networkConfiguration.observers.iterator();
		while (observersI.hasNext()) {
			this.addObserver(observersI.next());
		}
	}
	
	/**
	 * Adds an observer to the observers.
	 * 
	 * @param observer	The observer to add to the observers.
	 * 
	 * @throws NullPointerException		If the observer is null.
	 */
	public void addObserver(Observer observer) throws NullPointerException {
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
	public List<Observer> getObservers() {
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

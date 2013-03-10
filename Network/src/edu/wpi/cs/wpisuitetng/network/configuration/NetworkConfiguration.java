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
 ******************************************************************************/

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
	 * Adds a cookie to the request headers.
	 * 
	 * @param name  The name of the cookie.
	 * @param value The value of the cookie.
	 */
	public void addCookie(String name, String value) {
		if (name == null) {
			throw new NullPointerException("The name must not be null.");
		}
		if (value == null) {
			throw new NullPointerException("The value must not be null.");
		}
		
		
		String cookiesString;

		if (defaultRequestHeaders.get("cookie") != null && !defaultRequestHeaders.get("cookie").isEmpty()) {
			cookiesString = defaultRequestHeaders.get("cookie").get(0);
		}
		else {
			cookiesString = "";
		}

		String cookies[] = cookiesString.split(";\n");
		Map<String, String> cookiesVals = new HashMap<String, String>();
		String cookieVal[];
		boolean cookieMatch = false;
		
		for (int i = 0; i < cookies.length; i++) {
			cookieVal = cookies[i].split("=");
			
			if (cookieVal.length == 2) {
				if (name.equals(cookieVal[0])) {
					cookieMatch = true;
					cookiesVals.put(cookieVal[0], value);
				}
				else {
					cookiesVals.put(cookieVal[0], cookieVal[1]);
				}
			}
		}
		
		if (!cookieMatch) {
			cookiesVals.put(name, value);
		}
		
		cookiesString = "";
		boolean firstCookie = true;
		for (String cookieName : cookiesVals.keySet()) {
			cookiesString += (firstCookie ? "" : ";\n") + cookieName + "=" + cookiesVals.get(cookieName);
			
			if (firstCookie) {
				firstCookie = false;
			}
		}
		
		if (defaultRequestHeaders.get("cookie") == null) {
			List<String> cookieList = new ArrayList<String>();
			defaultRequestHeaders.put("cookie", cookieList);
		}
		
		if (defaultRequestHeaders.get("cookie").isEmpty()) {
			defaultRequestHeaders.get("cookie").add(cookiesString);
		}
		else {
			defaultRequestHeaders.get("cookie").set(0, cookiesString);
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

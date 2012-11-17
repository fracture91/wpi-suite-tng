package edu.wpi.cs.wpisuitetng.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observer;

/**
 * A Factory for making Requests.
 */
public class RequestFactory {
	protected String host;
	protected int port;
	protected String defaultModuleName;
	protected Map<String, List<String>> requestHeaders;
	protected List<Observer> observers;
	
	/**
	 * Constructs a RequestFactory.
	 * 
	 * @param host	The host to connect to.
	 * @param port	The port to connect to.
	 */
	public RequestFactory(String host, int port) {
		this.host = host;
		this.port = port;
		this.defaultModuleName = null;
		this.requestHeaders = new HashMap<String, List<String>>();
		this.observers = new ArrayList<Observer>();
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
	 * Adds a header to the request headers.
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
	 * Shorthand makesRequest method. This method makes a Request using the defaultModuleName, if set.
	 * 
	 * TODO improve javadoc, null pointer checks
	 * 
	 * @param modelName			The name of the model which you want to GET, POST, PUT, or DELETE.
	 * @param requestMethod		The requestMethod to use.
	 * 
	 * @return	A Request.
	 * 
	 * @throws MalformedURLException	If the RequestFactory was unable to make a URL to the server.
	 * @throws NullPointerException		If the defaultModuleName has not been set.
	 */
	public Request makeRequest(String modelName, Request.RequestMethod requestMethod) throws MalformedURLException, NullPointerException {
		if (defaultModuleName == null) {
			throw new NullPointerException("defaultModuleName has not been set");
		}
		
		if (modelName == null) {
			throw new NullPointerException("modelName may not be null");
		}
		
		if (requestMethod == null) {
			throw new NullPointerException("requestMethod may not be null");
		}
		
		return makeRequest(defaultModuleName, modelName, requestMethod);
	}
	
	/**
	 * Makes a Request using the moduleName and the modelName.
	 * 
	 * TODO improve javadoc, null pointer checks
	 * 
	 * @param moduleName		The name of the module which the model belongs to.
	 * @param modelName			The name of the model which you want to GET, POST, PUT, or DELETE.
	 * @param requestMethod		The requestMethod to use.
	 * 
	 * @return	A Request.
	 * 
	 * @throws MalformedURLException	If the RequestFactory was unable to make a URL to the server.
	 */
	public Request makeRequest(String moduleName, String modelName, Request.RequestMethod requestMethod) throws MalformedURLException {
		// construct request
		URL url = new URL("http", host, port, "/" + moduleName + "/" + modelName); //TODO switch to https
		Request request = new Request(url, requestMethod);
		
		// add observers to request
		Iterator<Observer> iObservers = observers.iterator();
		while (iObservers.hasNext()) {
			request.addObserver(iObservers.next());
		}
		
		// add headers to request
		Iterator<String> iHeaderKeys = requestHeaders.keySet().iterator();
		Iterator<String> iHeaderValues;
		String cHeaderKey;
		while (iHeaderKeys.hasNext()) {
			cHeaderKey = iHeaderKeys.next();
			iHeaderValues = requestHeaders.get(cHeaderKey).iterator();
			
			while (iHeaderValues.hasNext()) {
				request.addRequestHeader(cHeaderKey, iHeaderValues.next());
			}
		}
		
		return request;
	}
	
	/**
	 * Sets the defaultModuleName.
	 * 
	 * @param defaultModuleName		Default name of the module which is used with the shorthand makeRequest method.
	 */
	public void setDefaultModuleName(String defaultModuleName) {
		this.defaultModuleName = defaultModuleName;
	}
}

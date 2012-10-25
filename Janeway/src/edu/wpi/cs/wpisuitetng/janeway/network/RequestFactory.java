package edu.wpi.cs.wpisuitetng.janeway.network;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A Factory for making Requests.
 * 
 * TODO Add set default headers method
 * TODO Add add default observer method?
 * TODO Methods for GET, POST, PUT, DELETE request creation?
 * TODO Maybe change module name to command? Consult team (maybe core team) about this.
 */
public class RequestFactory {
	private String host;
	private int port;
	private String defaultModuleName;
	
	public RequestFactory(String host, int port) {
		this.host = host;
		this.port = port;
		this.defaultModuleName = null;
	}
	
	/**
	 * Shorthand makesRequest method. This method makes a Request using the defaultModuleName, if set.
	 * 
	 * TODO improve javadoc, null pointer checks
	 * 
	 * @param modelName		The name of the model which you want to GET, POST, PUT, or DELETE.
	 * 
	 * @return	A Request.
	 * 
	 * @throws MalformedURLException	If the RequestFactory was unable to make a URL to the server.
	 * @throws NullPointerException		If the defaultModuleName has not been set.
	 */
	public Request makeRequest(String modelName) throws MalformedURLException, NullPointerException {
		if (defaultModuleName == null) {
			throw new NullPointerException("defaultModuleName has not been set");
		}
		return makeRequest(defaultModuleName, modelName);
	}
	
	/**
	 * Makes a Request using the moduleName and the modelName.
	 * 
	 * TODO improve javadoc, null pointer checks
	 * 
	 * @param moduleName	The name of the module which the model belongs to.
	 * @param modelName		The name of the model which you want to GET, POST, PUT, or DELETE.
	 * 
	 * @return	A Request.
	 * 
	 * @throws MalformedURLException	If the RequestFactory was unable to make a URL to the server.
	 */
	public Request makeRequest(String moduleName, String modelName) throws MalformedURLException {
		URL url = new URL("http", host, port, "/" + moduleName + "/" + modelName); //TODO switch to https
		return new Request(url);
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

package edu.wpi.cs.wpisuitetng.network;

import java.net.MalformedURLException;

import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;

/**
 * Singleton for managing Network configuration.
 */
public class Network {
	/** The singleton instance of Network. */
	private static Network instance = null;
	
	/** The default NetworkConfiguration. */
	protected NetworkConfiguration defaultNetworkConfiguration;
	
	/**
	 * Constructs a new Network
	 */
	protected Network() {
		//TODO finish this
		//TODO should we create a blank NetworkConfiguration as the default?
	}
	
	/**
	 * Makes a Request using the defaultNetworkConfiguration, the given path, and the given requestMethod.
	 * 
	 * @param path				The core API subpath to make a request to.
	 * @param requestMethod		The requestMethod to use.
	 * 
	 * @return	A Request.
	 * 
	 * @throws NullPointerException		If the requestMethod is null.
	 */
	public Request makeRequest(String path, Request.RequestMethod requestMethod) throws MalformedURLException, NullPointerException {		
		if (requestMethod == null) {
			throw new NullPointerException("requestMethod may not be null");
		}
		
		return new Request(defaultNetworkConfiguration, path, requestMethod);
	}
	
	/**
	 * Sets the default NetworkConfiguration.
	 * 
	 * @param config	The new default NetworkConfiguration.
	 * 
	 * @throws NullPointerException		If the config parameter that has been passed is null.
	 */
	public void setDefaultNetworkConfiguration(NetworkConfiguration config) throws NullPointerException {
		if (config == null) {
			throw new NullPointerException("The config parameter may not be null.");
		}
		
		this.defaultNetworkConfiguration = config;
	}
	
	/**
	 * Replaces the Network instance with the provided instance. This method can be
	 * used to replace instance with subclasses of Network for testing purposes.
	 * @param network the new Network instance
	 */
	public static void initNetwork(Network network) {
		instance = network;
	}
	
	/**
	 * Returns the default network configuration. Note: this does not return a copy. Any modifications made 
	 * to the NetworkConfiguration returned by this method will affect the default network configuration.
	 * 
	 * @return	The default NetworkConfiguration.
	 */
	public NetworkConfiguration getDefaultNetworkConfiguration() {
		if (defaultNetworkConfiguration == null) {
			// TODO should we log it or throw an exception?
		}
		
		return defaultNetworkConfiguration;
	}
	
	/**
	 * Returns the singleton instance of Network.
	 * 
	 * @return The singleton instance of Network.
	 */
	public static Network getInstance() {
		if (instance == null) {
			instance = new Network();
		}
		
		return instance;
	}
}

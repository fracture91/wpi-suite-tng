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
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.network;

import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

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
	protected Network() {}
	
	/**
	 * Makes a Request using the defaultNetworkConfiguration, the given path, and the given requestMethod.
	 * 
	 * @param path				The core API subpath to make a request to.
	 * @param requestMethod		The requestMethod to use.
	 * 
	 * @return	A Request.
	 * 
	 * @throws RuntimeException			If there is an error using the configured URL.
	 * @throws NullPointerException		If the requestMethod is null or there is a configuration error.
	 */
	public Request makeRequest(String path, HttpMethod requestMethod) {		
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
	 * Returns a copy of the default NetworkConfiguration.
	 * 
	 * @return	The default NetworkConfiguration.
	 * 
	 * @throws RuntimeException if the defaultNetworkConfiguration is null.
	 */
	public NetworkConfiguration getDefaultNetworkConfiguration() {
		if (defaultNetworkConfiguration == null) {
			throw new RuntimeException("Default network configuration is null.");
		}
		
		return new NetworkConfiguration(defaultNetworkConfiguration);
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

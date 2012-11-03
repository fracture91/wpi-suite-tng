package edu.wpi.cs.wpisuitetng.janeway;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A Singleton to keep track of the configuration
 * of the Janeway client. Right now it just stores
 * the URL of the core.
 *
 */
public class Configuration {

	/** The Singleton Configuration instance */
	protected static Configuration instance = null;
	
	/** The URL of the core server */
	protected static URL coreURL;
	
	private static final String defaultURL = "http://localhost:8080/";
	
	/**
	 * Construct a new Configuration with the default settings
	 */
	protected Configuration() {
		try {
			coreURL = new URL(defaultURL);
		} catch (MalformedURLException e) {
			// if this throws, we're gonna have a bad time
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the Configuration instance and initializes it if necessary
	 * @return the Configuration instance
	 */
	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}
	
	/**
	 * Sets the core URL to the given value
	 * @param value the core URL
	 */
	public static void setCoreURL(URL value) {
		coreURL = value;
	}
	
	/**
	 * Returns the core URL
	 * @return the core URL
	 */
	public static URL getCoreURL() {
		return coreURL;
	}
}

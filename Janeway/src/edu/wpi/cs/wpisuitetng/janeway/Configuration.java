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
	private static final Configuration instance = new Configuration();
	
	private static final String defaultURL = "http://localhost:8080/";
	
	/** The URL of the core server */
	private URL coreURL;
	
	/**
	 * Construct a new Configuration with the default settings
	 */
	private Configuration() {
		try {
			coreURL = new URL(defaultURL);
		} catch (MalformedURLException e) {
			// if this throws, we're gonna have a bad time
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the Configuration instance
	 */
	public static Configuration getInstance() {
		return instance;
	}
	
	/**
	 * Sets the core URL to the given value
	 * @param value the core URL
	 */
	public void setCoreURL(URL value) {
		coreURL = value;
	}
	
	/**
	 * @return the core URL
	 */
	public URL getCoreURL() {
		return coreURL;
	}
}

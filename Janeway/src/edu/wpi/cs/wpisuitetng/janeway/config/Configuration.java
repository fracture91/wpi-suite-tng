package edu.wpi.cs.wpisuitetng.janeway.config;

import java.io.Serializable;
import java.net.URL;

/**
 * Stores the configuration of the Janeway client. This class is Serializable.
 *
 */
public class Configuration implements Serializable {
	
	/** UID for serialization purposes */
	private static final long serialVersionUID = -1016315397882055084L;
	
	/** The URL of the WPI Suite core server */
	URL coreUrl;
	
	/** The user name last entered */
	String userName = "";
	
	/** The name of the last project entered */
	String projectName = "";
}

package edu.wpi.cs.wpisuitetng.janeway.config;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Stores the configuration of the Janeway client. This class is Serializable.
 *
 */
public class Configuration implements Serializable {
	
	/** UID for serialization purposes */
	private static final long serialVersionUID = -1016315397882055084L;
	
	/*=====================================================
	 * Configuration items (fields)
	 *=====================================================*/
	
	/** The URL of the WPI Suite core server */
	URL coreUrl;
	
	/** The user name last entered */
	String userName = "";
	
	/** The name of the last project entered */
	String projectName = "";
	
	
	/*=====================================================
	 * Getters and setters for configuration items
	 *=====================================================*/
	
	/**
	 * @return the coreUrl
	 */
	public URL getCoreUrl() {
		return coreUrl;
	}
	
	/**
	 * @param coreUrl a string represntation of the coreUrl to set
	 */
	public void setCoreUrl(String coreUrl) {
		try {
			this.coreUrl = new URL(coreUrl);
		}
		catch (MalformedURLException e) {
			System.out.println("ERROR: Bad core url!");
			e.printStackTrace();
		}
	}

	/**
	 * @param coreUrl the coreUrl to set
	 */
	public void setCoreUrl(URL coreUrl) {
		this.coreUrl = coreUrl;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}

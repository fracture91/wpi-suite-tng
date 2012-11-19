package edu.wpi.cs.wpisuitetng.janeway.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Manages the configuration of the Janeway client. Persists the configuration
 * in the form of a serialized Configuration class stored in janeway.conf
 *
 */
public class ConfigManager {
	
	/** The file name to use when storing the configuration */
	public static final String configFileName = "./janeway.conf";

	/** The singleton instance of ConfigMgr */
	private static ConfigManager instance = null;

	/** The configuration of the client */
	private static Configuration config;

	/**
	 * Constructs a new ConfigMgr (only one can exist)
	 */
	private ConfigManager() {
		config = new Configuration();

		// Set the default core URL
		config.setCoreUrl("http://localhost:8080");
	}
	
	/**
	 * Returns the configuration
	 * @return the configuration
	 */
	public static Configuration getConfig() {
		getInstance();
		return config;
	}
	
	/**
	 * Returns the singleton instance of ConfigManager
	 * @return the singleton instance of ConfigManager
	 */
	public static ConfigManager getInstance() {
		if (instance == null) {
			loadConfig();
		}
		return instance;
	}
	
	/**
	 * Loads the configuration from a file
	 */
	public static void loadConfig() {
		// if instance is null, construct the ConfigMgr
		if (instance == null) {
			instance = new ConfigManager();
		}
		
		// Load the config from the configuration file if it exists
		try {
			FileInputStream fin = new FileInputStream(configFileName);
			ObjectInputStream in = new ObjectInputStream(fin);
			config = (Configuration) in.readObject();
			in.close();
			fin.close();
		}
		catch (FileNotFoundException e) { // there is no config file, create a new Configuration
			config = new Configuration();
			config.setCoreUrl("http://localhost:8080");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			System.out.println("An error occurred reading the Janeway config from a file!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the configuration to a file
	 */
	public static void writeConfig() {
		// Check that the configuration was constructed before trying to write it
		if (config == null) {
			throw new RuntimeException("Tried to write Janeway config before it was created!");
		}
		
		// Write the configuration
		try {
			FileOutputStream fout = new FileOutputStream(configFileName);
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(config);
			out.close();
			fout.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not open/create the Janeway config file!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("An error occurred writing the Janeway config to a file!");
			e.printStackTrace();
		}
		
	}
}

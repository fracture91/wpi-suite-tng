/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Chris Casola
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.janeway;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.JanewayFrame;
import edu.wpi.cs.wpisuitetng.janeway.gui.login.LoginController;
import edu.wpi.cs.wpisuitetng.janeway.gui.login.LoginFrame;
import edu.wpi.cs.wpisuitetng.janeway.modules.DummyModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.ModuleLoader;

/**
 * The client launcher class, contains the main method that
 * constructs the GUI.
 *
 */
public class Janeway {
	
	/** List containing all modules */
	protected static List<IJanewayModule> modules;

	/**
	 * Instantiate the main GUI frame
	 */
	public static void main(final String[] args) {
		
		// Set the look and feel to cross-platform so the UI looks
		// the same across operating systems
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch (Exception e) {
			System.out.println("Error setting UI manager to cross-platform!");
			e.printStackTrace();
		}
		
		// Load modules
		ModuleLoader<IJanewayModule> moduleLoader = new ModuleLoader<IJanewayModule>("./modules.conf");
		modules = moduleLoader.getModules();
		modules.add(new DummyModule());
		
		// Check for modules
		if (modules.size() < 1) {
			System.out.println("WARNING: No modules were loaded, be sure the correct config file\nis referenced and jar files have been created.");
		}
		
		// Start the GUI
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final JanewayFrame gui = JanewayFrame.initialize(modules);
				final LoginFrame loginGui = new LoginFrame("Janeway");
				
				if (args.length > 0 && args[0].equals("-nologin")) {
					loginGui.setVisible(false);
					gui.setVisible(true);
				}
				else {
					loginGui.setVisible(true);
					gui.setVisible(false);
					loginGui.getConnectButton().addActionListener(new LoginController(gui, loginGui));
				}
			}
		});
	}
	
	/**
	 * Dynamically load the modules to include and add them to the
	 * modules list.
	 * 
	 * TODO: Dynamic loading is currently reading full class names
	 * from the modules.conf file. In the future, this code should
	 * just look in a modules directory for jar files containing
	 * JanewayModule classes.
	 * 
	 * @return a list of modules
	 */
	public static List<IJanewayModule> getModules() {
		BufferedReader inFile; /* the module config file */
		String modPackage; /* the location of the current class to load */
		IJanewayModule currMod; /* the current module object */
		ClassLoader classLoader = Janeway.class.getClassLoader();
		List<IJanewayModule> retVal = new ArrayList<IJanewayModule>(); /* The list of modules to be returned */
		
		// Attempt to dynamically load the modules, based on the contents of
		// the modules.conf file
		try {
			inFile = new BufferedReader(new FileReader("modules.conf"));
			while ((modPackage = inFile.readLine()) != null) { // read the next Class name from the file
				Class<?> modClass = classLoader.loadClass(modPackage); // load the class
				currMod = (IJanewayModule) modClass.newInstance(); // instantiate the class and make it a IJanewayModule
				retVal.add(currMod); // add the new object to the return list
				System.out.println("Loaded class: " + modPackage);
			}
			inFile.close();
		}
		catch (FileNotFoundException fe) {
			System.out.println("Could not find module config file!");
			System.exit(1);
		}
		catch (IOException e) {
			System.out.println("An error occurred reading the config file! IO exception.");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			System.out.println("An error occurred reading the config file! Class not found.");
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			System.out.println("An error occurred instantiating the module class!");
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			System.out.println("An error occurred instantiating the module class!");
			e.printStackTrace();
		}		
		
		return retVal;
	}
}

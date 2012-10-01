package edu.wpi.cs.wpisuitetng.janeway;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.janeway.models.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.views.JanewayFrame;
import edu.wpi.cs.wpisuitetng.janeway.views.LoginView;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.JanewayModule;

/**
 * The client launcher class, contains the main method that
 * constructs the GUI.
 *
 */
public class JanewayGUILauncher {
	
	/** List containing all modules */
	protected static List<IJanewayModule> modules;

	/**
	 * Instantiate the main GUI frame
	 */
	public static void main(String[] args) {
		
		// Build list of modules
		modules = getModules();
		
		// 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final JanewayFrame gui = new JanewayFrame(modules);
				final LoginView loginGui = new LoginView("Janeway");
				loginGui.setVisible(true);
				gui.setVisible(false);
				loginGui.getConnectButton().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						gui.setVisible(true);
						loginGui.dispose();
					}
				});
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
		ClassLoader classLoader = JanewayGUILauncher.class.getClassLoader();
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

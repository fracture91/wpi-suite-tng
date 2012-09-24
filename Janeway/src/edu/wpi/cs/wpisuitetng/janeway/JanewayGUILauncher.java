package edu.wpi.cs.wpisuitetng.janeway;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.janeway.models.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.views.JanewayFrame;
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
				JanewayFrame gui = new JanewayFrame(modules);
				gui.setVisible(true);
			}
		});
	}
	
	/**
	 * Build the list of modules to include
	 * TODO Dynamically load the modules
	 * @return a list of modules
	 */
	public static List<IJanewayModule> getModules() {
		// The list of modules
		List<IJanewayModule> retVal = new ArrayList<IJanewayModule>();
		
		// Defect tracker
		IJanewayModule defectTracker = new JanewayModule();
		
		// Dummy module
		IJanewayModule dummyModule = new DummyModule();
		
		// Fill the list
		retVal.add(defectTracker);
		retVal.add(dummyModule);
		
		return retVal;
	}
}

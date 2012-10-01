package edu.wpi.cs.wpisuitetng.modules.defecttracker.controllers;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.views.MainTabView;

/**
 * This class provides convenient methods for controlling the given MainTabView.
 * Keep in mind that this controller is visible as a public field in the module.
 */
public class MainTabController {
	
	MainTabView view;
	
	public MainTabController(MainTabView view) {
		this.view = view;
	}
	
	/**
	 * Create a tab that shows the form for creating a new defect.
	 */
	public void addCreateDefectTab() {
		this.view.addTab("Create Defect", new ImageIcon(), new JPanel(), "Create a new defect");
		this.view.setSelectedIndex(this.view.getTabCount() - 1);
	}
	
	/**
	 * Create a tab that shows the defect with the given ID.
	 * @param id The ID of the defect to show in the tab
	 */
	public void addDefectTab(int id) {
		// TODO
	}
	
}

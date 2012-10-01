package edu.wpi.cs.wpisuitetng.modules.defecttracker.controllers;

import javax.swing.ImageIcon;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.views.CreateDefectView;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.views.MainTabView;

/**
 * This class provides convenient methods for controlling the given MainTabView.
 * Keep in mind that this controller is visible as a public field in the module.
 */
public class MainTabController {
	
	private MainTabView view;
	
	public MainTabController(MainTabView view) {
		this.view = view;
	}
	
	/**
	 * Create a tab that shows the form for creating a new defect.
	 */
	public void addCreateDefectTab() {
		this.view.addTab("Create Defect", new ImageIcon(), new CreateDefectView(), "Create a new defect");
		this.view.setSelectedIndex(this.view.getTabCount() - 1);
	}
	
	/**
	 * Create a tab that shows the defect with the given ID.
	 * @param id The ID of the defect to show in the tab
	 */
	public void addDefectTab(int id) {
		// TODO
	}
	
	/**
	 * Add a change listener to the view this is controlling.
	 * @param listener the ChangeListener that should receive ChangeEvents
	 */
	public void addChangeListener(ChangeListener listener) {
		this.view.addChangeListener(listener);
	}
	
}

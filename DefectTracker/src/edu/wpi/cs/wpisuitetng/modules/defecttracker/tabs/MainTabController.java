package edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.create.CreateDefectView;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.SearchDefectsView;

/**
 * Controls the behavior of a given MainTabView.
 * Provides convenient public methods for controlling the MainTabView.
 * Keep in mind that this controller is visible as a public field in the module.
 */
public class MainTabController {
	
	private MainTabView view;
	
	public MainTabController(MainTabView view) {
		this.view = view;
		this.view.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				MainTabController.this.onMouseClick(event);
			}
		});
	}
	
	/**
	 * Create a tab that shows the form for creating a new defect.
	 */
	public void addCreateDefectTab() {
		this.view.addTab("Create Defect", new ImageIcon(), new CreateDefectView(), "Create a new defect");
		this.view.setSelectedIndex(this.view.getTabCount() - 1);
	}
	
	/**
	 * Create a tab that shows the UI for searching and filtering defects
	 */
	public void addSearchDefectsTab() {
		SearchDefectsView searchDefectsView = new SearchDefectsView();
		this.view.addTab("Search Defects", new ImageIcon(), searchDefectsView, "Search for defects");
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
	
	/**
	 * Close tabs upon middle clicks.
	 * @param event MouseEvent that happened on this.view
	 */
	private void onMouseClick(MouseEvent event) {
		// only want middle mouse button
		if(event.getButton() == MouseEvent.BUTTON2) {
			final int clickedIndex = view.indexAtLocation(event.getX(), event.getY());
			if(clickedIndex > -1) {
				view.removeTabAt(clickedIndex);
			}
		}
	}
	
}

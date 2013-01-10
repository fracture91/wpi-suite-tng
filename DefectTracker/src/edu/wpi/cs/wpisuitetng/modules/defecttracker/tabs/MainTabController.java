package edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs;

/**
 * Contributors:
 * AHurle
 * JPage
 */

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectView;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;

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
	 * Adds a tab.
	 * 
	 * @param title			The tab's title.
	 * @param icon			The icon for the tab.
	 * @param component		The component that will be displayed inside the tab.
	 * @param tip			The tooltip to display when the cursor hovers over the tab title.
	 */
	public void addTab(String title, Icon icon, Component component, String tip) {
		this.view.addTab(title, icon, component, tip);
		this.view.setSelectedIndex(this.view.getTabCount() - 1);
	}
	
	/**
	 * Adds a tab that displays the given defect
	 * @param defect the defect to display
	 */
	public void addDefectTab(Defect defect) {
		DefectView defectView = new DefectView(defect, Mode.EDIT);
		addTab("Defect #" + defect.getId(), new ImageIcon(), defectView, "View defect " + defect.getTitle());
		defectView.requestFocus();
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

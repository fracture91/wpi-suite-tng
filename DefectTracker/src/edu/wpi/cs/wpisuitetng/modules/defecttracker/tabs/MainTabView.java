package edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.dashboard.DashboardView;

/**
 * This tabbed pane will appear as the main content of the Defects tab.
 * It starts out showing the single Dashboard tab.
 */
@SuppressWarnings("serial")
public class MainTabView extends JTabbedPane {
	
	public MainTabView() {
		setTabPlacement(LEFT);
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
		addTab("Dashboard", new ImageIcon(), new DashboardView(),
		       "Your Dashboard - notifications, etc.");
		addTab("Search", new ImageIcon(), new JPanel(), "Search");
		addTab("#113", new ImageIcon(), new JPanel(), "Defect #113");
	}
	
	@Override
	public void removeTabAt(int index) {
		// the Dashboard tab should never get removed
		if(!(getComponentAt(index) instanceof DashboardView)) {
			super.removeTabAt(index);
		}
	}
	
}

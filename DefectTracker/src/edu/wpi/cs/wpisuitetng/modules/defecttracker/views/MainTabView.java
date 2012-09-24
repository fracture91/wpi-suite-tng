package edu.wpi.cs.wpisuitetng.modules.defecttracker.views;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

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
	}
	
}

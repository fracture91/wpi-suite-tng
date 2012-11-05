package edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs;

import java.awt.Component;

import javax.swing.Icon;
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
	public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		super.insertTab(title, icon, component, tip, index);
		// the Dashboard tab cannot be closed
		if(!(component instanceof DashboardView)) {
			setTabComponentAt(index, new ClosableTabComponent(this));
		}
	}
	
	@Override
	public void removeTabAt(int index) {
		// if a tab does not have the close button UI, it cannot be removed
		if(getTabComponentAt(index) instanceof ClosableTabComponent) {
			super.removeTabAt(index);
		}
	}
	
}

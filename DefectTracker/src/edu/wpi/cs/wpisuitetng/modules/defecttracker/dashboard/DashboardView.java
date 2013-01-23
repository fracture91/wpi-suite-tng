package edu.wpi.cs.wpisuitetng.modules.defecttracker.dashboard;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This panel will show notifications, search widgets.
 */
@SuppressWarnings("serial")
public class DashboardView extends JPanel {
	
	public DashboardView() {
		JLabel testLabel = new JLabel("This is the dashboard panel.");
		this.add(testLabel);
	}
}

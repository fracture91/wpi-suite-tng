package edu.wpi.cs.wpisuitetng.modules.postboard.view;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This panel contains the refresh button
 * 
 * @author Chris Casola
 *
 */
@SuppressWarnings("serial")
public class ToolbarPanel extends JPanel {

	/** The refresh button */
	private final JButton btnRefresh;
	
	
	/**
	 * Construct the panel.
	 */
	public ToolbarPanel() {
		
		// Make this panel transparent, we want to see the JToolbar gradient beneath it
		this.setOpaque(false);
		
		// Construct the refresh button and add it to this panel
		btnRefresh = new JButton("Refresh");
		add(btnRefresh);
	}
}

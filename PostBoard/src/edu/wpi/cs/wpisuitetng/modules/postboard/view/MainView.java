package edu.wpi.cs.wpisuitetng.modules.postboard.view;

import javax.swing.JPanel;

/**
 * This panel fills the main content area of the tab for this module. It
 * contains one inner JPanel, the BoardPanel.
 * 
 * @author Chris Casola
 *
 */
@SuppressWarnings("serial")
public class MainView extends JPanel {

	/** The panel containing the post board */
	private final BoardPanel boardPanel;
	
	/**
	 * Construct the panel.
	 */
	public MainView() {
		// Add the board panel to this view
		boardPanel = new BoardPanel();
		add(boardPanel);
	}
}

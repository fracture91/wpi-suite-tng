package edu.wpi.cs.wpisuitetng.modules.postboard.view;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.postboard.model.PostBoardModel;

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
	 * @param boardModel 
	 */
	public MainView(PostBoardModel boardModel) {
		// Add the board panel to this view
		boardPanel = new BoardPanel(boardModel);
		add(boardPanel);
	}
}

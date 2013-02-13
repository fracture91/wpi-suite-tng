package edu.wpi.cs.wpisuitetng.modules.postboard.view;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.postboard.controller.GetMessagesController;
import edu.wpi.cs.wpisuitetng.modules.postboard.model.PostBoardModel;

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
	 * @param boardModel 
	 */
	public ToolbarPanel(PostBoardModel boardModel) {
		
		// Make this panel transparent, we want to see the JToolbar gradient beneath it
		this.setOpaque(false);
		
		// Construct the refresh button and add it to this panel
		btnRefresh = new JButton("Refresh");
		
		// Add the get messages controller to the button
		btnRefresh.addActionListener(new GetMessagesController(boardModel));
		
		// Add the button to this panel
		add(btnRefresh);
	}
}

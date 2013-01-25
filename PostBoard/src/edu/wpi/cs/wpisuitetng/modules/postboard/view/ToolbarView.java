package edu.wpi.cs.wpisuitetng.modules.postboard.view;

import javax.swing.JToolBar;

/**
 * This is the toolbar for the PostBoard module
 * 
 * @author Chris Casola
 * 
 */
@SuppressWarnings("serial")
public class ToolbarView extends JToolBar {
	
	/** The panel containing toolbar buttons */
	private final ToolbarPanel toolbarPanel;

	/**
	 * Construct this view and all components in it.
	 */
	public ToolbarView() {
		
		// Prevent this toolbar from being moved
		setFloatable(false);
		
		// Add the panel containing the toolbar buttons
		toolbarPanel = new ToolbarPanel();
		add(toolbarPanel);
	}
}

package edu.wpi.cs.wpisuitetng.modules.defecttracker.search;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;

/**
 * View that contains the entire defect searching interface
 */
@SuppressWarnings("serial")
public class SearchDefectsView extends JPanel implements IToolbarGroupProvider {
	
	/** Panel containing the search interface */
	protected SearchPanel mainPanel;
	
	/** The layout manager for this panel */
	protected SpringLayout layout;
	
	/**
	 * Construct the view
	 */
	public SearchDefectsView() {
		this.mainPanel = new SearchPanel();
		
		// Construct the layout manager and add constraints
		layout = new SpringLayout();
		this.setLayout(layout);
		layout.putConstraint(SpringLayout.NORTH, mainPanel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, mainPanel, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.WEST, mainPanel, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, mainPanel, 0, SpringLayout.EAST, this);
		
		// Add the mainPanel to this view
		this.add(mainPanel);
	}

	@Override
	public ToolbarGroupView getGroup() {
		return null; // there are no toolbar buttons yet
	}
}

package edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar;

import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabController;

/**
 * The Defect tab's toolbar panel.
 * Always has a group of global commands (Create Defect, Search).
 */
@SuppressWarnings("serial")
public class ToolbarView extends DefaultToolbarView {

	private JButton createDefect;
	private JButton searchDefects;
	
	/**
	 * Create a ToolbarView.
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public ToolbarView(MainTabController tabController) {
		ToolbarGroupView toolbarGroup = new ToolbarGroupView("Global");
		
		// Construct the create defect button
		createDefect = new JButton();
		createDefect.setAction(new CreateDefectAction(tabController));
		
		// Construct the search button
		searchDefects = new JButton("Search Defects");
		searchDefects.setAction(new SearchDefectsAction(tabController));
		
		// Add the buttons to the toolbar
		toolbarGroup.getContent().add(createDefect);
		toolbarGroup.getContent().add(searchDefects);
		toolbarGroup.setPreferredWidth(300);
		addGroup(toolbarGroup);
	}

}

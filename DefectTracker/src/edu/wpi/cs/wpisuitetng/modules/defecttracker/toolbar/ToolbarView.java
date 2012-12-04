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
		createDefect = new JButton();
		createDefect.setAction(new CreateDefectAction(tabController));
		searchDefects = new JButton("Search Defects");
		toolbarGroup.getContent().add(createDefect);
		toolbarGroup.getContent().add(searchDefects);
		toolbarGroup.setPreferredWidth(300);
		addGroup(toolbarGroup);
	}

}

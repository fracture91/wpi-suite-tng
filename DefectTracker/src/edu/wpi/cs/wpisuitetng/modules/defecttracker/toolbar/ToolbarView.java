package edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.JPlaceholderTextField;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabController;

/**
 * The Defect tab's toolbar panel.
 * Always has a group of global commands (Create Defect, Search).
 */
@SuppressWarnings("serial")
public class ToolbarView extends DefaultToolbarView {

	private JButton createDefect;
	private JButton searchDefects;
	private JPlaceholderTextField searchField;
	
	/**
	 * Create a ToolbarView.
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public ToolbarView(MainTabController tabController) {

		// Construct the content panel
		JPanel content = new JPanel();
		SpringLayout layout  = new SpringLayout();
		content.setLayout(layout);
		content.setOpaque(false);
				
		// Construct the create defect button
		createDefect = new JButton();
		createDefect.setAction(new CreateDefectAction(tabController));
		
		// Construct the search button
		searchDefects = new JButton("Search Defects");
		searchDefects.setAction(new SearchDefectsAction(tabController));
		
		// Construct the search field
		searchField = new JPlaceholderTextField("Lookup by ID", 15);
		searchField.setAction(new LookupDefectAction(new LookupDefectController()));
		
		// Add the buttons to the content panel
		layout.putConstraint(SpringLayout.NORTH, createDefect, 8, SpringLayout.NORTH, content);
		layout.putConstraint(SpringLayout.WEST, createDefect, 8, SpringLayout.WEST, content);
		layout.putConstraint(SpringLayout.WEST, searchDefects, 10, SpringLayout.EAST, createDefect);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, searchDefects, 0, SpringLayout.VERTICAL_CENTER, createDefect);
		layout.putConstraint(SpringLayout.NORTH, searchField, 15, SpringLayout.SOUTH, createDefect);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, searchField, 5, SpringLayout.EAST, createDefect);
		
		content.add(createDefect);
		content.add(searchDefects);
		content.add(searchField);
		
		ToolbarGroupView toolbarGroup = new ToolbarGroupView("Home", content);
		toolbarGroup.setPreferredWidth(270);
		addGroup(toolbarGroup);
	}

}

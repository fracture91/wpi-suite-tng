package edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.JanewayModule;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabController;

/**
 * The Defect tab's toolbar panel.
 * Always has a group of global commands (Create Defect, Search).
 */
@SuppressWarnings("serial")
public class ToolbarView extends DefaultToolbarView implements ActionListener {

	private JButton createDefect;
	private JButton searchDefects;
	private MainTabController tabController;
	
	/**
	 * Create a ToolbarView.
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public ToolbarView(MainTabController tabController) {
		this.tabController = tabController;
		
		ToolbarGroupView toolbarGroup = new ToolbarGroupView("Global");
		createDefect = new JButton("Create Defect");
		createDefect.addActionListener(this);
		searchDefects = new JButton("Search Defects");
		searchDefects.addActionListener(this);
		toolbarGroup.getContent().add(createDefect);
		toolbarGroup.getContent().add(searchDefects);
		toolbarGroup.setPreferredWidth(300);
		addGroup(toolbarGroup);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createDefect) {
			tabController.addCreateDefectTab();
		}
	}

}

package edu.wpi.cs.wpisuitetng.modules.defecttracker.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import edu.wpi.cs.wpisuitetng.janeway.views.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.views.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.JanewayModule;

/**
 * The Defect tab's toolbar panel.
 * Always has a group of global commands (Create Defect, Search).
 */
@SuppressWarnings("serial")
public class ToolbarView extends DefaultToolbarView implements ActionListener {

	private JButton createDefect;
	private JButton searchDefects;
	private JanewayModule module;
	
	/**
	 * Create a ToolbarView.
	 * @param module The module this is associated with.
	 */
	public ToolbarView(JanewayModule module) {
		this.module = module;
		
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
			module.mainTabController.addCreateDefectTab();
		}
	}

}

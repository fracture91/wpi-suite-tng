package edu.wpi.cs.wpisuitetng.janeway.maingui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.wpi.cs.wpisuitetng.janeway.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.JanewayTabModel;

/**
 * The main application panel.
 * Holds each modules' tabs, each of which contain a toolbar and main component.
 */
@SuppressWarnings("serial")
public class TabPanel extends JPanel {

	/** The tab pane */
	protected JTabbedPane tabbedPane;
	protected List<IJanewayModule> modules;
	
	public TabPanel(List<IJanewayModule> modules) {
		this.modules = modules;
		this.tabbedPane = new JTabbedPane();
		
		// Populate tabs
		addModules();
		
		// Set the layout and add the tabs to the panel
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);
	}
	
	public void addModules() {
		for (IJanewayModule ijm : modules) {
			for (JanewayTabModel jtm : ijm.getTabs()) {
				// create a panel to hold the buttons and tab contents
				JPanel newPanel = new JPanel();
				newPanel.setLayout(new BorderLayout());
				
				// Set the height of the toolbar to 100px
				jtm.getToolbar().setPreferredSize(new Dimension(TabPanel.WIDTH, 100));
				
				// Add the toolbar panel and main panel for the module to the new panel
				newPanel.add(jtm.getToolbar(), BorderLayout.PAGE_START);
				newPanel.add(jtm.getMainComponent(), BorderLayout.CENTER);
				
				// Add a tab to the tabbed pane containing the new panel
				tabbedPane.addTab(jtm.getName(), jtm.getIcon(), newPanel);
			}
		}
	}
}

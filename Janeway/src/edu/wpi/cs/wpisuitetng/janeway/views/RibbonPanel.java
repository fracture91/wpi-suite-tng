package edu.wpi.cs.wpisuitetng.janeway.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.wpi.cs.wpisuitetng.janeway.models.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.models.JanewayTabModel;

/**
 * The main application panel, containing the ribbon bar
 * and a main panel for each module.
 *
 */
@SuppressWarnings("serial")
public class RibbonPanel extends JPanel {

	/** The tab pane */
	protected JTabbedPane tabbedPane;
	protected List<IJanewayModule> modules;
	
	public RibbonPanel(List<IJanewayModule> modules) {
		// Set fields
		this.modules = modules;
		this.tabbedPane = new JTabbedPane();
		
		// Populate ribbon
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
				
				// Set the height of the ribbon bar to 100px
				jtm.getRibbon().setPreferredSize(new Dimension(RibbonPanel.WIDTH, 100));
				
				// Add the ribbon panel and main panel for the module to the new panel
				newPanel.add(jtm.getRibbon(), BorderLayout.PAGE_START);
				newPanel.add(jtm.getMainPanel(), BorderLayout.CENTER);
				
				// Add a tab to the tabbed pane containing the new panel
				tabbedPane.addTab(jtm.getName(), jtm.getIcon(), newPanel);
			}
		}
	}
}

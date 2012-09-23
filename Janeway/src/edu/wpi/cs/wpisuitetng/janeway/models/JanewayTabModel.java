package edu.wpi.cs.wpisuitetng.janeway.models;

import javax.swing.Icon;
import javax.swing.JPanel;

/**
 * Represents a tab in the Janeway interface.
 * Tabs have a name, icon, ribbon JPanel, and main JPanel.
 */
public class JanewayTabModel {
	
	private String name;
	private Icon icon;
	private JPanel ribbon;
	private JPanel mainPanel;
	
	/**
	 * Construct a tab model with the given properties.
	 */
	public JanewayTabModel(String name, Icon icon, JPanel ribbon, JPanel mainPanel) {
		this.name = name;
		this.icon = icon;
		this.ribbon = ribbon;
		this.mainPanel = mainPanel;
	}
	
	/**
	 * @return The name that will appear on the module's tab (e.g. "Defect").
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The icon that will appear on the left of the tab.
	 */
	public Icon getIcon() {
		return icon;
	}
	
	/**
	 * @return The JPanel that will appear in the "ribbon" menu below the tab bar.
	 */
	public JPanel getRibbon() {
		return ribbon;
	}
	
	/**
	 * @return The main JPanel that appears below the ribbon menu.
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
}

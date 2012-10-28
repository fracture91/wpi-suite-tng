package edu.wpi.cs.wpisuitetng.janeway.models;

import javax.swing.Icon;
import javax.swing.JComponent;

/**
 * Represents a tab in the Janeway interface.
 * Tabs have a name, icon, toolbar JComponent, and main JComponent.
 */
public class JanewayTabModel {
	
	private String name;
	private Icon icon;
	private JComponent toolbar;
	private JComponent mainComponent;
	
	/**
	 * Construct a tab model with the given properties.
	 */
	public JanewayTabModel(String name, Icon icon, JComponent toolbar, JComponent mainComponent) {
		this.name = name;
		this.icon = icon;
		this.toolbar = toolbar;
		this.mainComponent = mainComponent;
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
	 * @return The JComponent that will appear in the toolbar below the tab bar.
	 */
	public JComponent getToolbar() {
		return toolbar;
	}
	
	/**
	 * @return The main JComponent that appears below the toolbar menu.
	 */
	public JComponent getMainComponent() {
		return mainComponent;
	}
	
}

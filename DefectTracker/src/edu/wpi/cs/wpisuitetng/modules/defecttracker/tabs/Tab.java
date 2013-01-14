package edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs;

import java.awt.Component;

import javax.swing.Icon;

/**
 * A wrapper class for MainTabView that can be given to components within that view
 * in order to allow them to easily change their titles and icons.
 */
public class Tab {

	private MainTabView view;
	private Component tabComponent;

	/**
	 * Create a Tab identified by the given MainTabView and tabComponent.
	 * 
	 * @param view The MainTabView this Tab belongs to
	 * @param tabComponent The tabComponent for this Tab
	 */
	public Tab(MainTabView view, Component tabComponent) {
		this.view = view;
		this.tabComponent = tabComponent;
	}
	
	private int getIndex() {
		return view.indexOfTabComponent(tabComponent);
	}
	
	public String getTitle() {
		return view.getTitleAt(getIndex());
	}
	
	public void setTitle(String title) {
		view.setTitleAt(getIndex(), title);
		tabComponent.invalidate(); // needed to make tab shrink with smaller title
	}
	
	public Icon getIcon() {
		return view.getIconAt(getIndex());
	}
	
	public void setIcon(Icon icon) {
		view.setIconAt(getIndex(), icon);
	}
	
	public String getToolTipText() {
		return view.getToolTipTextAt(getIndex());
	}
	
	public void setToolTipText(String toolTipText) {
		view.setToolTipTextAt(getIndex(), toolTipText);
	}
	
	public Component getComponent() {
		return view.getComponentAt(getIndex());
	}
	
	public void setComponent(Component component) {
		view.setComponentAt(getIndex(), component);
	}
	
}

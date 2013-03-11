/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs;

import java.awt.Component;

import javax.swing.Icon;

/**
 * Holds values given to it, but doesn't actually change the given MainTabView.
 */
public class DummyTab extends Tab {

	private String title;
	private Icon icon;
	private String toolTipText;
	private Component component;

	/**
	 * Construct a DummyTab - arguments ignored
	 * 
	 * @param view ignored
	 * @param tabComponent ignored
	 */
	public DummyTab(MainTabView view, Component tabComponent) {
		super(null, null);
	}
	
	/**
	 * Same as DummyTab(null, null)
	 */
	public DummyTab() {
		this(null, null);
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public Icon getIcon() {
		return icon;
	}
	
	@Override
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	
	@Override
	public String getToolTipText() {
		return toolTipText;
	}
	
	@Override
	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}
	
	public Component getComponent() {
		return component;
	}
	
	public void setComponent(Component component) {
		this.component = component;
	}

}

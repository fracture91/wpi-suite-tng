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
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.KeyboardShortcut;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabView;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar.ToolbarController;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar.ToolbarView;

/**
 * This is where the module can define what's necessary to work correctly in Janeway.
 * The module consists of a single "Defects" tab.
 */
public class JanewayModule implements IJanewayModule {

	private ArrayList<JanewayTabModel> tabs;
	public final MainTabController mainTabController;
	public ToolbarController toolbarController;

	public JanewayModule() {
		MainTabView mainTabView = new MainTabView();
		mainTabController = new MainTabController(mainTabView);

		ToolbarView toolbarView = new ToolbarView(mainTabController);
		toolbarController = new ToolbarController(toolbarView, mainTabController);

		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Defects", new ImageIcon(), toolbarView, mainTabView);
		tabs.add(tab);

		// add keyboard shortcuts to defects tab
		registerKeyboardShortcuts(tab);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "Defect Tracker";
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public ArrayList<JanewayTabModel> getTabs() {
		return tabs;
	}

	@SuppressWarnings("serial")
	private void registerKeyboardShortcuts(JanewayTabModel tab) {
		String osName = System.getProperty("os.name").toLowerCase();
		
		// control + tab: switch to right tab
		tab.addKeyboardShortcut(new KeyboardShortcut(KeyStroke.getKeyStroke("control TAB"), new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainTabController.switchToRightTab();
			}
		}));
		
		// control + shift + tab: switch to left tab
		tab.addKeyboardShortcut(new KeyboardShortcut(KeyStroke.getKeyStroke("control shift TAB"), new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainTabController.switchToLeftTab();
			}
		}));
		
		// command + w for mac or control + w for windows: close the current tab
		if (osName.contains("mac")) {
			tab.addKeyboardShortcut(new KeyboardShortcut(KeyStroke.getKeyStroke("meta W"), new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mainTabController.closeCurrentTab();
				}
			}));
		}
		else {
			tab.addKeyboardShortcut(new KeyboardShortcut(KeyStroke.getKeyStroke("control W"), new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mainTabController.closeCurrentTab();
				}
			}));
		}
	}
}

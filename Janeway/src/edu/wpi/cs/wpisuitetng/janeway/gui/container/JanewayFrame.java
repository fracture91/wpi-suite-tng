/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Chris Casola
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.janeway.gui.container;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.KeyboardShortcut;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;

/**
 * The main application window for the Janeway client
 *
 */
@SuppressWarnings("serial")
public class JanewayFrame extends JFrame {

	/** A panel to contain the tabs */
	protected final TabPanel tabPanel;
	
	/** The singelton instance */
	private static JanewayFrame instance = null;

	/**
	 * Construct a new JanewayFrame
	 */
	private JanewayFrame(List<IJanewayModule> modules) {
		// Set window properties
		setTitle("Janeway - WPI Suite Desktop Client");
		setMinimumSize(new Dimension(800, 600)); // minimum window size is 800 x 600
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// Clean up when the window is closed
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				// write the configuration to janeway.conf
				ConfigManager.writeConfig();

				// dispose of this window
				dispose();
			}
		});

		// Set the window size and position based on screen size
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)(dim.width * 0.85);
		int height = (int)(dim.height * 0.85);
		int xPos = (dim.width - width) / 2;
		int yPos = (int)((dim.height - height) / 2 * .75);
		setBounds(xPos, yPos, width, height);
		this.setPreferredSize(new Dimension(width, height));

		// Setup the layout manager
		this.setLayout(new BorderLayout());

		// Add the tab panel
		tabPanel = new TabPanel(modules);
		this.add(tabPanel, BorderLayout.CENTER);

		// Add key event dispatcher and global shortcuts
		JanewayKeyEventDispatcher keyEventDispatcher = new JanewayKeyEventDispatcher(this, modules);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
		addGlobalKeyboardShortcuts(keyEventDispatcher);
		
	}
	
	public static JanewayFrame getInstance() {
		if (instance == null) {
			throw new RuntimeException("You must call the initialize method of JanewayFrame before calling getInstance!");
		}
		return instance;
	}
	
	public static JanewayFrame initialize(List<IJanewayModule> modules) {
		if (instance != null) {
			throw new RuntimeException("JanewayFrame is already initialized!");
		}
		instance = new JanewayFrame(modules);
		return instance;
	}

	/**
	 * @return the tab panel
	 */
	public TabPanel getTabPanel() {
		return tabPanel;
	}

	/**
	 * Add global keyboard shortcuts
	 * @param keyEventDispatcher the current key event dispatcher
	 */
	private void addGlobalKeyboardShortcuts(JanewayKeyEventDispatcher keyEventDispatcher) {
		
		// control + page down: switch to the module tab to the right
		keyEventDispatcher.addGlobalKeyboardShortcut(new KeyboardShortcut(KeyStroke.getKeyStroke("control PAGE_DOWN"), new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent event) {
				tabPanel.switchToRightTab();
			}
		}));
		
		// control + page up: switch to the module tab to the left
		keyEventDispatcher.addGlobalKeyboardShortcut(new KeyboardShortcut(KeyStroke.getKeyStroke("control PAGE_UP"), new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent event) {
				tabPanel.switchToLeftTab();
			}
		}));
	}
}

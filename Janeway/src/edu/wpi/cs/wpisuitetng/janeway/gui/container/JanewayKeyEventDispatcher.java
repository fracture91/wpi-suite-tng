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
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.janeway.gui.container;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.KeyStroke;

import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.KeyboardShortcut;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

/**
 * This class calls the {@link edu.wpi.cs.wpisuitetng.janeway.gui.widgets.KeyboardShortcut#processKeyEvent(KeyEvent)}
 * method for each KeyboardShortcut in the active tab.
 *
 */
public class JanewayKeyEventDispatcher implements KeyEventDispatcher {

	/** The main Janeway frame */
	protected final JanewayFrame mainWindow;

	/** The list of modules that were loaded at runtime */
	protected final List<IJanewayModule> modules;

	/** The list of shortcuts used globally by all modules */
	protected List<KeyboardShortcut> globalShortcuts;

	/** A flag representing the state of the windows key */
	protected boolean windowsKeyDown;

	/**
	 * Constructs a new JanewayKeyEventDispatcher
	 * @param mainWindow the main Janeway frame
	 * @param modules the list of modules that were loaded at runtime
	 */
	public JanewayKeyEventDispatcher(JanewayFrame mainWindow, List<IJanewayModule> modules) {
		this.mainWindow = mainWindow;
		this.modules = modules;
		this.globalShortcuts = new ArrayList<KeyboardShortcut>();
		this.windowsKeyDown = false;
		mainWindow.addWindowFocusListener(new JanewayFocusListener(this));
	}

	/**
	 * Dispatches the given key event to the active tab.
	 * 
	 * @return true if the event was dispatched, false if the event should be handled
	 * by the next dispatcher. IMPORTANT: if you don't want the default action to occur
	 * for a shortcut, you must consume the event and then return true.
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(isWindowsKeyUp(event)) {
			KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(event);
			for (KeyboardShortcut shortcut : globalShortcuts) {
				if (shortcut.processKeyEvent(keyStroke)) {
					event.consume();
					return true;
				}
			}

			for (IJanewayModule module : modules) {
				for (JanewayTabModel tabModel : module.getTabs()) {
					if (tabModel.getName().equals(mainWindow.getTabPanel().getTabbedPane().getSelectedComponent().getName())) {
						for (KeyboardShortcut shortcut : tabModel.getKeyboardShortcuts()) {
							if (shortcut.processKeyEvent(keyStroke)) {
								event.consume();
								return true;
							}
						}
						return false;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Adds the given shortcut to the list of global shortcuts for
	 * the Janeway client.
	 * @param shortcut the new keyboard shortcut to add
	 */
	public void addGlobalKeyboardShortcut(KeyboardShortcut shortcut) {
		globalShortcuts.add(shortcut);
	}
	
	/**
	 * Maintains the state of the windowsKeyDown flag.
	 * @param event the last keye vent
	 * @return false if the windows key is pressed, otherwise true
	 */
	private synchronized boolean isWindowsKeyUp(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_WINDOWS) {
			if (event.getID() == KeyEvent.KEY_PRESSED) {
				windowsKeyDown = true;
			}
			else if (event.getID() == KeyEvent.KEY_RELEASED) {
				windowsKeyDown = false;
			}
		}
		return !windowsKeyDown;
	}
	
	/**
	 * Sets the value of the windows key flag
	 * @param value the new value of the flag
	 */
	protected synchronized void setWindowsKeyFlag(boolean value) {
		windowsKeyDown = value;
	}
}

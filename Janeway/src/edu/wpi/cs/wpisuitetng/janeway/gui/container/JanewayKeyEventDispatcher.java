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

	protected List<KeyboardShortcut> globalShortcuts;

	/**
	 * Constructs a new JanewayKeyEventDispatcher
	 * @param mainWindow the main Janeway frame
	 * @param modules the list of modules that were loaded at runtime
	 */
	public JanewayKeyEventDispatcher(JanewayFrame mainWindow, List<IJanewayModule> modules) {
		this.mainWindow = mainWindow;
		this.modules = modules;
		this.globalShortcuts = new ArrayList<KeyboardShortcut>();
	}

	/**
	 * Dispatches the given key event to the active tab
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(event);
		for (KeyboardShortcut shortcut : globalShortcuts) {
			shortcut.processKeyEvent(keyStroke);
		}
		
		for (IJanewayModule module : modules) {
			for (JanewayTabModel tabModel : module.getTabs()) {
				if (tabModel.getName().equals(mainWindow.getTabPanel().getTabbedPane().getSelectedComponent().getName())) {
					for (KeyboardShortcut shortcut : tabModel.getKeyboardShortcuts()) {
						shortcut.processKeyEvent(keyStroke);
					}
					return false;
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
}

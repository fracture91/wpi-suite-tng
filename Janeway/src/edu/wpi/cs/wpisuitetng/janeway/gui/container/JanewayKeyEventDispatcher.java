package edu.wpi.cs.wpisuitetng.janeway.gui.container;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.List;

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
	
	/**
	 * Constructs a new JanewayKeyEventDispatcher
	 * @param mainWindow the main Janeway frame
	 * @param modules the list of modules that were loaded at runtime
	 */
	public JanewayKeyEventDispatcher(JanewayFrame mainWindow, List<IJanewayModule> modules) {
		this.mainWindow = mainWindow;
		this.modules = modules;
	}

	/**
	 * Dispatches the given key event to the active tab
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		for (IJanewayModule module : modules) {
			for (JanewayTabModel tabModel : module.getTabs()) {
				if (tabModel.getName().equals(mainWindow.getTabPanel().getTabbedPane().getSelectedComponent().getName())) {
					for (KeyboardShortcut shortcut : tabModel.getKeyboardShortcuts()) {
						shortcut.processKeyEvent(event);
					}
					return false;
				}
			}
		}
		return false;
	}
}

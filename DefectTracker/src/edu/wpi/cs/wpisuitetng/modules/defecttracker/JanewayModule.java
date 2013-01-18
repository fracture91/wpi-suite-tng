package edu.wpi.cs.wpisuitetng.modules.defecttracker;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

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
		//int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		int shortcutKeyMask = KeyEvent.CTRL_DOWN_MASK;

		// control + tab: switch to right tab
		tab.getKeyboardShortcuts().add(new KeyboardShortcut(shortcutKeyMask, KeyEvent.SHIFT_DOWN_MASK, KeyEvent.KEY_PRESSED, KeyEvent.VK_TAB, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainTabController.switchToRightTab();
			}
		}));

		// control + shift + tab: switch to left tab
		tab.getKeyboardShortcuts().add(new KeyboardShortcut(shortcutKeyMask | KeyEvent.SHIFT_DOWN_MASK, 0, KeyEvent.KEY_PRESSED, KeyEvent.VK_TAB, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainTabController.switchToLeftTab();
			}
		}));

		// control + w: close tab
		tab.getKeyboardShortcuts().add(new KeyboardShortcut(shortcutKeyMask, 0, KeyEvent.KEY_PRESSED, KeyEvent.VK_W, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainTabController.closeCurrentTab();
			}
		}));

		// command + w: close tab
		tab.getKeyboardShortcuts().add(new KeyboardShortcut(KeyEvent.META_DOWN_MASK, 0, KeyEvent.KEY_PRESSED, KeyEvent.VK_W, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainTabController.closeCurrentTab();
			}
		}));
	}
}

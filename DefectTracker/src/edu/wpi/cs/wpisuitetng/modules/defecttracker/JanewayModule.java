package edu.wpi.cs.wpisuitetng.modules.defecttracker;

import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

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
	
	@SuppressWarnings("serial")
	public JanewayModule() {
		MainTabView mainTabView = new MainTabView();
		mainTabController = new MainTabController(mainTabView);
		
		// Set keyboard shortcuts
		InputMap inputMap = mainTabView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = mainTabView.getActionMap();
		
		//Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, Event.CTRL_MASK), "switchTabRight");
		actionMap.put("switchTabRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainTabController.switchToRightTab();
			}
			
		});
		
		ToolbarView toolbarView = new ToolbarView(mainTabController);
		toolbarController = new ToolbarController(toolbarView, mainTabController);
		
		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Defects", new ImageIcon(), toolbarView, mainTabView);
		tabs.add(tab);
	}
	
	@Override
	public String getName() {
		return "Defect Tracker";
	}

	@Override
	public ArrayList<JanewayTabModel> getTabs() {
		return tabs;
	}

}

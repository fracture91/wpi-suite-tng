package edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabController;

/**
 * Action that calls {@link MainTabController#addSearchDefectsTab()}, default mnemonic key is D.
 */
@SuppressWarnings("serial")
public class SearchDefectsAction extends AbstractAction {
	
	private final MainTabController controller;
	
	/**
	 * Construct a search defects action
	 * @param controller the controller to call when the action is performed
	 */
	public SearchDefectsAction(MainTabController controller) {
		super("Search Defects");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_D);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.addSearchDefectsTab();
	}

}

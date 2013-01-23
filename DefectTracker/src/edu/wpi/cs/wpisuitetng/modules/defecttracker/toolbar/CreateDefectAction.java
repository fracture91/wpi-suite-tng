package edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar;

/**
 * Contributors:
 * AHurle
 * JPage
 */

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabController;

/**
 * Action that calls {@link MainTabController#addCreateDefectTab()}, default mnemonic key is C. 
 */
@SuppressWarnings("serial")
public class CreateDefectAction extends AbstractAction {

	private final MainTabController controller;
	
	/**
	 * Create a CreateDefectAction
	 * @param controller When the action is performed, controller.addCreateDefectTab() is called
	 */
	public CreateDefectAction(MainTabController controller) {
		super("Create Defect");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_C);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.addCreateDefectTab();
	}

}

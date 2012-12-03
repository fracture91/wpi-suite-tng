package edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar;

/**
 * Contributors:
 * CCasola
 * JPage
 */

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.SearchDefectsView;
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
		SearchDefectsView searchDefectsView = new SearchDefectsView();
		controller.addTab("Search Defects", new ImageIcon(), searchDefectsView, "Search for defects");
	}

}

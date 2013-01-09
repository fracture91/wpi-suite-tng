package edu.wpi.cs.wpisuitetng.modules.defecttracker.search;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class RefreshDefectsAction extends AbstractAction {
	
	protected final FilterDefectsController controller;
	
	public RefreshDefectsAction(FilterDefectsController controller) {
		super("Refresh");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_R);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.refreshData();
	}

}

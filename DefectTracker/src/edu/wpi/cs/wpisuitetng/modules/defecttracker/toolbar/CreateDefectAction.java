/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 *    JPage
 ******************************************************************************/

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

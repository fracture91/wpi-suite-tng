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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.search.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;


/**
 * Action that calls {@link RetrieveAllDefectsController#refreshData()}, default mnemonic key is R
 */
@SuppressWarnings("serial")
public class RefreshDefectsAction extends AbstractAction {
	
	/** The controller to be called when this action is performed */
	protected final RetrieveAllDefectsController controller;
	
	/**
	 * Construct a RefreshDefectsAction
	 * @param controller when the action is performed this controller's refreshData() method will be called
	 */
	public RefreshDefectsAction(RetrieveAllDefectsController controller) {
		super("Refresh");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_R);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.refreshData();
	}

}

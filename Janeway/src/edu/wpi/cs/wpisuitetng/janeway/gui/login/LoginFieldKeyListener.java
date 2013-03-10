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

package edu.wpi.cs.wpisuitetng.janeway.gui.login;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;

/**
 * Listens for enter key presses and activates the Connect button
 *
 */
public class LoginFieldKeyListener extends KeyAdapter {

	/** The connect button */
	protected JButton connectButton;
	
	public LoginFieldKeyListener(JButton connectButton) {
		super();
		this.connectButton = connectButton;
	}
	
	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
			connectButton.doClick();
		}
	}
}

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

package edu.wpi.cs.wpisuitetng.janeway.gui.container;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JanewayFocusListener extends WindowAdapter {
	
	private final JanewayKeyEventDispatcher keyDispatcher;
	
	public JanewayFocusListener(JanewayKeyEventDispatcher keyDispatcher) {
		this.keyDispatcher = keyDispatcher;
	}
	
	@Override
	public void windowGainedFocus(WindowEvent e) {
		keyDispatcher.setWindowsKeyFlag(false);
	}
}

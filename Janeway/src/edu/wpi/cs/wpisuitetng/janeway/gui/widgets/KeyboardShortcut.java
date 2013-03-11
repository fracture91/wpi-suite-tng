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

package edu.wpi.cs.wpisuitetng.janeway.gui.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;
import javax.swing.KeyStroke;

/**
 * This class performs the given action when a keyboard event is received
 * that matches the keyboard shortcut this class represents.
 *
 */
public class KeyboardShortcut {
	
	/** A KeyStroke representing the combination of keys required for this shortcut */
	protected KeyStroke keyStroke;
	
	/** The list of actions to perform when a KeyEvent is received that matches the keyStroke */
	protected final List<Action> actions;
	
	/**
	 * Constructs a new KeyboardShortcut for the combination of keys represented
	 * by the given KeyStroke.
	 * @param keyStroke a KeyStroke representing the combination of keys that make up this shortcut
	 */
	public KeyboardShortcut(KeyStroke keyStroke) {
		this(keyStroke, null);
	}
	
	/**
	 * Constructs a new KeyboardShortcut for the combination of keys represented
	 * @param keyStroke a KeyStroke representing the combination of keys that make up this shortcut
	 * @param action the action to perform when this keyboard shortcut is typed
	 */
	public KeyboardShortcut(KeyStroke keyStroke, Action action) {
		if (keyStroke == null) {
			throw new NullPointerException("The given KeyStroke cannot be null");
		}
		this.keyStroke = keyStroke;
		actions = new ArrayList<Action>();
		if (action != null) {
			actions.add(action);
		}
	}
	
	/**
	 * Determines if the given key stroke matches this shortcut. If it matches
	 * each of the actions is performed.
	 * @param keyStroke the key stroke to compare
	 */
	public boolean processKeyEvent(KeyStroke keyStroke) {
		if (this.keyStroke.equals(keyStroke)) {
			performActions();
			return true;
		}
		return false;
	}
	
	/**
	 * Adds the given action to the list of actions to perform when
	 * this KeyboardShortcut is pressed
	 * @param action the action to perform
	 */
	public void addAction(Action action) {
		actions.add(action);
	}
	
	/**
	 * Removes the given action from the list of actions to perform when
	 * this KeyboardShortcut is pressed
	 * @param action the action to remove
	 */
	public void removeAction(Action action) {
		Iterator<Action> iterator = actions.iterator();
		while (iterator.hasNext()) {
			if (iterator.next() == action) {
				iterator.remove();
				break;
			}
		}
	}
	
	private void performActions() {
		for (Action action : actions) {
			action.actionPerformed(null);
		}
	}
}

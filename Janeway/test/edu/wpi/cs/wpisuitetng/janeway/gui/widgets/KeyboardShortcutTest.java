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

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("serial")
public class KeyboardShortcutTest {

	private KeyboardShortcut keyboardShortcut;

	@Before
	public void setUp() throws Exception {
		keyboardShortcut = new KeyboardShortcut(KeyStroke.getKeyStroke("control TAB"));
	}

	/**
	 * The constructor should be able to add an action to the list of actions
	 */
	@Test
	public void constructAKeyboardShortcutWithAnAction() {
		final KeyboardShortcut keyboardShortcut = 
				new KeyboardShortcut(KeyStroke.getKeyStroke("control TAB"), new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// do nothing
					}
				});
		assertEquals(1, keyboardShortcut.actions.size());
	}
	
	/**
	 * Test that an action can be added to the KeyboardShortcut
	 */
	@Test
	public void addAnAction() {
		keyboardShortcut.addAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do nothing
			}
		});
		assertEquals(1, keyboardShortcut.actions.size());
	}
	
	/**
	 * An action should be able to be removed from a KeyboardShortcut
	 */
	@Test
	public void removeAnAction() {
		AbstractAction myAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do nothing
			}
		};
		
		AbstractAction myAction2 = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do nothing
			}
		};
		
		keyboardShortcut.addAction(myAction);
		keyboardShortcut.addAction(myAction2);
		assertEquals(2, keyboardShortcut.actions.size());
		assertTrue(keyboardShortcut.actions.contains(myAction));
		assertTrue(keyboardShortcut.actions.contains(myAction2));
		keyboardShortcut.removeAction(myAction2);
		assertEquals(1, keyboardShortcut.actions.size());
	}
	
	/**
	 * A KeyboardShortcut should recognize when a given KeyEvent matches the shortcut
	 * it represents.
	 */
	@Test
	public void respondToMatchingKeyEvent() {
		final KeyEvent keyEvent = new KeyEvent(new JPanel(), KeyEvent.KEY_PRESSED, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
		keyboardShortcut.addAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				keyEvent.consume();
			}
		});
		keyboardShortcut.processKeyEvent(KeyStroke.getKeyStrokeForEvent(keyEvent));
		assertTrue(keyEvent.isConsumed());
	}
	
	/**
	 * A KeyboardShortcut should ignore key events that do not match the shortcut
	 * it represents.
	 */
	@Test
	public void ignoreNonMatchingKeyEvent() {
		final KeyEvent keyEvent = new KeyEvent(new JPanel(), KeyEvent.KEY_PRESSED, 0, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
		keyboardShortcut.addAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				keyEvent.consume();
			}
		});
		keyboardShortcut.processKeyEvent(KeyStroke.getKeyStrokeForEvent(keyEvent));
		assertFalse(keyEvent.isConsumed());
	}
	
	/**
	 * If an invalid (read NULL) KeyStroke is passed to the constructor a NullPointerException
	 * should be thrown.
	 */
	@Test(expected=NullPointerException.class)
	public void invalidKeyStrokeThrowsNullPointerException() {
		// "control tab" is an invalid KeyStroke, a valid KeyStroke would be "control TAB"
		keyboardShortcut = new KeyboardShortcut(KeyStroke.getKeyStroke("control tab"));
	}
}

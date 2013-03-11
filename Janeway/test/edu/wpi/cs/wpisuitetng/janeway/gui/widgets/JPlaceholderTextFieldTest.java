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

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import org.junit.Before;
import org.junit.Test;

public class JPlaceholderTextFieldTest {
	
	private JPlaceholderTextField textField;

	@Before
	public void setUp() throws Exception {
		this.textField = new JPlaceholderTextField("hello world");
	}
	
	@Test
	public void functionNormallyWithoutPlaceholderText() {
		final JPlaceholderTextField textField = new JPlaceholderTextField();
		assertEquals(10, textField.getColumns());
		assertEquals("", textField.getText());
		textField.setText("hello world");
		assertEquals("hello world", textField.getText());
		textField.setText("");
		assertEquals("", textField.getText());
	}

	@Test
	public void providedPlaceholderTextIsDisplayedInitially() {
		assertEquals("hello world", textField.getText());
		assertEquals(Color.gray, textField.getForeground());
	}
	
	@Test
	public void placeholderTextRemovedWhenFieldGainsFocus() {
		focus();
		assertEquals("", textField.getText());
		assertEquals(Color.black, textField.getForeground());
	}
	
	@Test
	public void placeholderTextRestoredWhenFieldLosesFocusAndNoTextEntered() {
		focus();
		removeFocus();
		assertEquals("hello world", textField.getText());
	}
	
	@Test
	public void enteredTextIsKeptWhenFieldLosesFocus() {
		focus();
		textField.setText("new text");
		removeFocus();
		assertEquals("new text", textField.getText());
	}
	
	private void focus() {
		FocusListener[] listeners = textField.getFocusListeners();
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].focusGained(new FocusEvent(textField, FocusEvent.FOCUS_GAINED));
		}
	}
	
	private void removeFocus() {
		FocusListener[] listeners = textField.getFocusListeners();
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].focusLost(new FocusEvent(textField, FocusEvent.FOCUS_LOST));
		}
	}
}

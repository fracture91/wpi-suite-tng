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

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

/**
 * A text field that supports placeholder text. The placeholder text
 * is displayed in a gray font and it is removed when the text field
 * gains focus.
 */
@SuppressWarnings("serial")
public class JPlaceholderTextField extends JTextField {

	/** The placeholder text */
	protected final String placeholderText;

	/**
	 * Constructs the text field with the default number of
	 * columns equal to 10 and with no placeholder text.
	 */
	public JPlaceholderTextField() {
		this(10);
	}

	/**
	 * Constructs the text field with the specified number of
	 * columns and with no placeholder text
	 * @param columns the number of columns
	 */
	public JPlaceholderTextField(int columns) {
		this("", columns);
	}

	/**
	 * Constructs the text field with the given placeholder text
	 * @param text the placeholder text
	 */
	public JPlaceholderTextField(String text) {
		this(text, 10);
	}

	/**
	 * Constructs the text field with the given placeholder text
	 * and number of columns
	 * @param text the placeholder text
	 * @param columns the number of columns
	 */
	public JPlaceholderTextField(String text, int columns) {
		super(text, columns);
		this.placeholderText = text;
		this.setForeground(Color.gray);

		// Listen for gain focus and lose focus to remove/add
		// the placeholder text accordingly
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent event) {
				JTextField source = (JTextField) event.getSource();
				if (source.getText().equals(placeholderText)) {
					source.setText("");
					source.setForeground(Color.black);
				}
			}

			@Override
			public void focusLost(FocusEvent event) {
				JPlaceholderTextField source = (JPlaceholderTextField) event.getSource();
				if (source.getText().length() == 0) {
					source.clearText();
				}
			}
		});
	}

	/**
	 * Clears the text in the text field and restores the
	 * placeholder text.
	 */
	public void clearText() {
		setText(placeholderText);
		this.setForeground(Color.gray);
	}
	
	@Override
	public void setText(String newText) {
		super.setText(newText);
		this.setForeground(Color.black);
	}
}

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

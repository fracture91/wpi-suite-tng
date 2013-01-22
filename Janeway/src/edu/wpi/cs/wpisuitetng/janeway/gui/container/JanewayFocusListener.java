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

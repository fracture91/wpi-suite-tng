package edu.wpi.cs.wpisuitetng.janeway.gui.widgets;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;

/**
 * This class performs the given action when a keyboard event is received
 * that matches the keyboard shortcut this class represents.
 *
 */
public class KeyboardShortcut {
	
	/** 
	 * The modifier keys that must be pressed (e.g. CTRL, SHIFT). Build
	 * this mask by combining key masks with bitwise OR.
	 * 
	 * Example: to match the CTRL key and Shift key, use KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK
	 */
	protected final int modifiersDownMask;
	
	/** The modifier keys that must NOT be pressed */
	protected final int modifiersUpMask;
	
	/** The type of key event (e.g. KeyEvent.KEY_PRESSED, KeyEvent.KEY_TYPED, KeyEvent.KEY_RELEASED) */
	protected final int keyEventType;
	
	/** The alphanumeric key that must be matched (e.g. the mask KeyEvent.VK_S corresponds to the letter s) */
	protected final int keyPressed;
	
	/** The list of actions to perform when a KeyEvent is received that matches */
	protected final List<Action> actions;
	
	/**
	 * Constructs a new keyboard shortcut.
	 * @param modifiersDownMask the modifier keys that must be pressed
	 * @param modifiersUpMask the modifier keys that must not be pressed
	 * @param keyEventType the type of key event
	 * @param keyPressed the alphanumeric keys that must be pressed
	 */
	public KeyboardShortcut(int modifiersDownMask, int modifiersUpMask, int keyEventType, int keyPressed) {
		this(modifiersDownMask, modifiersUpMask, keyEventType, keyPressed, null);
	}
	
	/**
	 * Constructs a new keyboard shortcut.
	 * @param modifiersDownMask the modifier keys that must be pressed
	 * @param modifiersUpMask the modifier keys that must not be pressed
	 * @param keyEventType the type of key event
	 * @param keyPressed the alphanumeric keys that must be pressed
	 * @param action an action to be performed when a KeyEvent is received that matches
	 */
	public KeyboardShortcut(int modifiersDownMask, int modifiersUpMask, int keyEventType, int keyPressed, Action action) {
		this.modifiersDownMask = modifiersDownMask;
		this.modifiersUpMask = modifiersUpMask;
		this.keyEventType = keyEventType;
		this.keyPressed = keyPressed;
		actions = new ArrayList<Action>();
		if (action != null) {
			actions.add(action);
		}
	}
	
	/**
	 * Determines if the given key event matches this shortcut. If it matches
	 * each of the actions is performed.
	 * @param e the key event
	 */
	public void processKeyEvent(KeyEvent e) {
		if (((e.getModifiersEx() & (modifiersDownMask | modifiersUpMask)) == modifiersDownMask) &&
				(e.getID() == keyEventType) &&
				(e.getKeyCode() == keyPressed)) {
			performActions();
		}
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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.views;

import javax.swing.JPanel;

/**
 * Implementations of this interface provide buttons to display when they are relevant.
 * For example, defect views might provide buttons to save or discard changes.
 */
public interface IToolbarButtonProvider {
	/**
	 * @return a JPanel containing buttons to display.
	 */
	public JPanel getButtonPanel();
}

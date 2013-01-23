package edu.wpi.cs.wpisuitetng.janeway.gui.widgets;

import java.awt.Dimension;

/**
 * Implementors of this interface must be able to respond to mouse hover events
 */
public interface Hoverable {

	/**
	 * Called by the {@link edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.MouseHoverListener} when the mouse enters the Hoverable
	 */
	public void mouseEntered();
	
	/**
	 * Called by the {@link edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.MouseHoverListener} when the mouse exits the Hoverable
	 */
	public void mouseExited();
	
	/**
	 * Returns the dimensions of the Hoverable
	 * @return the dimensions of the Hoverable
	 */
	public Dimension getSize();
}

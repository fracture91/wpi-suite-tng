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

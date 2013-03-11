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

package edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.Hoverable;

/**
 * Mouse listener to detect when the mouse is hovering over a Hoverable
 */
public class MouseHoverListener implements MouseListener {
	
	/**
	 * The Hoverable being monitored for hover events
	 */
	protected Hoverable hoverable;
	
	/**
	 * The number of pixels the mouse event must be from the component border to be considered an exit/entrance
	 */
	protected static final int BUFFER = 5;
	
	/**
	 * Constructs a new listener for the given component
	 * @param component the component to monitor for hover events
	 */
	public MouseHoverListener(Hoverable hoverable) {
		this.hoverable = hoverable;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		hoverable.mouseEntered();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Check to make sure mouse is exiting through the outer bounds of the component
		if (e.getPoint().getX() < BUFFER || e.getPoint().getX() > (hoverable.getSize().getWidth() - BUFFER) || e.getPoint().getY() < BUFFER || e.getPoint().getY() > (hoverable.getSize().getHeight() - BUFFER)) { 				
			hoverable.mouseExited();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// do nothing
	}
}

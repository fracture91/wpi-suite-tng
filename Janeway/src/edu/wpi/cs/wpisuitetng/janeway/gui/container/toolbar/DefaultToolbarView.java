/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JToolBar;


/**
 * Provides a default view for a toolbar and methods for adding/removing ToolbarGroupViews.
 * The toolbar cannot be dragged, and groups line up along the X axis. Excess space goes to the right.
 * Methods will take care of separators such that there is exactly one between each
 * ToolbarGroupView within the toolbar, assuming no outside forces muck with the toolbar contents.
 */
@SuppressWarnings("serial")
public class DefaultToolbarView extends JToolBar {
	
	private Component glue;
	
	public DefaultToolbarView() {
		setFloatable(false);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		glue = Box.createHorizontalGlue();
		add(glue);
	}
	
	/**
	 * Insert a ToolbarGroupView into the toolbar at the specified index.
	 * If the group is already at the specified index, nothing happens.
	 * If the group is already in the toolbar but at the wrong index, a RuntimeException is thrown
	 * since the index value becomes meaningless after the existing group is removed.
	 * @param group The group to insert
	 * @param index The index to insert the group at, which counts separators.
	 */
	public void insertGroupAt(ToolbarGroupView group, int index) {
		if(containsGroup(group)) {
			if(getComponentAtIndex(index) == group) {
				return;  // do nothing, this is already in the desired position
			} else {
				throw new RuntimeException("Group already exists at different index, remove it first");
			}
		}
		// TODO: make separators draw as visible lines
		if(index > 0 && getComponentAtIndex(index - 1) instanceof ToolbarGroupView) {
			add(new Separator(), index);
			index++;
		}
		if(index < getComponentIndex(glue) && getComponentAtIndex(index) instanceof ToolbarGroupView) {
			add(new Separator(), index);
		}
		add(group, index);
		repaint();
	}
	
	/**
	 * Add a ToolbarGroupView to the end of the toolbar if it isn't already on the toolbar.
	 * @param group The group to add
	 */
	public void addGroup(ToolbarGroupView group) {
		if(!containsGroup(group)) {
			insertGroupAt(group, getComponentIndex(glue));
		}
	}
	
	/**
	 * Remove a ToolbarGroupView from the toolbar, if it exists.
	 * @param group The group to remove
	 */
	public void removeGroup(ToolbarGroupView group) {
		if(!containsGroup(group)) {
			return;
		}
		int groupIndex = getComponentIndex(group);
		Component nearComponent = getComponentAtIndex(groupIndex+1);
		if(nearComponent instanceof Separator) {
			remove(nearComponent);
		} else if(nearComponent == glue && groupIndex > 0) {
			nearComponent = getComponentAtIndex(groupIndex-1);
			if(nearComponent instanceof Separator) {
				remove(nearComponent);
			}
		}
		remove(group);
		repaint();
	}
	
	/**
	 * @param group A group to check
	 * @return true if the given group is in this toolbar, false otherwise 
	 */
	public boolean containsGroup(ToolbarGroupView group) {
		return group.getParent() == this;
	}
	
}

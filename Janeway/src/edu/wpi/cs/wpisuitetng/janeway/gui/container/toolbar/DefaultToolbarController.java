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


/**
 * A barebones controller for a DefaultToolbarView.
 * Makes the view display those groups deemed relevant.
 * Intended to listen to other objects (e.g. a tabbed pane) and determine what is relevant based on what is
 * happening to those other objects.  For example, this could listen to a tabbed pane to determine which
 * tab is currently focused, and change the set of relevant groups based on the visible tab.
 * 
 * A more complex controller might override setRelevant and ensure/change the order of relevant groups,
 * only add them based on certain criteria, etc.
 */
public class DefaultToolbarController {

	private DefaultToolbarView toolbarView;
	
	/**
	 * Construct a controller for a DefaultToolbarView.
	 * @param toolbarView The toolbar to control
	 */
	public DefaultToolbarController(DefaultToolbarView toolbarView) {
		this.toolbarView = toolbarView;
	}
	
	/**
	 * Set if the given group is relevant.  Relevant groups will be added to the toolbar if they aren't
	 * already there, irrelevant groups are removed.
	 * @param group The group to set the relevance of
	 * @param isRelevant true if relevant, false otherwise
	 */
	public void setRelevant(ToolbarGroupView group, boolean isRelevant) {
		if(!isRelevant) {
			toolbarView.removeGroup(group);
		} else {
			toolbarView.addGroup(group);
		}
	}
	
}

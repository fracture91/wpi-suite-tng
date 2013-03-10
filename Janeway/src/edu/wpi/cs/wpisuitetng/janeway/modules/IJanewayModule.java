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
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.janeway.modules;

import java.util.List;

/**
 * A module being used in Janeway must implement this interface.
 * Modules have a name and a list of tabs associated with them.
 */
public interface IJanewayModule {
	
	/**
	 * @return The name of the module (e.g. "Defect Tracker").
	 */
	public String getName();
	
	/**
	 * @return The list of tab models associated with this module.
	 */
	public List<JanewayTabModel> getTabs();
	
}

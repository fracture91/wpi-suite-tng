/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    twack
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng;

import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;

/**
 * Handles the formatting strategy of the content body String. Implementations
 * 	of this interface determine the content body's format.
 * @author twack
 *
 */
public interface ErrorResponseFormatter {
	
	/**
	 * the algorithm used to format the Exception data into a
	 * 	content body String.
	 * @param e
	 * @return	the content body
	 */
	String formatContent(WPISuiteException e);
}

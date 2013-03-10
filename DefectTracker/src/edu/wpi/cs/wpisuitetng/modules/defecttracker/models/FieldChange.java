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
 *    Mike Della Donna
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.models;

/**
 * Model that holds and old and new value for some field.
 * Doesn't implement Model since it will see no use outside of DefectChangesets.
 *
 * @param <T> the type of the field that was changed
 */
public class FieldChange<T> {
	private final T oldValue;
	private final T newValue;
	
	/**
	 * @param oldValue the old value of a field before it was changed
	 * @param newValue the new value of a field after it was changed
	 */
	public FieldChange(T oldValue, T newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * @return the oldValue
	 */
	public T getOldValue() {
		return oldValue;
	}

	/**
	 * @return the newValue
	 */
	public T getNewValue() {
		return newValue;
	}
	
}

/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    JPage
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.border.Border;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectStatus;

/**
 * Checks for whether or not the text in a given JComboBox differs from the current model (a Defect).
 * 
 * Whenever a key is released in the ItemListener's component, checkIfUpdated() is called. This method 
 * gets the component's name and looks up the value of the relevant field in panel's Defect model. It then 
 * compares this value to the component's selection to see if the text differs from the model. If the selection 
 * differs, the style of the component is changed to show that it differs from the relevant field in the model.
 * Otherwise, the component's style is changed to be normal.
 */
public class ComboUpdateListener implements ItemListener {
	protected final DefectPanel panel;
	protected final JComboBox component;
	protected final Border defaultBorder;
	
	/**
	 * Constructs a ComboUpdateListener.
	 * 
	 * @param panel			The DefectPanel which contains the JTextComponent.
	 * @param component		The JComboBox which will have its selection compared to the model. The name 
	 * 						of the JComboBox must match the name of a getter in Defect after the 
	 * 						"get". For instance: for the method "getStatus", the name of the 
	 * 						JComboBox must be "Status".
	 */
	public ComboUpdateListener(DefectPanel panel, JComboBox component) {
		this.panel = panel;
		this.component = component;
		this.defaultBorder = component.getBorder();
	}
	
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		checkIfUpdated();
	}

	/**
	 * Checks if the field differs from the DefectPanel's model and changes the style of the field accordingly.
	 */
	public void checkIfUpdated() {
		String base = ""; // the String value of the field in the panel's Defect model that corresponds to the component

		// Get the base String to compare to the text of the JTextComponent
		try {
			// Get the field from the Defect model that corresponds with the name of component.
			// For instance, if the component's name is "Title" Defect#getStatus() will be called.
			Object field = panel.getModel().getClass().getDeclaredMethod("get" + component.getName()).invoke(panel.getModel());
			
			// If field is null, set base to an empty String.
			if (field == null) {
				base = "";
			}
			// If field is an instance of String, set base to that String.
			else if (field instanceof String) {
				base = (String) field;
			}
			// If field is an instance of User, set base to its username.
			else if (field instanceof DefectStatus) {
				base = ((DefectStatus) field).toString();
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Compare base to the component's text to determine whether or not to highlight the field.
		if (base.equals((String) component.getSelectedItem())) {
			component.setBackground(Color.WHITE);
			component.setBorder(defaultBorder);
		}
		else {
			component.setBackground(new Color(243, 243, 209));
			component.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}
	}
}

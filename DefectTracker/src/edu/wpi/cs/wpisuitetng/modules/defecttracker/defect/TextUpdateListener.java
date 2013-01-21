package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Checks for whether or not the text in a given JTextComponent differs from the current model (a Defect).
 */
public class TextUpdateListener implements KeyListener {
	protected final DefectPanel panel;
	protected final JTextComponent component;
	protected final Border defaultBorder;

	/**
	 * Constructs a TextUpdateListener.
	 * 
	 * @param panel			The DefectPanel which contains the JTextComponent.
	 * @param component		The JTextComponent which will have its text compared to the model. The name 
	 * 						of the JTextComponent must match the name of a getter in Defect after the 
	 * 						"get". For instance: for the method "getTitle", the name of the 
	 * 						JTextComponent must be "Title".
	 */
	public TextUpdateListener(DefectPanel panel, JTextComponent component) {
		this.panel = panel;
		this.component = component;
		this.defaultBorder = component.getBorder();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		checkIfUpdated();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * Checks if the field differs from the DefectPanel's model and changes the style of the field accordingly.
	 */
	public void checkIfUpdated() {
		String base = ""; // the String value of the field in the panel's Defect model that corresponds to the component

		// Get the base String to compare to the text of the JTextComponent
		try {
			// Get the field from the Defect model that corresponds with the name of component.
			// For instance, if the component's name is "Title" Defect#getTitle will be called.
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
			else if (field instanceof User) {
				base = ((User) field).getUsername();
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
		if (base.equals(component.getText())) {
			component.setBackground(Color.WHITE);
			component.setBorder(defaultBorder);
		}
		else {
			component.setBackground(new Color(243, 243, 209));
			component.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}
	}
}
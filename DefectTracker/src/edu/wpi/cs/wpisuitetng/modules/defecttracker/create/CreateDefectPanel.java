package edu.wpi.cs.wpisuitetng.modules.defecttracker.create;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;

/**
 * A panel containing GUI elements for creating a new Defect
 *
 */
@SuppressWarnings("serial")
public class CreateDefectPanel extends JPanel {
	
	/** Label for the description text field */
	protected JLabel lblDescription;
	
	/** Label for the title text field */
	protected JLabel lblTitle;
	
	/** Label for the user text field */
	protected JLabel lblUser;
	
	/** Text field for the user to enter the title */
	protected JTextField txtTitle;
	
	/** Text field for the user to enter the user name */
	protected JTextField txtUser;
	
	/** Text area for the user to enter the description */
	protected JTextArea txtDescription;
	
	/** The spring layout used to layout this panel */
	protected SpringLayout springLayout;
	
	/**
	 * Creates a new CreateDefectPanel and adds all of the elements
	 * to the panel. Also places the layout constraints.
	 */
	public CreateDefectPanel() {
		// Set the layout of the panel
		springLayout = new SpringLayout();
		this.setLayout(springLayout);
		
		// Add the title field
		lblTitle = new JLabel("Title");		
		txtTitle = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, lblTitle, 75, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, lblTitle, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, txtTitle, 5, SpringLayout.EAST, lblTitle);
		springLayout.putConstraint(SpringLayout.NORTH, txtTitle, 5, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, txtTitle, -300, SpringLayout.EAST, this);
		this.add(lblTitle);
		this.add(txtTitle);
		
		// Add the user field
		lblUser = new JLabel("User");
		txtUser = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, lblUser, 75, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, lblUser, 27, SpringLayout.SOUTH, lblTitle);
		springLayout.putConstraint(SpringLayout.WEST, txtUser, 5, SpringLayout.EAST, lblUser);
		springLayout.putConstraint(SpringLayout.NORTH, txtUser, 15, SpringLayout.SOUTH, txtTitle);
		springLayout.putConstraint(SpringLayout.EAST, txtUser, -525, SpringLayout.EAST, this);
		this.add(lblUser);
		this.add(txtUser);
		
		// Add the description field
		lblDescription = new JLabel("Description");
		txtDescription = new JTextArea();
		txtDescription.setLineWrap(true);
		springLayout.putConstraint(SpringLayout.WEST, lblDescription, 75, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, lblDescription, 27, SpringLayout.SOUTH, lblUser);
		springLayout.putConstraint(SpringLayout.WEST, txtDescription, 5, SpringLayout.EAST, lblDescription);
		springLayout.putConstraint(SpringLayout.NORTH, txtDescription, 15, SpringLayout.SOUTH, txtUser);
		springLayout.putConstraint(SpringLayout.EAST, txtDescription, -300, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, txtDescription, -275, SpringLayout.SOUTH, this);
		this.add(lblDescription);
		this.add(txtDescription);
		
		this.validate();
	}
	
	/**
	 * Returns the model object represented by this view
	 * TODO: Change return type to the abstract class / interface
	 * TODO: Ensure that if id field is set to -1, that a new defect is created on the server
	 * TODO: Do some basic input verification
	 * @return the model represented by this view
	 */
	public Defect getModel() {
		return new Defect(-1, txtTitle.getText(), txtDescription.getText(), new User("", txtUser.getText(), -1));
	}
}

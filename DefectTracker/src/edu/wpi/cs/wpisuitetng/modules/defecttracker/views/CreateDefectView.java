package edu.wpi.cs.wpisuitetng.modules.defecttracker.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.controllers.SaveDefectController;

/**
 * This view is responsible for showing the form for creating a new defect. 
 */
@SuppressWarnings("serial")
public class CreateDefectView extends JPanel implements IToolbarButtonProvider {

	private JPanel buttonPanel;
	private JButton saveButton;
	private JPanel mainPanel;
	
	/**
	 * Constructs a new CreateDefectView where the user can
	 * enter the data for a new defect.
	 */
	public CreateDefectView() {
		
		// Instantiate the button panel
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		
		// Instantiate the save button and add it to the button panel
		saveButton = new JButton("Save Changes");
		buttonPanel.add(saveButton);
		
		// Instantiate the main create defect panel
		mainPanel = new CreateDefectPanel();
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		
		// Add action listener to save button
		saveButton.addActionListener(new SaveDefectController((CreateDefectPanel)mainPanel));
	}

	/**
	 * Returns the main panel with the data fields
	 * @return the main panel with the data fields
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	@Override
	public JPanel getButtonPanel() {
		return buttonPanel;
	}
}

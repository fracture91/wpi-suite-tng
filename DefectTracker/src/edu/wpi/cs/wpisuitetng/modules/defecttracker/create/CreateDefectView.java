package edu.wpi.cs.wpisuitetng.modules.defecttracker.create;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;

/**
 * This view is responsible for showing the form for creating a new defect. 
 */
@SuppressWarnings("serial")
public class CreateDefectView extends JPanel implements IToolbarGroupProvider {

	private ToolbarGroupView buttonGroup;
	private JButton saveButton;
	private CreateDefectPanel mainPanel;
	private SaveDefectController controller;
	
	/**
	 * Constructs a new CreateDefectView where the user can
	 * enter the data for a new defect.
	 */
	public CreateDefectView() {
		// Instantiate the main create defect panel
		mainPanel = new CreateDefectPanel();
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		controller = new SaveDefectController(mainPanel);
		
		// Instantiate the button panel
		buttonGroup = new ToolbarGroupView("Create Defect");
		
		// Instantiate the save button and add it to the button panel
		saveButton = new JButton();
		saveButton.setAction(new SaveChangesAction(controller));
		buttonGroup.getContent().add(saveButton);
		buttonGroup.setPreferredWidth(150);
	}

	/**
	 * Returns the main panel with the data fields
	 * @return the main panel with the data fields
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	@Override
	public ToolbarGroupView getGroup() {
		return buttonGroup;
	}
}

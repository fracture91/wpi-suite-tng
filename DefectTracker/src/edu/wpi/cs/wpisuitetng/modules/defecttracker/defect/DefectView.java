package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;

/**
 * This view is responsible for showing the form for creating or viewing a new defect.
 */
@SuppressWarnings("serial")
public class DefectView extends JPanel implements IToolbarGroupProvider {

	private ToolbarGroupView buttonGroup;
	private JButton saveButton;
	private DefectPanel mainPanel;
	private SaveDefectController controller;

	/**
	 * Constructs a new CreateDefectView where the user can enter the data for a new defect.
	 */
	public DefectView() {
		// Instantiate the main create defect panel
		mainPanel = new DefectPanel();
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.PAGE_START);
		controller = new SaveDefectController(mainPanel);

		// Instantiate the button panel
		buttonGroup = new ToolbarGroupView("View/Edit Defect");

		// Instantiate the save button and add it to the button panel
		saveButton = new JButton();
		saveButton.setAction(new SaveChangesAction(controller));
		buttonGroup.getContent().add(saveButton);
		buttonGroup.setPreferredWidth(150);
		
		// Prefill the defect panel
		mainPanel.getCreatorField().setText(ConfigManager.getConfig().getUserName());
	}

	/**
	 * Constructs a new CreateDefectView where the user can view (and edit) a defect.
	 * 
	 * @param defect	The defect to show.
	 */
	public DefectView(Defect defect) {
		// Instantiate the main create defect panel
		mainPanel = new DefectPanel(defect);
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.PAGE_START);
		controller = new SaveDefectController(mainPanel);

		// Instantiate the button panel
		buttonGroup = new ToolbarGroupView("View/Edit Defect");

		// Instantiate the save button and add it to the button panel
		saveButton = new JButton();
		saveButton.setAction(new SaveChangesAction(controller));
		buttonGroup.getContent().add(saveButton);
		buttonGroup.setPreferredWidth(150);
	}

	/**
	 * Returns the main panel with the data fields
	 * 
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

package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.DummyTab;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.Tab;

/**
 * This view is responsible for showing the form for creating or viewing a new defect.
 */
@SuppressWarnings("serial")
public class DefectView extends JPanel implements IToolbarGroupProvider {

	private ToolbarGroupView buttonGroup;
	private JButton saveButton;
	private DefectPanel mainPanel;
	private SaveDefectController controller;
	private Tab containingTab;

	/**
	 * Constructs a new CreateDefectView where the user can enter the data for a new defect.
	 */
	public DefectView() {
		this(new Defect(), Mode.CREATE, null);
	}

	/**
	 * Constructs a new DefectView where the user can view (and edit) a defect.
	 * 
	 * @param defect	The defect to show.
	 * @param editMode	The editMode for editing the Defect
	 * @param tab		The Tab holding this DefectView (can be null)
	 */
	public DefectView(Defect defect, Mode editMode, Tab tab) {
		containingTab = tab;
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		
		// Instantiate the button panel
		buttonGroup = new ToolbarGroupView("Create Defect");
		
		containingTab.setIcon(new ImageIcon());
		if(editMode == Mode.CREATE) {
			containingTab.setTitle("Create Defect");
			containingTab.setToolTipText("Create a new defect");
		} else {
			setEditModeDescriptors(defect);
		}
		
		// If this is a new defect, set the creator
		if (editMode == Mode.CREATE) {
			defect.setCreator(new User("", ConfigManager.getConfig().getUserName(), "", -1));
		}
		
		// Instantiate the main create defect panel
		mainPanel = new DefectPanel(defect, editMode);
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.PAGE_START);
		controller = new SaveDefectController(this);

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
	public JPanel getDefectPanel() {
		return mainPanel;
	}
	
	@Override
	public ToolbarGroupView getGroup() {
		return buttonGroup;
	}
	
	/**
	 * @param defect Set the tab title, tooltip, and group name according to this Defect
	 */
	protected void setEditModeDescriptors(Defect defect) {
		containingTab.setTitle("Defect #" + defect.getId());
		containingTab.setToolTipText("View defect " + defect.getTitle());
		buttonGroup.setName("Edit Defect");
	}
}

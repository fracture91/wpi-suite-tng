package edu.wpi.cs.wpisuitetng.modules.defecttracker.views;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This view is responsible for showing the form for creating a new defect. 
 */
@SuppressWarnings("serial")
public class CreateDefectView extends JPanel implements IRibbonButtonProvider {

	private JPanel buttonPanel;
	
	public CreateDefectView() {
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		JButton saveChanges = new JButton("Save Changes");
		buttonPanel.add(saveChanges);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public JPanel getButtonPanel() {
		return buttonPanel;
	}

}

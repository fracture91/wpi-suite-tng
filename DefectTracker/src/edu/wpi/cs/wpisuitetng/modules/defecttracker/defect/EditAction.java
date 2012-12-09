package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class EditAction extends AbstractAction {

	private final JButton saveButton;
	private final JButton editButton;
	private final DefectPanel mainPanel;
	
	/**
	 * Create an EditAction
	 */
	public EditAction(JButton saveButton, JButton editButton, DefectPanel mainPanel) {
		super("Edit Defect");
		this.saveButton = saveButton;
		this.editButton = editButton;
		this.mainPanel = mainPanel;
		putValue(MNEMONIC_KEY, KeyEvent.VK_E);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		editButton.setEnabled(false);
		editButton.setVisible(false);
		
		saveButton.setVisible(true);
		saveButton.setEnabled(true);
		
		mainPanel.setInputEnabled(true);
	}

}

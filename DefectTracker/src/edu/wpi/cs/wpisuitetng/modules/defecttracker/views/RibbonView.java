package edu.wpi.cs.wpisuitetng.modules.defecttracker.views;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The Defect tab's Ribbon panel.
 */
@SuppressWarnings("serial")
public class RibbonView extends JPanel {

	public RibbonView() {
		this.setLayout(new FlowLayout());
		JLabel testLabel = new JLabel("This is the ribbon panel.");
		this.add(testLabel);
	}

}

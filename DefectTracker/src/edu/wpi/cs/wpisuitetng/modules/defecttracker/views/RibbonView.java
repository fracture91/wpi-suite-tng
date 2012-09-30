package edu.wpi.cs.wpisuitetng.modules.defecttracker.views;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The Defect tab's Ribbon panel.
 */
@SuppressWarnings("serial")
public class RibbonView extends JPanel {

	public RibbonView() {
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		JButton newDefect = new JButton("New Defect");
		JButton searchDefects = new JButton("Search Defects");
		this.add(newDefect);
		this.add(searchDefects);
	}

}

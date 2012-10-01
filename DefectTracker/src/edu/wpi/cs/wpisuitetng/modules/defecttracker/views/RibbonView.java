package edu.wpi.cs.wpisuitetng.modules.defecttracker.views;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.JanewayModule;

/**
 * The Defect tab's Ribbon panel.
 */
@SuppressWarnings("serial")
public class RibbonView extends JPanel implements ActionListener {

	private JButton createDefect;
	private JButton searchDefects;
	private JanewayModule module;
	
	public RibbonView(JanewayModule module) {
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		this.module = module;
		createDefect = new JButton("Create Defect");
		createDefect.addActionListener(this);
		searchDefects = new JButton("Search Defects");
		searchDefects.addActionListener(this);
		this.add(createDefect);
		this.add(searchDefects);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createDefect) {
			module.mainTabController.addCreateDefectTab();
		}
	}

}

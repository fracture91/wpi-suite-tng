package edu.wpi.cs.wpisuitetng.modules.defecttracker.views;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.JanewayModule;

/**
 * The Defect tab's toolbar panel.
 */
@SuppressWarnings("serial")
public class ToolbarView extends JPanel implements ActionListener, ChangeListener {

	private JButton createDefect;
	private JButton searchDefects;
	private JPanel providedButtons;
	private JanewayModule module;
	
	public ToolbarView(JanewayModule module) {
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		this.module = module;
		createDefect = new JButton("Create Defect");
		createDefect.addActionListener(this);
		searchDefects = new JButton("Search Defects");
		searchDefects.addActionListener(this);
		this.add(createDefect);
		this.add(searchDefects);
		module.mainTabController.addChangeListener(this);
	}

	private void setProvidedButtons(JPanel buttons) {
		if(providedButtons != null) {
			this.remove(providedButtons);
		}
		providedButtons = buttons;
		if(providedButtons != null) {
			this.add(providedButtons);
		}
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createDefect) {
			module.mainTabController.addCreateDefectTab();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO: there has to be a cleaner way to do this
		if(e.getSource() instanceof MainTabView) {
			MainTabView view = (MainTabView) e.getSource();
			Component selectedComponent = view.getSelectedComponent();
			if(selectedComponent instanceof IToolbarButtonProvider) {
				IToolbarButtonProvider provider = (IToolbarButtonProvider) selectedComponent;
				this.setProvidedButtons(provider.getButtonPanel());
			} else {
				this.setProvidedButtons(null);
			}
		}
	}

}

package edu.wpi.cs.wpisuitetng.modules.postboard;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.postboard.view.MainView;

/**
 * This is a module for WPISuiteTNG that provides a post board. A post
 * board is simply a place where messages can be posted that are viewable
 * by all users. Think of it as a cork board on your wall, but only for text.
 * 
 * @author Chris Casola
 *
 */
public class PostBoard implements IJanewayModule {
	
	/**
	 * A list of tabs owned by this module
	 */
	List<JanewayTabModel> tabs;
	
	/**
	 * Constructs the main views for this module. Namely one tab, containing
	 * a toolbar and a main content panel.
	 */
	public PostBoard() {

		// Initialize the list of tabs (however, this module has only one tab)
		tabs = new ArrayList<JanewayTabModel>();
		
		// Create a JPanel to hold the toolbar for the tab
		JPanel toolbarPanel = new JPanel();
		toolbarPanel.add(new JLabel("PostBoard toolbar placeholder")); // add a label with some placeholder text
		toolbarPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 2)); // add a border so you can see the panel
		
		// Constructs and adds the MainPanel
		MainView mainPanel = new MainView();
		
		// Create a tab model that contains the toolbar panel and the main content panel
		JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), toolbarPanel, mainPanel);
		
		// Add the tab to the list of tabs owned by this module
		tabs.add(tab1);
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "PostBoard";
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}

}

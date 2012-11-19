package edu.wpi.cs.wpisuitetng.janeway.gui.container;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigMgr;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;

/**
 * The main application window for the Janeway client
 *
 */
@SuppressWarnings("serial")
public class JanewayFrame extends JFrame {
	
	/** A panel to contain the tabs */
	protected TabPanel tabPanel;
	
	/**
	 * Construct a new JanewayFrame
	 */
	public JanewayFrame(List<IJanewayModule> modules) {
		// Set window properties
		setTitle("Janeway - WPI Suite Desktop Client");
		setMinimumSize(new Dimension(800, 600)); // minimum window size is 800 x 600
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		// Clean up when the window is closed
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				// write the configuration to janeway.conf
				ConfigMgr.writeConfig();
				
				// dispose of this window
				dispose();
			}
		});
		
		// Set the window size and position based on screen size
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)(dim.width * 0.85);
		int height = (int)(dim.height * 0.85);
		int xPos = (dim.width - width) / 2;
		int yPos = (int)((dim.height - height) / 2 * .75);
		setBounds(xPos, yPos, width, height);
		
		// Setup the layout manager
		this.setLayout(new BorderLayout());
		
		// Add the tab panel
		tabPanel = new TabPanel(modules);
		this.add(tabPanel, BorderLayout.CENTER);		
	}
}

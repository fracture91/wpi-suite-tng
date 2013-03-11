/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 *    JPage
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.janeway.gui.login;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * This is the login window for Janeway.
 *
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame {
	
	protected JPanel contentPane;
	protected LoginHeaderPanel loginHeaderPanel;
	protected LoginFieldPanel loginFieldPanel;
	protected LoginButtonPanel loginButtonPanel;
	protected static final int WINDOW_WIDTH = 500;
	protected static final int WINDOW_HEIGHT = 400;
	
	/**
	 * Constructs a login JFrame with all of the relevant controls and packs them.
	 * 
	 * @param applicationName	The name of the application. This will be used to construct the window title.
	 */
	public LoginFrame(String applicationName) {
		super("Login - " + applicationName);
		
		// Call System.exit() when the close button is clicked.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create content pane and set layout to SpringLayout
		contentPane = new JPanel();
		setContentPane(contentPane);
		SpringLayout layout = new SpringLayout();
		contentPane.setLayout(layout);
		
		// Add LoginHeaderPanel
		loginHeaderPanel = new LoginHeaderPanel();
		layout.putConstraint(SpringLayout.NORTH, loginHeaderPanel, 15, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, loginHeaderPanel, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, loginHeaderPanel, -5, SpringLayout.EAST, contentPane);
		contentPane.add(loginHeaderPanel);
	
		// Add LoginFieldPanel
		loginFieldPanel = new LoginFieldPanel();
		layout.putConstraint(SpringLayout.NORTH, loginFieldPanel, 40, SpringLayout.SOUTH, loginHeaderPanel);
		layout.putConstraint(SpringLayout.WEST, loginFieldPanel, 45, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, loginFieldPanel, -45, SpringLayout.EAST, contentPane);
		contentPane.add(loginFieldPanel);
		
		// Add LoginButtonPanel
		loginButtonPanel = new LoginButtonPanel();
		layout.putConstraint(SpringLayout.NORTH, loginButtonPanel, 40, SpringLayout.SOUTH, loginFieldPanel);
		layout.putConstraint(SpringLayout.WEST, loginButtonPanel, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, loginButtonPanel, -5, SpringLayout.EAST, contentPane);
		contentPane.add(loginButtonPanel);
		
		// Add key listener for Enter to the login fields
		this.getRootPane().setDefaultButton(getConnectButton());
		
		// Pack the frame
		pack();
		
		// Set the window size and position
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int xPos = (dim.width - WINDOW_WIDTH) / 2;
		int yPos = (int)((dim.height - WINDOW_HEIGHT) / 2 * .75);
		setBounds(xPos, yPos, WINDOW_WIDTH, WINDOW_HEIGHT);
	}
	
	/**
	 * Getter for the url text field
	 * @return the url text field
	 */
	public JTextField getUrlTextField() {
		return this.loginFieldPanel.txtUrl;
	}
	
	/**
	 * Getter for the project field
	 * @return the project text field
	 */
	public JTextField getProjectField() {
		return this.loginFieldPanel.txtProject;
	}
	
	/**
	 * Getter for the user name text field
	 * @return the user name text field
	 */
	public JTextField getUserNameField() {
		return this.loginFieldPanel.txtUsername;
	}
	
	/**
	 * Getter for the password text field
	 * @return the password text field
	 */
	public JPasswordField getPasswordField() {
		return this.loginFieldPanel.txtPassword;
	}
	
	/**
	 * Getter for the connect button
	 * @return the connect button
	 */
	public JButton getConnectButton() {
		return this.loginButtonPanel.btnConnect;
	}
}

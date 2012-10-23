package edu.wpi.cs.wpisuitetng.janeway.startup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * This is the login window for Janeway.
 * 
 * TODO handle button clicks
 */
@SuppressWarnings("serial")
public class LoginView extends JFrame {
	private JTextField usernameText;
	private JPasswordField passwordText;
	private JTextField	projectText;
	private JTextField urlText;
	private JButton connectButton;
	
	/**
	 * Constructor for LoginView. Populates the JFrame with all of the relevant controls and packs them.
	 * 
	 * @param applicationName	The name of the application. This will be used to construct the window title.
	 */
	public LoginView(String applicationName) {
		super("Login - " + applicationName);
		
		// Create and populate usernamePanel
		JPanel usernamePanel = new JPanel();
		usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
		JLabel usernameLabel = new JLabel("Username", JLabel.TRAILING);
		usernameText = new JTextField(20);
		usernameLabel.setLabelFor(usernameText);
		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameText);
		
		// Create and populate passwordPanel
		JPanel passwordPanel = new JPanel();
		passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
		passwordPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
		JLabel passwordLabel = new JLabel("Password", JLabel.TRAILING);
		passwordText = new JPasswordField(20);
		passwordLabel.setLabelFor(passwordText);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordText);
		
		// Create and populate projectPanel
		JPanel projectPanel = new JPanel();
		projectPanel.setLayout(new BoxLayout(projectPanel, BoxLayout.X_AXIS));
		projectPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
		JLabel projectLabel = new JLabel("Project", JLabel.TRAILING);
		urlText = new JTextField(40);
		projectPanel.add(projectLabel);
		projectText = new JTextField(20);
		projectLabel.setLabelFor(projectText);
		projectPanel.add(projectText);
		
		// Create and populate infoPanel
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		infoPanel.add(usernamePanel);
		infoPanel.add(passwordPanel);
		infoPanel.add(projectPanel);
		
		// Create and populate connectionPanel
		JPanel connectionPanel = new JPanel();
		connectionPanel.setLayout(new BoxLayout(connectionPanel, BoxLayout.X_AXIS));
		connectionPanel.setBorder(new EmptyBorder(10, 5, 5, 5));
		JLabel urlLabel = new JLabel("URL", JLabel.TRAILING);
		urlLabel.setLabelFor(urlText);
		connectionPanel.add(urlLabel);
		connectionPanel.add(urlText);
		
		// Create and populate buttonPanel
		JPanel buttonPanel = new JPanel();
		connectButton = new JButton("Connect");
		connectButton.setPreferredSize(new Dimension(150, 25));
		buttonPanel.add(connectButton);
		
		// Populate this JFrame
		this.add(infoPanel, BorderLayout.PAGE_START);
		this.add(connectionPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.PAGE_END);//*/
		
		// Pack this
		this.pack();
	}
	
	/**
	 * Getter for the url text field
	 * @return the url text field
	 */
	public JTextField getUrlTextField() {
		return urlText;
	}
	
	/**
	 * Getter for the connect button
	 * @return
	 */
	public JButton getConnectButton() {
		return connectButton;
	}
}

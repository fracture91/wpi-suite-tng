package edu.wpi.cs.wpisuitetng.janeway.gui.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.wpi.cs.wpisuitetng.janeway.Configuration;


/**
 * Controller to handle user login
 *
 */
public class LoginController implements ActionListener {
	
	/** The view containing the login form */
	protected LoginView view;
	
	/** The main application GUI to load after login */
	protected JFrame mainGUI;
	
	/**
	 * Construct a new login controller
	 * @param mainGUI the main application GUI to load after login
	 * @param view the view containing the login form
	 */
	public LoginController(JFrame mainGUI, LoginView view) {
		this.view = view;
		this.mainGUI = mainGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// For now, just set local core URL variable
		if (view.getUrlTextField().getText().length() > 0) {
			Configuration.setCoreURL(view.getUrlTextField().getText());
			mainGUI.setVisible(true);
			view.dispose();
		}
		else {
			JOptionPane.showMessageDialog(view, "You must specify the server address!", "Login Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
}

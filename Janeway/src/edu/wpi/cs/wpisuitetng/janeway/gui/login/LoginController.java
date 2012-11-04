package edu.wpi.cs.wpisuitetng.janeway.gui.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.wpi.cs.wpisuitetng.janeway.Configuration;


/**
 * Controller to handle user login
 *
 */
public class LoginController implements ActionListener {
	
	/** The view containing the login form */
	protected LoginFrame view;
	
	/** The main application GUI to load after login */
	protected JFrame mainGUI;
	
	/** The title of error dialogs */
	private static final String errorTitle = "Login Error";
	
	/**
	 * Construct a new login controller
	 * @param mainGUI the main application GUI to load after login
	 * @param view the view containing the login form
	 */
	public LoginController(JFrame mainGUI, LoginFrame view) {
		this.view = view;
		this.mainGUI = mainGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (view.getUrlTextField().getText().length() > 0) {
			final String URLText = view.getUrlTextField().getText();
			final URL coreURL;
			try {
				coreURL = new URL(URLText);
				Configuration.getInstance().setCoreURL(coreURL);
				mainGUI.setVisible(true);
				view.dispose();
			} catch (MalformedURLException e1) {
				JOptionPane.showMessageDialog(view,
				                              "The server address \"" + URLText + "\" is not a valid URL!",
				                              errorTitle, JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			JOptionPane.showMessageDialog(view, "You must specify the server address!", errorTitle,
			                              JOptionPane.ERROR_MESSAGE);
		}
		
	}
}

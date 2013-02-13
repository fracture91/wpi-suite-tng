package edu.wpi.cs.wpisuitetng.janeway.gui.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Base64;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

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
		
		// Save the field values
		ConfigManager.getConfig().setUserName(view.getUserNameField().getText());
		ConfigManager.getConfig().setProjectName(view.getProjectField().getText());
		
		// Check the core URL and display the main application window
		if (view.getUrlTextField().getText().length() > 0) { // ensure the URL field has content
			final String URLText = view.getUrlTextField().getText();
			final URL coreURL;
			try { // try to convert the URL text to a URL object
				coreURL = new URL(URLText);
				ConfigManager.getConfig().setCoreUrl(coreURL);
				ConfigManager.writeConfig();
				Network.getInstance().setDefaultNetworkConfiguration(new NetworkConfiguration(URLText));
				
				// Send the request
				sendLoginRequest();
				
			} catch (MalformedURLException e1) { // failed, bad URL
				JOptionPane.showMessageDialog(view,
				                              "The server address \"" + URLText + "\" is not a valid URL!",
				                              errorTitle, JOptionPane.ERROR_MESSAGE);
			}
		}
		else { // a URL was not entered
			JOptionPane.showMessageDialog(view, "You must specify the server address!", errorTitle,
			                              JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/**
	 * Constructs a login request and sends it. Uses basic auth to send username
	 * and password (base64 encoded)
	 */
	public void sendLoginRequest() {
		// Form the basic auth string
		String basicAuth = "Basic ";
		String password = new String(view.getPasswordField().getPassword());
		String credentials = view.getUserNameField().getText() + ":" + password;
		basicAuth += Base64.encodeBase64String(credentials.getBytes());

		// Create and send the login request
		Request request = Network.getInstance().makeRequest("login", HttpMethod.POST);
		System.out.println(basicAuth);
		request.addHeader("Authorization", basicAuth);
		request.addObserver(new LoginRequestObserver(this));
		request.send();
	}
	
	/**
	 * Method that is called by {@link LoginRequestObserver} if the login
	 * request was successful.
	 * @param response the response returned by the server
	 */
	public void loginSuccessful(ResponseModel response) {
		// Save the session cookie
		Network.getInstance().getDefaultNetworkConfiguration().addRequestHeader("cookie", response.getHeaders().get("Set-Cookie").get(0).split(";")[0] + ";");
		
		// Show the main GUI
		mainGUI.setVisible(true);
		view.dispose();
	}
	
	/**
	 * Method that is called by {@link LoginRequestObserver} if the login
	 * request was unsuccessful.
	 * @param response the response returned by the server
	 */
	public void loginFailed(String error) {
		JOptionPane.showMessageDialog(view, "Unable to login: " + error, "Login Error", 
				JOptionPane.ERROR_MESSAGE);
	}
}

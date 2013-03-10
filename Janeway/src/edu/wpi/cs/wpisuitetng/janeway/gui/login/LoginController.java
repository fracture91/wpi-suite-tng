/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Chris Casola
 *    Andrew Hurle
 *    JPage
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.janeway.gui.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
		// Save the cookies
		List<String> cookieList = response.getHeaders().get("Set-Cookie");
		String cookieParts[];
		String cookieNameVal[];
		if (cookieList != null) { // if the server returned cookies
			for (String cookie : cookieList) { // for each returned cookie
				cookieParts = cookie.split(";"); // split the cookie
				if (cookieParts.length >= 1) { // if there is at least one part to the cookie
					cookieNameVal = cookieParts[0].split("="); // split the cookie into its name and value
					if (cookieNameVal.length == 2) { // if the split worked, add the cookie to the default NetworkConfiguration
						NetworkConfiguration config = Network.getInstance().getDefaultNetworkConfiguration();
						config.addCookie(cookieNameVal[0], cookieNameVal[1]);
						Network.getInstance().setDefaultNetworkConfiguration(config);
					}
					else {
						System.err.println("Received unparsable cookie: " + cookie);
					}
				}
				else {
					System.err.println("Received unparsable cookie: " + cookie);
				}
			}
			
			System.out.println(Network.getInstance().getDefaultNetworkConfiguration().getRequestHeaders().get("cookie").get(0));

			// Select the project
			Request projectSelectRequest = Network.getInstance().makeRequest("login", HttpMethod.PUT);
			projectSelectRequest.addObserver(new ProjectSelectRequestObserver(this));
			projectSelectRequest.setBody(ConfigManager.getConfig().getProjectName());
			projectSelectRequest.send();
		}
		else {
			JOptionPane.showMessageDialog(view, "Unable to login: no cookies returned.", "Login Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Method that is called by {@link LoginRequestObserver} if the login
	 * request was unsuccessful.
	 * @param response A string representing the error that occurred.
	 */
	public void loginFailed(String error) {
		JOptionPane.showMessageDialog(view, "Unable to login: " + error, "Login Error", 
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Method that is called by {@link ProjectSelectRequestObserver} if the login
	 * request was successful.
	 * 
	 * @param response the response returned by the server
	 */
	public void projectSelectSuccessful(ResponseModel response) {
		// Save the cookies
		List<String> cookieList = response.getHeaders().get("Set-Cookie");
		String cookieParts[];
		String cookieNameVal[];
		if (cookieList != null) { // if the server returned cookies
			for (String cookie : cookieList) { // for each returned cookie
				cookieParts = cookie.split(";");
				if (cookieParts.length >= 1) {
					cookieNameVal = cookieParts[0].split("=");
					if (cookieNameVal.length == 2) {
						NetworkConfiguration config = Network.getInstance().getDefaultNetworkConfiguration();
						config.addCookie(cookieNameVal[0], cookieNameVal[1]);
						Network.getInstance().setDefaultNetworkConfiguration(config);
					}
					else {
						System.err.println("Received unparsable cookie: " + cookie);
					}
				}
				else {
					System.err.println("Received unparsable cookie: " + cookie);
				}
			}

			System.out.println(Network.getInstance().getDefaultNetworkConfiguration().getRequestHeaders().get("cookie").get(0));
			
			// Show the main GUI
			mainGUI.setVisible(true);
			view.dispose();
		}
		else {
			JOptionPane.showMessageDialog(view, "Unable to select project: no cookies returned.", "Project Selection Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Method that is called by {@link ProjectSelectRequestObserver} if the project select
	 * request was unsuccessful.
	 * 
	 * @param error A string representing the error that occurred.
	 */
	public void projectSelectFailed(String error) {
		JOptionPane.showMessageDialog(view, "Unable to select projectc: " + error, "Project Selection Error", 
				JOptionPane.ERROR_MESSAGE);
	}
}

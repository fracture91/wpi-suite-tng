package edu.wpi.cs.wpisuitetng.janeway.gui.login;

import java.util.Observable;
import java.util.Observer;

import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.Response;

public class LoginRequestObserver implements Observer {

	protected LoginController controller;

	public LoginRequestObserver(LoginController controller) {
		this.controller = controller;
	}

	/**
	 * @see java.util.Observer#update
	 */
	@Override
	public void update(Observable observable, Object arg1) {
		// If observable is a Request...
		if (observable instanceof Request) {
			// cast observable to a Request
			Request request = (Request) observable;

			// get the response from the request
			Response response = request.getResponse();
			
			// check the response code
			if (response.getResponseCode() == 100) {
				controller.loginSuccessful(response);
			}
			else { // login failed
				controller.loginFailed(response);
			}
		}
		// Otherwise...
		else {
			System.out.println("Observable is not a Request.");
		}
	}
}

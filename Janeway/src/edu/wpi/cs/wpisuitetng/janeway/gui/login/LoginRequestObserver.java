package edu.wpi.cs.wpisuitetng.janeway.gui.login;

import edu.wpi.cs.wpisuitetng.network.Observable;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.Response;

public class LoginRequestObserver implements RequestObserver {

	protected LoginController controller;

	public LoginRequestObserver(LoginController controller) {
		this.controller = controller;
	}

	@Override
	public void done(Observable observable) {
		// If observable is a Request...
		if (observable instanceof Request) {
			// cast observable to a Request
			Request request = (Request) observable;

			// get the response from the request
			Response response = request.getResponse();

			// check the response code
			if (response.getResponseCode() == 200) {
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

	@Override
	public void error(Observable o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail(Observable o) {
		// TODO Auto-generated method stub

	}
}

package edu.wpi.cs.wpisuitetng.janeway.gui.login;

import edu.wpi.cs.wpisuitetng.network.IRequest;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.Response;

public class LoginRequestObserver implements RequestObserver {

	protected LoginController controller;

	public LoginRequestObserver(LoginController controller) {
		this.controller = controller;
	}

	@Override
	public void success(IRequest iReq) {
		// If observable is a Request...
		if (iReq instanceof Request) {
			// cast observable to a Request
			Request request = (Request) iReq;

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
	public void error(IRequest iReq) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail(IRequest iReq, String errorMessage) {
		// TODO Auto-generated method stub

	}
}

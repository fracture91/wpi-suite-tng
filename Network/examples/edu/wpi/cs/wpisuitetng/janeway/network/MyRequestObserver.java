package edu.wpi.cs.wpisuitetng.janeway.network;

import edu.wpi.cs.wpisuitetng.network.IRequest;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.Response;

/**
 * A RequestObserver for the Request class.
 * 
 * TODO Make this example more thorough.
 */
public class MyRequestObserver implements RequestObserver {
	@Override
	public void responseSuccess(IRequest iReq) {
		// If observable is a Request...
		if (Request.class.getName().equals(iReq.getClass().getName())) {
			// cast observable to a Request
			Request request = (Request) iReq;

			// get the response from the request
			Response response = request.getResponse();

			// print the body
			System.out.println("Received response: " + response.getBody());
		}
		// Otherwise...
		else {
			System.out.println("Observable is not a Request.");
		}
	}

	@Override
	public void responseError(IRequest iReq) {
		// TODO Auto-generated method stub
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		// TODO Auto-generated method stub
	}
}

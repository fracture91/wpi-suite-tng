package edu.wpi.cs.wpisuitetng.janeway.network;

import edu.wpi.cs.wpisuitetng.network.Observable;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.Response;

/**
 * A RequestObserver for the Request class.
 * 
 * TODO Make this example more thorough.
 */
public class MyRequestObserver implements RequestObserver {

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#done
	 */
	@Override
	public void done(Observable observable) {
		// If observable is a Request...
		if (Request.class.getName().equals(observable.getClass().getName())) {
			// cast observable to a Request
			Request request = (Request) observable;

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
	public void error(Observable o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail(Observable o) {
		// TODO Auto-generated method stub

	}
}

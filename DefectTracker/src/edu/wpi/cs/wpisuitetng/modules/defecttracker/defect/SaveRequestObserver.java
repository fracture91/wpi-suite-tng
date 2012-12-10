package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.util.Observable;
import java.util.Observer;

import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.Response;

/**
 * An Observer for the Request class.
 * 
 * TODO Make this example more thorough.
 */
public class SaveRequestObserver implements Observer {

	/**
	 * @see java.util.Observer#update
	 */
	@Override
	public void update(Observable observable, Object arg) {
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
}

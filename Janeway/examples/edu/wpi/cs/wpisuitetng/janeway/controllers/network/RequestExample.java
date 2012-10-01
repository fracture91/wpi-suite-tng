package edu.wpi.cs.wpisuitetng.janeway.controllers.network;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is a preliminary example for making request using the Request class.
 * @author jenny
 * 
 */
public class RequestExample {
	
	public static void main(String[] args) {
		try {
			// Make a new instance of the MyRequestObserver class.
			final MyRequestObserver requestObserver = new MyRequestObserver();
			
			// Make a new Request. TODO fix this so that the body can be ignored.
			Request request = new Request(new URL("http://google.com"), Request.RequestMethod.GET, null);
			
			// Add the requestObserver to the request's set of Observers
			request.addObserver(requestObserver);
			
			// Send the request!
			request.send();
			
			// This should print first.
			System.out.println("This should print first!");
		}
		// For URL
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// For Request#send
		catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

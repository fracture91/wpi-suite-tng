package edu.wpi.cs.wpisuitetng.janeway.network;

import java.net.MalformedURLException;
import java.net.URL;

import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestFactory;
import edu.wpi.cs.wpisuitetng.network.Request.RequestMethod;

/**
 * This is a preliminary example for making request using the Request class.
 * @author jenny
 * 
 */
public class RequestExample {
	private static String host = "localhost";
	private static int port = 8080;
	
	/**
	 * Main method.
	 */
	public static void main(String[] args) {
		try {
			// Make a new instance of the MyRequestObserver class.
			final MyRequestObserver requestObserver = new MyRequestObserver();
			
			
			
			
			
			/* ~~~ Below shows how to create a defect manually. ~~~ */
			
			// The first Request's body
			String body = "body 1";
			
			// Create the URL
			URL url = new URL("http", host, port, "/myModule/myModel"); //TODO switch to https
			
			// Make a new POST Request.
			Request manualRequest = new Request(url, RequestMethod.POST);	// construct the Request
			
			// Configure the request
			manualRequest.setRequestBody(body);	// set the request body to send to the server
			manualRequest.addObserver(requestObserver);	// Add the requestObserver to the request's set of Observers
			
			// Send the request!
			manualRequest.send();
			
			// This should print first.
			System.out.println("This was printed after sending the manually created Request!");
			
			
			
			
			
			/* ~~~ Below shows how to create a Request using a RequestFactory. ~~~ */
			
			// The second request's body
			String body2 = "body 2";
			
			// create a new RequestFactory
			RequestFactory factory = new RequestFactory(host, port);
			
			// Make a request using the RequestFactory
			Request manufacturedRequest = factory.makeRequest("myModule", "myModel", Request.RequestMethod.POST);
			
			// Configure the Request
			manufacturedRequest.setRequestBody(body2);	// set the request body to send to the server
			manufacturedRequest.addObserver(requestObserver);	// Add the requestObserver to the request's set of Observers
			
			// Send the request!
			manufacturedRequest.send();
			
			System.out.println("This was printed after sending the manufactured Request!");
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

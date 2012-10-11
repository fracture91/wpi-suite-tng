package edu.wpi.cs.wpisuitetng.janeway.controllers.network;

import java.net.MalformedURLException;
import java.net.URL;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;

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
			
			// Create a Defect
			Defect defect = new Defect(1, "This is the description of a defect sent in a manually created Request.", "Defect 1", "JJ");
			
			// Create the URL
			URL url = new URL("http", host, port, "/myModule/myModel"); //TODO switch to https
			
			// Make a new POST Request.
			Request manualRequest = new Request(url);	// construct the Request
			
			// Configure the request
			manualRequest.setRequestMethod(Request.RequestMethod.POST);	// set the request method to POST
			manualRequest.setRequestBody(defect.toJSON());	// set the request body to send to the server
			manualRequest.addObserver(requestObserver);	// Add the requestObserver to the request's set of Observers
			
			// Send the request!
			manualRequest.send();
			
			// This should print first.
			System.out.println("This was printed after sending the manually created Request!");
			
			
			
			
			
			/* ~~~ Below shows how to create a defect using a RequestFactory. ~~~ */
			
			// Create a Defect
			Defect defect2 = new Defect(1, "This is the description of a defect sent in a manufactured Request.", "Defect 2", "JJ");
			
			// create a new RequestFactory
			RequestFactory factory = new RequestFactory(host, port);
			
			// Make a request using the RequestFactory
			Request manufacturedRequest = factory.makeRequest("myModule", "myModel");
			
			// Configure the Request
			manufacturedRequest.setRequestMethod(Request.RequestMethod.POST);	// set the request method to POST
			manufacturedRequest.setRequestBody(defect2.toJSON());	// set the request body to send to the server
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

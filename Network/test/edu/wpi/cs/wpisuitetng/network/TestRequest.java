package edu.wpi.cs.wpisuitetng.network;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import org.junit.*;


import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.Response;
import edu.wpi.cs.wpisuitetng.network.Request.RequestMethod;

public class TestRequest {
	class MockObserver implements Observer {
		
		private boolean updateCalled;
		private Response response;
		
		public MockObserver() {
			super();
			updateCalled = false;
			response = null;
			
		}

		/**
		 * @see java.util.Observable#update
		 */
		@Override
		public void update(Observable observable, Object arg) {
			synchronized (this) {
	            notifyAll(  );
	        }
			// If observable is a Request...
			if (Request.class.getName().equals(observable.getClass().getName())) {
				// cast observable to a Request
				Request request = (Request) observable;

				// get the response from the request
				response = request.getResponse();

				// print the body
			}
			// Otherwise...
			else {
				System.out.println("Observable is not a Request.");
			}
		}
	}
	
	private static int port = 8080;
	
	/**
	 * Test that a NullPointerException is thrown when a null url is passed to the Request constructor.
	 * @throws MalformedURLException 
	 */
	@Test
	public void testRequestConstructorNullPointerException() throws MalformedURLException {
		try {
			Request r = new Request(null, null);
			fail("No exception thrown.");
		} catch (NullPointerException e) {
			// Do nothing
		}
		
		try {
			Request r = new Request(new URL("http://wpi.edu"), null);
			fail("No exception thrown.");
		} catch (NullPointerException e) {
			// Do nothing
		}
	}
	
	/**
	 * Test that a NullPointerException is thrown when a null requestMethod is passed to the Request#setRequestMethod.
	 */
	@Test
	public void testRequestSetRequestMethodNullPointerException() {
		try {
			Request r = new Request(new URL("http://localhost:8080"), RequestMethod.POST);
			r.setRequestMethod(null);
			fail("No exception thrown.");
		} catch (NullPointerException e) {
			// Do nothing
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test that a NullPointerException is thrown when a null body is passed to the Request#setRequestBody.
	 */
	@Test
	public void testRequestSetRequestBodyNullPointerException() {
		try {
			Request r = new Request(new URL("http://localhost:8080"), RequestMethod.POST);
			r.setRequestBody(null);
			fail("No exception thrown.");
		} catch (NullPointerException e) {
			// Do nothing
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test that communication works. DummyServer must be running on port 8080 for this test to work.
	 */
	@Test
	public void testRequestCommunication() {
		// Make a new instance of the MyRequestObserver class.
		final MockObserver requestObserver = new MockObserver();

		// The request body
		String body = "This is the request body!";
		
		// Create the URL
		try {
			URL url = new URL("http", "localhost", port, "/myModule/myModel");


			// Make a new POST Request.
			Request manualRequest = new Request(url, RequestMethod.POST);	// construct the Request

			// Configure the request
			manualRequest.setRequestBody(body);	// set the request body to send to the server
			manualRequest.addObserver(requestObserver);	// Add the requestObserver to the request's set of Observers

			// Send the request!
			manualRequest.send();
			synchronized (requestObserver) {
				requestObserver.wait(2000);
		    }
			
			assertEquals(true, (body+"\n").equals(manualRequest.getResponse().getBody()));
			assertEquals(200, manualRequest.getResponse().getResponseCode());
			assertEquals(true, "OK".equalsIgnoreCase(manualRequest.getResponse().getResponseMessage()));
		} catch (MalformedURLException e) {
			fail("MalformedURLException");
		} //TODO switch to https
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
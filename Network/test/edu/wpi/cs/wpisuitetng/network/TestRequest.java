package edu.wpi.cs.wpisuitetng.network;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.*;


import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.Response;
import edu.wpi.cs.wpisuitetng.network.Request.RequestMethod;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;

public class TestRequest {
	class MockObserver implements RequestObserver {

		private boolean updateCalled;
		private Response response;

		public MockObserver() {
			super();
			updateCalled = false;
			response = null;

		}

		@Override
		public void responseReceived(IRequest o) {
			// TODO Auto-generated method stub
			synchronized (this) {
				notifyAll(  );
			}
			// If observable is a Request...
			if (Request.class.getName().equals(o.getClass().getName())) {
				// cast observable to a Request
				Request request = (Request) o;

				// get the response from the request
				response = request.getResponse();

				// print the body
			}
			// Otherwise...
			else {
				System.out.println("Observable is not a Request.");
			}
		}

		@Override
		public void responseError(IRequest o) {
			// TODO Auto-generated method stub

		}

		@Override
		public void requestFail(IRequest o) {
			// TODO Auto-generated method stub

		}

		@Override
		public void before(IRequest o) {
			// TODO Auto-generated method stub

		}
	}

	private NetworkConfiguration config;
	private static int port = 8080;

	@Before
	public void setUp() {
		config = new NetworkConfiguration("http://localhost:" + port);
	}

	/**
	 * Test that a NullPointerException is thrown when a null networkConfiguration is passed to the Request constructor.
	 * @throws MalformedURLException 
	 */
	@Test
	public void testRequestConstructorNullPointerException() throws MalformedURLException {
		try {
			Request r = new Request(null, null, null);
			fail("No NullPointerException thrown when constructing a Request with null networkConfiguration parameter.");
		} catch (NullPointerException e) {
			assertTrue("The networkConfiguration must not be null.".equals(e.getMessage()));
		}


	}

	/**
	 * Test that a NullPointerException is thrown when a null requestMethod is passed to the Request#setRequestMethod.
	 * @throws MalformedURLException 
	 */
	@Test
	public void testRequestSetRequestMethodNullPointerException() throws MalformedURLException {
		try {
			Request r = new Request(config, null, null);
			fail("No NullPointerException thrown when constructing a Request with null requestMethod parameter.");
		} catch (NullPointerException e) {
			assertTrue("The requestMethod must not be null.".equals(e.getMessage()));
		}
	}

	/**
	 * Test that a NullPointerException is thrown when a null body is passed to the Request#setRequestBody.
	 */
	@Test
	public void testRequestSetRequestBodyNullPointerException() {
		try {
			Request r = new Request(config, null, RequestMethod.POST);
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
			Request manualRequest = new Request(config, null, RequestMethod.POST);	// construct the Request

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
package edu.wpi.cs.wpisuitetng.network;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.*;


import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.dummyserver.DummyServer;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

public class TestRequest {
	class MockObserver implements RequestObserver {

		private boolean updateCalled;
		private ResponseModel response;

		public MockObserver() {
			super();
			updateCalled = false;
			response = null;
		}

		@Override
		public void responseSuccess(IRequest iReq) {
			synchronized (this) {
				notifyAll(  );
			}
			// get the response from the request
			response = iReq.getResponse();
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
	@Test(expected = NullPointerException.class)
	public void testRequestConstructorNullPointerException() throws MalformedURLException {
		new Request(null, null, null);
	}

	/**
	 * Test that a NullPointerException is thrown when a null requestMethod is passed to the Request#setRequestMethod.
	 * @throws MalformedURLException 
	 */
	@Test(expected = NullPointerException.class)
	public void testRequestSetRequestMethodNullPointerException() throws MalformedURLException {
		new Request(config, null, null);
	}

	/**
	 * Test that a NullPointerException is thrown when a null body is passed to the Request#setRequestBody.
	 */
	@Test(expected = NullPointerException.class)
	public void testRequestSetRequestBodyNullPointerException() {
		Request r = new Request(config, null, HttpMethod.POST);
		r.setBody(null);
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
			DummyServer server = new DummyServer(port);
			server.start();

			// Make a new POST Request.
			Request manualRequest = new Request(config, null, HttpMethod.POST);	// construct the Request

			// Configure the request
			manualRequest.setBody(body);	// set the request body to send to the server
			manualRequest.addObserver(requestObserver);	// Add the requestObserver to the request's set of Observers

			// Send the request!
			manualRequest.send();
			synchronized (requestObserver) {
				requestObserver.wait(2000);
			}

			//assertEquals(true, (body+"\n").equals(manualRequest.getResponse().getBody()));
			assertEquals(200, manualRequest.getResponse().getStatusCode());
			assertEquals(true, "OK".equalsIgnoreCase(manualRequest.getResponse().getStatusMessage()));
			
			assertTrue(body.equals(server.getLastReceived().getBody()));
			assertEquals(manualRequest.getHttpMethod(), server.getLastReceived().getHttpMethod());
			
			server.stop();
		} //TODO switch to https
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
package edu.wpi.cs.wpisuitetng.modules.defecttracker;

import java.net.MalformedURLException;

import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;

public class MockRequest extends Request {
	
	protected boolean sent = false;

	public MockRequest(NetworkConfiguration networkConfiguration, String path, RequestMethod requestMethod) throws NullPointerException, MalformedURLException {
		super(networkConfiguration, path, requestMethod);
	}

	@Override
	public void send() throws IllegalStateException {
		// don't actually send
		sent = true;
	}
	
	public boolean isSent() {
		return sent;
	}
}

package edu.wpi.cs.wpisuitetng.modules.defecttracker;

import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;

public class MockNetwork extends Network {
	
	protected MockRequest lastRequestMade = null;
	
	@Override
	public Request makeRequest(String path, Request.RequestMethod requestMethod) {
		if (requestMethod == null) {
			throw new NullPointerException("requestMethod may not be null");
		}
		
		lastRequestMade = new MockRequest(defaultNetworkConfiguration, path, requestMethod); 
		
		return lastRequestMade;
	}
	
	public MockRequest getLastRequestMade() {
		return lastRequestMade;
	}
}

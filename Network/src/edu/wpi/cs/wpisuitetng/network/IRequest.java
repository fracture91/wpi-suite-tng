package edu.wpi.cs.wpisuitetng.network;

import java.net.URL;
import java.util.List;
import java.util.Map;

public interface IRequest {
	public Response getResponse();
	public URL getURL();
	public String getRequestMethod();
	public Map<String, List<String>> getRequestHeaders();
	
	public String getRequestBody();
}

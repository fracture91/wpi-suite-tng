package edu.wpi.cs.wpisuitetng.network.models;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * An interface for providing information regarding a network request. This 
 * interface allows for RequestObserver to be a superclass of Request while 
 * preventing the need for developers to cast a RequestObserver to a Request 
 * inside their responseSuccess, responseError and fail methods.
 */
public interface IRequest {
	/**
	 * Returns a String containing the request body.
	 * 
	 * @return	A String containing the request body.
	 */
	public String getBody();
	
	/**
	 * Returns a Map containing the url data.
	 * 
	 * @return a Map containing the url data.
	 */
	public Map<String, String> getUrlData();
	
	/**
	 * Returns a Map of request header keys to Lists of request header values.
	 * 
	 * @return	A Map of request header keys to Lists of request header values.
	 */
	public Map<String, List<String>> getHeaders();
	
	/**
	 * Returns a HttpMethod representing the HTTP request method.
	 * 
	 * @return	A HttpMethod representing the request method.
	 */
	public HttpMethod getHttpMethod();
	
	/**
	 * Returns the server's Response to the Request.
	 * 
	 * @return	The server's Response to the Request.
	 */
	public ResponseModel getResponse();

	/**
	 * Returns a URL pointing to the server.
	 * 
	 * @return	A URL pointing to the server.
	 */
	public URL getUrl();
	
}

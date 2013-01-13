package edu.wpi.cs.wpisuitetng.network;

/**
 * Interface for observing requests.
 */
public interface RequestObserver {
	/**
	 * Called when a response is received with a success (2xx) status code.
	 * 
	 * @param iReq	An instance of a class that implements an iRequest.
	 */
	public void success(IRequest iReq);
	
	/**
	 * Called when a response is received with an client error (4xx) or server error (5xx) status code.
	 * 
	 * @param iReq	An instance of a class that implements an iRequest.
	 */
	public void error(IRequest iReq);
	
	/**
	 * Called if an attempt to make a request fails.
	 * 
	 * @param iReq	        An instance of a class that implements an iRequest.
	 * @param errorMessage An error message.
	 */
	public void fail(IRequest iReq, String errorMessage);
}

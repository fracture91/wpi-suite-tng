package edu.wpi.cs.wpisuitetng.network;

/**
 * Interface for observing requests.
 */
public interface RequestObserver {
	/**
	 * Called when a response is received.
	 * 
	 * @param o
	 */
	public void responseReceived(IRequest o);
	
	/**
	 * Called on when a response is received with a 400 or 500 error.
	 * 
	 * @param o
	 */
	public void responseError(IRequest o);
	
	/**
	 * Called if an attempt to make a request fails.
	 * 
	 * @param o
	 */
	public void requestFail(IRequest o);
	
	/**
	 * Called before a request attempt is made.
	 */
	public void before(IRequest o);
}

package edu.wpi.cs.wpisuitetng.network;

/**
 * Interface for observing requests.
 */
public interface RequestObserver {
	/**
	 * Called on successful completion of a request (error code 200).
	 * 
	 * @param o
	 */
	public void done(Observable o);
	
	/**
	 * Called on unsuccessful completion of a request (based on error code).
	 * 
	 * @param o
	 */
	public void error(Observable o);
	
	/**
	 * Called if an attempt to make a request fails.
	 * 
	 * @param o
	 */
	public void fail(Observable o);
}

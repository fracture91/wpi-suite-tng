package edu.wpi.cs.wpisuitetng.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class exists only to be extended by Request.
 */
public abstract class Observable {
	private List<RequestObserver> observers;

	public Observable() {
		observers = new ArrayList<RequestObserver>();
	}

	/**
	 * Adds a RequestObserver to this Observable.
	 * 
	 * @param o The RequestObserver to add.
	 */
	public void addObserver(RequestObserver o) {
		observers.add(o);
	}
	
	/**
	 * Returns the number of RequestObservers for this Observable.
	 * 
	 * @return The number of RequestObservers for this Observable.
	 */
	public int countObservers() {
		return observers.size();
	}

	/**
	 * Notifies RequestObservers of request completion.
	 */
	public void notifyObserversResponseReceived() {
		Iterator<RequestObserver> observersI = observers.iterator();

		while (observersI.hasNext()) {
			observersI.next().responseReceived((IRequest) this);
		}
	}

	/**
	 * Notifies RequestObservers of response 400 or 500 error.
	 */
	public void notifyObserversResponseError() {
		Iterator<RequestObserver> observersI = observers.iterator();

		while (observersI.hasNext()) {
			observersI.next().responseError((IRequest) this);
		}
	}

	/**
	 * Notifies RequestObservers of request failure.
	 */
	public void notifyObserversRequestFail() {
		Iterator<RequestObserver> observersI = observers.iterator();

		while (observersI.hasNext()) {
			observersI.next().requestFail((IRequest) this);
		}
	}
	
	/**
	 * Notifies RequestObservers of before request.
	 */
	public void notifyObserversBefore() {
		Iterator<RequestObserver> observersI = observers.iterator();

		while (observersI.hasNext()) {
			observersI.next().before((IRequest) this);
		}
	}
}

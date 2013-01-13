package edu.wpi.cs.wpisuitetng.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Observable {
	private List<RequestObserver> observers;
	private boolean changed;

	public Observable() {
		observers = new ArrayList<RequestObserver>();
		changed = false;
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
	 * Sets the Observable's status to changed.
	 */
	public void setChanged() {
		changed = true;
	}

	/**
	 * Clears the Observable's status of being changed.
	 */
	public void clearChanged() {
		changed = false;
	}

	/**
	 * Returns whether or not this Observable has been changed.
	 * 
	 * @return	Whether or not this Observable has been changed.
	 */
	public boolean hasChanged() {
		return changed;
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
	public void notifyObserversDone() {
		Iterator<RequestObserver> observersI = observers.iterator();

		while (observersI.hasNext()) {
			observersI.next().done(this);
		}
	}

	/**
	 * Notifies RequestObservers of request error.
	 */
	public void notifyObserversError() {
		Iterator<RequestObserver> observersI = observers.iterator();

		while (observersI.hasNext()) {
			observersI.next().error(this);
		}
	}

	/**
	 * Notifies RequestObservers of request failure.
	 */
	public void notifyObserversFail() {
		Iterator<RequestObserver> observersI = observers.iterator();

		while (observersI.hasNext()) {
			observersI.next().fail(this);
		}
	}
}

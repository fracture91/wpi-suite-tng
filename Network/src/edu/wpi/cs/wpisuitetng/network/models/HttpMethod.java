package edu.wpi.cs.wpisuitetng.network.models;

public enum HttpMethod {
	GET, 
	POST, 
	PUT, 
	DELETE;
	
	public String toString() {
		return this.name();
	}
}

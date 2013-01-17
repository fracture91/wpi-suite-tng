package edu.wpi.cs.wpisuitetng.network.models;

public enum RequestMethod {
	GET("GET"), 
	POST("POST"), 
	PUT("PUT"), 
	DELETE("DELETE");
	
	private final String stringRepresentation;
	
	private RequestMethod(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	
	public String toString() {
		return stringRepresentation;
	}
}

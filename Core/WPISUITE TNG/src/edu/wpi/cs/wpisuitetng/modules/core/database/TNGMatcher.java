package edu.wpi.cs.wpisuitetng.modules.core.database;

public class TNGMatcher<T> {

	T theValue;
	public TNGMatcher(T aValue){
		theValue = aValue;
	}
	
	public T getValue(){
		return this.theValue;
	}
	
	public void setValue(T aValue){
		this.theValue = aValue;
	}
}

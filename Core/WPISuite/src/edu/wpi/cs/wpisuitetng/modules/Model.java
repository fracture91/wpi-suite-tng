package edu.wpi.cs.wpisuitetng.modules;

import java.lang.String;

/**
 * Model : The interface for all data models. Prototypes methods for model handling
 * 		and serializing.
 * @author twack
 *
 */
public interface Model {

	/* database interaction */
	public void save();
	public void delete();
	
	/* serializing */
	
	/** toJSON : serializing this Model's contents into a JSON/GSON string
	 * @return	A string containing the serialized JSON representation of this Model.
	 */
	public String toJSON();
	
	/* Built-in overrides/overloads */
	
	/** toString : enforce an override. May simply call serializeToJSON.
	 * @return	The string representation of this Model
	 */
	public String toString();
	
	/**
	 * identify: if the argument o is equal this object's unique identifier
	 * this method was created for use with the mock database
	 * 
	 * @param o - a unique identifier belonging to an object
	 * @return true if the o is equal to this Model's unique identifier, else false
	 */
	public Boolean identify(Object o);
}

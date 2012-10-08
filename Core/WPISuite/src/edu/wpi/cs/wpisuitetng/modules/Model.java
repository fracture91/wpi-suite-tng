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
}

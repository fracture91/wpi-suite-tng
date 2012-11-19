package edu.wpi.cs.wpisuitetng.core;

import java.util.List;

/**
 * Interface that dictates database behavior 
 * @author rchamer
 * @param <C>
 *
 */
public interface DatabaseInterface<C, T> {

	/** Retrieves objects of type objectType when the value of the field
	 * fieldName equals compareValue
	 * The object type is the class of the object to be retrieved
	 * 
	 * @param objectType - Class of object to be retrieved
	 * @param fieldName - Name of the field that will be compared against
	 * @param compareValue - Value of field, fieldName, that is being queried against
	 * @return The object that is retrieved
	 */
	public List<T> retrieve(Class objectType, String fieldName, Object compareValue);
	
	/**
	 * Updates the object with the value uniqueID for the field fieldName
	 * and changes the value of the field changeField to the value changeValue
	 * 
	 * @param objectType - object type of object to be retrieved
	 * @param fieldName - field whose value the object will be retrieved by
	 * @param uniqueID - value of the field that the object is retrieved by
	 * @param changeValue - field whose value will be changed 
	 */
	public void update(Class objectType, String fieldName,Object uniqueID, String changeField, Object changeValue);
	
	/**
	 * Deletes the object of type objectType which has the value uniqueID in the field fieldName
	 * 
	 * For this method to work there must be a setter of the form setFieldName which returns
	 * a copy of the updated object
	 * @param objectType
	 * @param fieldName
	 * @param uniqueID
	 * @return The object that was deleted
	 */
	public T delete(Class objectType, String fieldName, Object uniqueID);

}

package edu.wpi.cs.wpisuitetng.core;
/**
 * Interface that dictates database behavior 
 * @author rchamer
 * @param <C>
 *
 */
public interface DatabaseInterface<C, T> {

	/** Retrieves objects of type objectType when the value of the field
	 * fieldName equals compareValue
	 * 
	 * @param objectType - Class of object to be retrieved
	 * @param fieldName - Name of the field that will be compared against
	 * @param compareValue - Value of field, fieldName, that is being queried against
	 * @return The object that is retrieved
	 */
	public T retrieve(T objectType, String fieldName, C compareValue);
	
	/**
	 * Updates the object with the value uniqueID for the field fieldName
	 * and changes the value of 
	 * @param objectType
	 * @param fieldName
	 * @param uniqueID
	 * @param changeValue
	 */
	public void update(T objectType, String fieldName,C uniqueID, String changeField, C changeValue);
	
	/**
	 * Deletes the object of type objectType which has the value uniqueID in the field fieldName
	 * @param objectType
	 * @param fieldName
	 * @param uniqueID
	 * @return The object that was deleted
	 */
	public T delete(T objectType, String fieldName,C uniqueID);
}

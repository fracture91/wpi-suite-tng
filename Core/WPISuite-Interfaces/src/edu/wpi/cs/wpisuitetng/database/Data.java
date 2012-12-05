package edu.wpi.cs.wpisuitetng.database;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.Model;

public interface Data 
{
	public <T> boolean save(T aTNG);
	public List<Model> retrieve(@SuppressWarnings("rawtypes") final Class anObjectQueried, String aFieldName, final Object theGivenValue);
	public <T> T delete(T aTNG);
	public void update(final Class anObjectToBeModified, String fieldName, Object uniqueID, String changeField, Object changeValue);
	
}

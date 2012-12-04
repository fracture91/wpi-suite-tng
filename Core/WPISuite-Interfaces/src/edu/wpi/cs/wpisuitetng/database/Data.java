package edu.wpi.cs.wpisuitetng.database;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.Model;

public interface Data 
{
	public <T> boolean save(T aTNG);
	public List<Model> retrieve(final Class anObjectQueried, String aFieldName, final Object theGivenValue);
	public <T> T delete(T aTNG);
	
}

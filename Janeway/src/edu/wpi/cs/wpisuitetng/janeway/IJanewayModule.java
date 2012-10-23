package edu.wpi.cs.wpisuitetng.janeway;

import java.util.List;


/**
 * A module being used in Janeway must implement this interface.
 * Modules have a name and a list of tabs associated with them.
 */
public interface IJanewayModule {
	
	/**
	 * @return The name of the module (e.g. "Defect Tracker").
	 */
	public String getName();
	
	/**
	 * @return The list of tab models associated with this module.
	 */
	public List<JanewayTabModel> getTabs();
	
}

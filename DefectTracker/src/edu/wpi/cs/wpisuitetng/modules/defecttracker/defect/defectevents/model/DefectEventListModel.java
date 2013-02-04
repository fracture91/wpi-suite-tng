package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.defectevents.model;

import javax.swing.DefaultListModel;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;

/**
 * A data model for a list of DefectEvents.
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked"})
public class DefectEventListModel extends DefaultListModel {
	
	/**
	 * Construct a new model and populate with the DefectEvents contained
	 * in the given Defect
	 * 
	 * @param defect the Defect containing the events to populate this model
	 */
	public DefectEventListModel(Defect defect) {
		update(defect);
	}
	
	/**
	 * Replaces the contents of this model with the events in the given Defect
	 * @param model the Defect containing the events to populate this model
	 */
	public void update(Defect model) {
		this.clear();
		
		for (DefectEvent event : model.getEvents()) {
			addElement(event);
		}
	}
}

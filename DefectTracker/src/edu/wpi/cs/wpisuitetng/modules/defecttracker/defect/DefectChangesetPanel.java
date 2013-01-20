package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectChangeset;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.FieldChange;

@SuppressWarnings("serial")
public class DefectChangesetPanel extends DefectEventPanel {

	public DefectChangesetPanel(DefectChangeset event) {
		setTitle("Fields changed by: " + event.getUser().getUsername());
		
		// build content from map of field changes
		String content = "";
		Map<String, FieldChange> changes = event.getChanges();
		for (String fieldName : changes.keySet()) {
			content += fieldName + " changed from " + changes.get(fieldName).getOldValue().toString() + " to " + changes.get(fieldName).getNewValue().toString() + "\n";
		}
		setContent(content);
	}
}

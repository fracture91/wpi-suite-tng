package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectChangeset;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.FieldChange;

@SuppressWarnings("serial")
public class DefectChangesetPanel extends DefectEventPanel {

	public DefectChangesetPanel(DefectChangeset event) {
		setTitle("Fields changed by: " + event.getUser().getUsername());
		
		// TODO need to parse assignee and tag FieldChanges correctly
		// build content from map of field changes
		String content = "<html>";
		Map<String, FieldChange> changes = event.getChanges();
		for (String fieldName : changes.keySet()) {
			content += fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			if (fieldName.equals("title") || fieldName.equals("assignee") || fieldName.equals("tags")) {
				content += " <b>FROM</b><i> " + changes.get(fieldName).getOldValue().toString() + " </i><b>TO</b><i> " + changes.get(fieldName).getNewValue().toString() + "</i><br />";
			}
			else if (fieldName.equals("description")) {
				content += " <b>FROM</b><i> " + changes.get(fieldName).getOldValue().toString().substring(0, Math.min(35, changes.get(fieldName).getOldValue().toString().length() - 1)) + "...</i> <b>TO</b><i> " + changes.get(fieldName).getNewValue().toString().substring(0, Math.min(35, changes.get(fieldName).getNewValue().toString().length() - 1)) + "...</i><br />";
			}
			else {
				content += fieldName + " changed from " + changes.get(fieldName).getOldValue().toString() + " to " + changes.get(fieldName).getNewValue().toString() + "<br />";
			}
		}
		content += "</html>";
		setContent(content);
	}
}

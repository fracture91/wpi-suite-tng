package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectChangeset;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.FieldChange;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Tag;

/**
 * This panel displays a {@link edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectChangeset}
 * A DefectChangeset contains a number of FieldChange<T> objects of generic type T. Currently,
 * this panel can display FieldChanges of type String, User, and Set<Tag>
 *
 */
@SuppressWarnings("serial")
public class DefectChangesetPanel extends DefectEventPanel {

	/**
	 * Construct the JPanel
	 * @param event the DefectChangeset to display
	 */
	@SuppressWarnings("unchecked")
	public DefectChangesetPanel(DefectChangeset event) {
		setTitle("<html>Modified by <i>" + event.getUser().getUsername() + " </i>on <i>" + new SimpleDateFormat("MM/dd/yy hh:mm a").format(event.getDate()) + "</i></html>");

		// build content from map of field changes
		String content = "<html>";
		Map<String, FieldChange<?>> changes = event.getChanges();

		// add content for each field change
		for (String fieldName : changes.keySet()) {

			// get class information for Set<Tag>
			Set<Tag> tagSet = new HashSet<Tag>();
			Class<?> tagSetClass = tagSet.getClass();

			// Get the old and new field objects from the FieldChange
			Object oldField = changes.get(fieldName).getOldValue();
			Object newField = changes.get(fieldName).getNewValue();

			// Add the field name to the content label
			content += fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

			// handle fields of type String
			if (String.class.isInstance(newField)) {
				String oldValue = (String)oldField;
				String newValue = (String)newField;

				if (oldValue.length() > 0) {
					content += " <b>FROM</b><i> ";
					content += (oldValue.length() < 35) ? oldValue : oldValue.substring(0, 35) + "...";
					content += " </i><b>TO</b><i> ";
					content += (newValue.length() < 35) ? newValue : newValue.substring(0, 35) + "...";
					content += "</i><br />";
				}
				else {
					content += " <b>NEW</b><i> ";
					content += (newValue.length() < 35) ? newValue : newValue.substring(0, 35) + "...";
					content += "</i><br />";
				}
			}
			// handle fields of type User
			else if (User.class.isInstance(newField)) {
				User oldValue = (User)oldField;
				User newValue = (User)newField;
				if (oldValue != null) {
					content += " <b>FROM</b><i> " + oldValue.getUsername() + " </i><b>TO</b><i> " + newValue.getUsername() + "</i><br />";
				}
				else {
					content += " <b>NEW</b><i> " + newValue.getUsername() + "</i><br />";
				}
			}
			// handle fields of type Set<Tag>
			else if (tagSetClass.isInstance(oldField) && tagSetClass.isInstance(newField)) { 
				Set<Tag> oldTags = (Set<Tag>)oldField;
				Set<Tag> newTags = (Set<Tag>)newField;
				if (oldTags.size() > 0) {
					content += " <b>OLD</b><i> ";
					for (Tag tag : oldTags) {
						content += tag.getName() + ", ";
					}
					content += " </i>";
				}
				content += " <b>NEW</b><i> ";
				for (Tag tag : newTags) {
					content += tag.getName() + ", ";
				}
				content += "</i><br />";
			}
			// the field type is not recognized
			else {
				throw new RuntimeException("Cannot handle a FieldChange of generic type " + oldField);
			}
		}
		content += "</html>";
		setContent(content);
	}
}

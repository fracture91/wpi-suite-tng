package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.comments;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectEventPanel;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;

@SuppressWarnings("serial")
public class ViewCommentPanel extends DefectEventPanel {

	public ViewCommentPanel(Comment comment) {
		setTitle("Comment by " + comment.getUser().getUsername() + " on " + comment.getDate().toString());
		setContent(comment.getBody());
	}
}

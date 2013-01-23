package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;

@SuppressWarnings("serial")
public class CommentPanel extends DefectEventPanel {

	public CommentPanel(Comment comment) {
		setTitle("Comment by " + comment.getUser().getUsername() + " on " + comment.getDate().toString());
		setContent(comment.getBody());
	}
}

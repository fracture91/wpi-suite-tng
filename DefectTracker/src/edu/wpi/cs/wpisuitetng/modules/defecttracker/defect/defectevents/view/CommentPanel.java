package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.defectevents.view;

import java.text.SimpleDateFormat;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;

@SuppressWarnings("serial")
public class CommentPanel extends DefectEventPanel {

	public CommentPanel(Comment comment) {
		setTitle("<html><font size=4><b>" + comment.getUser().getName() + "<font size=.25></b> commented on " + new SimpleDateFormat("MM/dd/yy hh:mm a").format(comment.getDate()) + "</html>");
		setContent("<html><i>" + comment.getBody() + "</i></html>");
	}
}

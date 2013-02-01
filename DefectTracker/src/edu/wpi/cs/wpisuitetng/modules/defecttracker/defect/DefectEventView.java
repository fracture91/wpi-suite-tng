package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.comments.ViewCommentPanel;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectChangeset;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;

/**
 * View to display defect events
 */
@SuppressWarnings("serial")
public class DefectEventView extends JPanel {

	private List<DefectEventPanel> defectEventPanels;
	private final DefectPanel view;

	/**
	 * Constructs the view
	 * @param model the defect model to grab events from
	 * @param view the parent view
	 */
	public DefectEventView(Defect model, DefectPanel view) {

		defectEventPanels = new ArrayList<DefectEventPanel>();
		this.view = view;
		this.setLayout(new GridLayout(0, 1));

		// Add changes from the model to this panel
		update(model);
	}
	
	/**
	 * Removes all currently displayed defect events and replaces it with
	 * those contained in the given Defect model
	 * @param model the defect model
	 */
	public void update(Defect model) {
		// Remove old events
		this.removeAll();
		defectEventPanels = new ArrayList<DefectEventPanel>();
		
		// Construct and add the new events
		try {
			for (DefectEvent event : model.getEvents()) {
				if (event instanceof DefectChangeset) {
					defectEventPanels.add(new DefectChangesetPanel((DefectChangeset)event));
				}
				else if (event instanceof Comment) {
					defectEventPanels.add(new ViewCommentPanel((Comment) event));
				}
			}
	
			for (DefectEventPanel panel : defectEventPanels) {
				this.add(panel);
			}
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, "An error occurred displaying the change history of this defect.", "Error Displaying Changes", JOptionPane.ERROR_MESSAGE);
			// TODO log this error
		}
		
		// Refresh this view
		this.validate();
		this.repaint();
	}
	
	/**
	 * Adds a comment to the list of defect events without replacing those already
	 * being displayed
	 * @param comment the new comment to add
	 */
	public void addComment(Comment comment) {
		ViewCommentPanel panel = new ViewCommentPanel(comment);
		defectEventPanels.add(panel);
		this.add(panel);
		
		// Refresh the parent view
		view.refresh();
				
		// Scroll the scroll bar to the bottom
		view.getParent().scrollToBottom();
	}
}

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

@SuppressWarnings("serial")
public class DefectEventView extends JPanel {

	List<DefectEventPanel> defectEventPanels;

	public DefectEventView(Defect model) {

		defectEventPanels = new ArrayList<DefectEventPanel>();
		this.setLayout(new GridLayout(0, 1));

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
	}
}

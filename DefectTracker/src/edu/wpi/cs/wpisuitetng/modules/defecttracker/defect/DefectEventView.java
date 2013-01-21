package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

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

		for (DefectEvent event : model.getEvents()) {
			if (event instanceof DefectChangeset) {
				defectEventPanels.add(new DefectChangesetPanel((DefectChangeset)event));
			}
			else if (event instanceof Comment) {
				defectEventPanels.add(new CommentPanel((Comment) event));
			}
		}

		for (DefectEventPanel panel : defectEventPanels) {
			this.add(panel);
		}
	}
}

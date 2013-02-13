package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.defectevents.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectChangeset;

/**
 * A cell renderer for a list of DefectEvents
 */
public class DefectEventListCellRenderer implements ListCellRenderer {
	
	/*
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		// The JPanel to represent this cell
		final JPanel panel;
		
		if (value instanceof Comment) { // if the cell contains a comment, construct a ViewCommentPanel
			panel = new CommentPanel((Comment)value);
		}
		else if (value instanceof DefectChangeset) { // if the cell contains a DefectChangeset, then construct a DefectChangesetPanel
			panel = new DefectChangesetPanel((DefectChangeset) value);
		}
		else if (value instanceof JPanel) { // if the cell contains a JPanel, just display it
			panel = (JPanel)value;
		}
		else { // Otherwise just make an empty JPanel, and add a label with the result of value.toString()
			panel = new JPanel();
			panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0), BorderFactory.createLineBorder(Color.red, 1)));
			panel.add(new JLabel(value.toString()));
		}
		
		return panel;
	}

}

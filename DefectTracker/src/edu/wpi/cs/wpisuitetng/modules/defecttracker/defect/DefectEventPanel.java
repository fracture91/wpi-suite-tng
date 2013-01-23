package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectChangeset;

@SuppressWarnings("serial")
public class DefectEventPanel extends JPanel {
	
	protected JLabel title;
	protected JLabel content;
	
	public DefectEventPanel(DefectChangeset event) {
		
	}

	public DefectEventPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		title = new JLabel("Title of Defect Event");
		title.setFont(title.getFont().deriveFont(11));
		title.setFont(title.getFont().deriveFont(Font.BOLD));
		content = new JLabel("Content goes here.");
		content.setFont(content.getFont().deriveFont(10));
		this.add(title);
		this.add(content);
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createLineBorder(Color.black, 1)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	}
	
	protected void setTitle(String title) {
		this.title.setText(title);
	}
	
	protected void setContent(String content) {
		this.content.setText(content);
	}
}

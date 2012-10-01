package edu.wpi.cs.wpisuitetng.modules.defecttracker.views;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * This view is responsible for showing the form for creating a new defect. 
 */
@SuppressWarnings("serial")
public class CreateDefectView extends JPanel implements IRibbonButtonProvider {

	private JPanel buttonPanel;
	
	public CreateDefectView() {
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		JButton saveChanges = new JButton("Save Changes");
		buttonPanel.add(saveChanges);
		
		Box mainPanel = Box.createVerticalBox();
		JLabel title = new JLabel("Create Defect");
		title.setFont(title.getFont().deriveFont(title.getFont().getSize2D() + 12));
		title.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(title);
		
		JPanel fieldWrapper = new JPanel();
		fieldWrapper.setBackground(fieldWrapper.getBackground().darker());
		fieldWrapper.setBorder(BorderFactory.createLineBorder(fieldWrapper.getBackground().darker(), 1));
		fieldWrapper.setMaximumSize(new Dimension(800, 0));
		fieldWrapper.add(new JLabel("Title:"));
		fieldWrapper.add(new JTextField(50));
		// these fields should probably get generated magically from the model in the future
		mainPanel.add(fieldWrapper);
		
		mainPanel.add(Box.createVerticalGlue());
		
		JScrollPane scrollPane = new JScrollPane(mainPanel);
		setLayout(new GridLayout());
		add(scrollPane);
	}

	@Override
	public JPanel getButtonPanel() {
		return buttonPanel;
	}

}

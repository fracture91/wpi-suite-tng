package edu.wpi.cs.wpisuitetng.modules.defecttracker.views;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.janeway.controllers.network.MyRequestObserver;
import edu.wpi.cs.wpisuitetng.janeway.controllers.network.Request;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;

/**
 * This view is responsible for showing the form for creating a new defect. 
 */
@SuppressWarnings("serial")
public class CreateDefectView extends JPanel implements IRibbonButtonProvider, ActionListener {

	private JPanel buttonPanel;
	private JButton saveButton;
	private JTextField titleInput;
	
	public CreateDefectView() {
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		saveButton = new JButton("Save Changes");
		saveButton.addActionListener(this);
		buttonPanel.add(saveButton);
		
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
		titleInput = new JTextField(50);
		fieldWrapper.add(titleInput);
		// these fields should probably get generated magically from the custom fields
		// in the model at some point
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
	
	/**
	 * Create a Defect model based on the input in the fields and send it to the server.
	 */
	private void createAndSendDefect() {
		// this just prints to the console to confirm everything went through properly
		final MyRequestObserver requestObserver = new MyRequestObserver();
		Defect defect = new Defect(1, "This is a description", titleInput.getText(), "SomeUser");
		try {
			URL host = new URL("http://localhost:8080/");
			Request request = new Request(host, Request.RequestMethod.POST, defect.toJSON());
			request.addObserver(requestObserver);
			request.send();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == saveButton) {
			createAndSendDefect();
		}
	}

}

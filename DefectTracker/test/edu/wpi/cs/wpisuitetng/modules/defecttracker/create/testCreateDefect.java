package edu.wpi.cs.wpisuitetng.modules.defecttracker.create;

import java.awt.event.ActionListener;

import org.junit.Before;
import org.junit.Test;

public class testCreateDefect {
	
	protected CreateDefectView view;
	protected TestSaveDefectController controller;

	@Before
	public void setUp() throws Exception {
		view = new CreateDefectView();
		for (ActionListener al : view.saveButton.getActionListeners()) {
			view.saveButton.removeActionListener(al);
		}
		controller = new TestSaveDefectController((CreateDefectPanel)view.getMainPanel()); 
		view.saveButton.addActionListener(controller);
	}

	@Test
	public void test() {
		CreateDefectPanel cdPanel = (CreateDefectPanel) view.getMainPanel();
		cdPanel.txtTitle.setText("Test Defect 1");
		cdPanel.txtDescription.setText("This defect is for testing purposes");
		cdPanel.txtUser.setText("test_user");
		view.saveButton.doClick();
		
		System.out.println(controller.request.getRequestBody());
		
		System.out.println(((SaveRequestObserver)controller.observer).response.getBody());
	}

}

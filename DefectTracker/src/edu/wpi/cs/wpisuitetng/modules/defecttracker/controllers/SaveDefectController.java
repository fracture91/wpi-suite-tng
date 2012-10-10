package edu.wpi.cs.wpisuitetng.modules.defecttracker.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import edu.wpi.cs.wpisuitetng.janeway.controllers.network.MyRequestObserver;
import edu.wpi.cs.wpisuitetng.janeway.controllers.network.Request;
import edu.wpi.cs.wpisuitetng.janeway.controllers.network.Request.RequestMethod;
import edu.wpi.cs.wpisuitetng.janeway.models.Configuration;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.views.CreateDefectPanel;

/**
 * Controller to handle the saving of a defect
 *
 */
public class SaveDefectController implements ActionListener {

	/** The view object containing the request fields */
	protected CreateDefectPanel view;
	
	/**
	 * Construct a new handler for the given view
	 * @param view the view containing the request fields
	 */
	public SaveDefectController(CreateDefectPanel view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		final MyRequestObserver requestObserver = new MyRequestObserver();
		try {
			URL host = new URL(Configuration.getCoreURL());
			Request request = new Request(host);
			request.setRequestMethod(RequestMethod.POST);
			request.setRequestBody(view.getModel().toJSON());
			request.addObserver(requestObserver);
			request.send();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
}

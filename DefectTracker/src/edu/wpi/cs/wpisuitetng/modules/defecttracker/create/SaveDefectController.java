package edu.wpi.cs.wpisuitetng.modules.defecttracker.create;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.janeway.Configuration;
import edu.wpi.cs.wpisuitetng.janeway.network.Request;
import edu.wpi.cs.wpisuitetng.janeway.network.Request.RequestMethod;

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
		final SaveRequestObserver requestObserver = new SaveRequestObserver();
		Request request = new Request(Configuration.getInstance().getCoreURL());
		request.setRequestMethod(RequestMethod.POST);
		request.setRequestBody(view.getModel().toJSON());
		request.addObserver(requestObserver);
		request.send();
	}
}

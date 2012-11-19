package edu.wpi.cs.wpisuitetng.modules.defecttracker.create;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.Request.RequestMethod;

/**
 * Controller to handle the saving of a defect
 *
 */
public class SaveDefectController {

	/** The view object containing the request fields */
	protected DefectPanel view;
	
	/**
	 * Construct a new handler for the given view
	 * @param view the view containing the request fields
	 */
	public SaveDefectController(DefectPanel view) {
		this.view = view;
	}

	/**
	 * Save the view's Defect model to the server (asynchronous).
	 */
	public void save() {
		final SaveRequestObserver requestObserver = new SaveRequestObserver();
		Request request = new Request(ConfigManager.getConfig().getCoreUrl(), RequestMethod.POST);
		request.setRequestBody(view.getModel().toJSON());
		request.addObserver(requestObserver);
		request.send();
	}
	
}

package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.net.MalformedURLException;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.network.Network;
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
		// TODO Change PUT/POST depending on whether this is create or update
		Request request;
		try {
			request = Network.getInstance().makeRequest("defecttracker/defect", RequestMethod.PUT);
			request.setRequestBody(view.getFieldModel().toJSON());
			request.addObserver(requestObserver);
			request.send();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}

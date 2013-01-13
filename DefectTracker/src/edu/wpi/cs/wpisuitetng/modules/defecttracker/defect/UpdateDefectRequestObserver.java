package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import edu.wpi.cs.wpisuitetng.network.Observable;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.Response;

/**
 * A RequestObserver for a Request to update a Defect.
 */
public class UpdateDefectRequestObserver implements RequestObserver {

	private final DefectView view;

	/**
	 * Constructs a new UpdateDefectRequestObserver
	 * 
	 * @param view	The DefectView that will be affected by any updates.
	 */
	public UpdateDefectRequestObserver(DefectView view) {
		this.view = view;
	}

	@Override
	public void done(Observable observable) {
		// If observable is a Request...
		if (Request.class.getName().equals(observable.getClass().getName())) {
			// cast observable to a Request
			Request request = (Request) observable;

			// get the response from the request
			Response response = request.getResponse();

			// print the body
			System.out.println("Received response: " + response.getBody()); //TODO change this to logger

			// on success
			if (response.getBody().equals("success")) {
				((DefectPanel) view.getDefectPanel()).updateModel(((DefectPanel) view.getDefectPanel()).getEditedModel());
			}
			else {
				// TODO
			}
		}
		// Otherwise...
		else {
			System.out.println("Observable is not a Request."); // TODO change this to logger
		}
	}

	@Override
	public void error(Observable o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail(Observable o) {
		// TODO Auto-generated method stub

	}
}

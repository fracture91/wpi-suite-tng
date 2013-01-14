package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.util.Observable;
import java.util.Observer;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.Response;

/**
 * An Observer for a Request to update a Defect.
 */
public class UpdateDefectRequestObserver implements Observer {

	private final DefectView view;

	/**
	 * Constructs a new UpdateDefectRequestObserver
	 * 
	 * @param view	The DefectView that will be affected by any updates.
	 */
	public UpdateDefectRequestObserver(DefectView view) {
		this.view = view;
	}

	/**
	 * @see java.util.Observer#update
	 */
	@Override
	public void update(Observable observable, Object arg) {
		// If observable is a Request...
		if (Request.class.getName().equals(observable.getClass().getName())) {
			// cast observable to a Request
			Request request = (Request) observable;

			// get the response from the request
			Response response = request.getResponse();

			// print the body
			System.out.println("Received response: " + response.getBody()); //TODO change this to logger

			// parse the defect from the body
			Gson parser = new Gson();
			Defect defect = parser.fromJson(response.getBody(), Defect.class);

			// make sure the defect isn't null
			if (defect != null) {
				((DefectPanel) view.getDefectPanel()).updateModel(defect);
			}
			else {
				// TODO notify user of server error
			}
		}
		// Otherwise...
		else {
			System.out.println("Observable is not a Request."); // TODO change this to logger
		}
	}
}

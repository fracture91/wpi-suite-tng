package edu.wpi.cs.wpisuitetng.modules.defecttracker.search;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.Observable;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.Response;

/**
 * An observer for a request to retrieve all defects
 */
public class RetrieveAllDefectsRequestObserver implements RequestObserver {

	/** The controller managing the request */
	protected RetrieveAllDefectsController controller;
	
	/**
	 * Construct the observer
	 * @param controller
	 */
	public RetrieveAllDefectsRequestObserver(RetrieveAllDefectsController controller) {
		this.controller = controller;
	}

	@Override
	public void done(Observable observable) {
		if (Request.class.getName().equals(observable.getClass().getName())) {
			// cast observable to request
			Request request = (Request) observable;
			
			// get the response from the request
			Response response = request.getResponse();
			
			if (response.getResponseCode() == 200) {
				// parse the response				
				Gson parser = new Gson();
				Defect[] defects = parser.fromJson(response.getBody(), Defect[].class);
				
				// notify the controller
				controller.receivedData(defects);
			}
			else {
				
			}
			
		}
		else {
			// an error occurred
			controller.errorReceivingData();
		}
	}

	@Override
	public void error(Observable o) {
		// an error occurred
		controller.errorReceivingData();
	}

	@Override
	public void fail(Observable o) {
		// an error occurred
		controller.errorReceivingData();
	}
}

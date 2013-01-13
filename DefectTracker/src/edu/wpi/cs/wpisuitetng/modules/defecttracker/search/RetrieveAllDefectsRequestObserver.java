package edu.wpi.cs.wpisuitetng.modules.defecttracker.search;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.IRequest;
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
	public void success(IRequest iReq) {
		if (Request.class.getName().equals(iReq.getClass().getName())) {
			// cast observable to request
			Request request = (Request) iReq;

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
	public void error(IRequest iReq) {
		// an error occurred
		controller.errorReceivingData();
	}

	@Override
	public void fail(IRequest iReq, String errorMessage) {
		// an error occurred
		controller.errorReceivingData();
	}
}

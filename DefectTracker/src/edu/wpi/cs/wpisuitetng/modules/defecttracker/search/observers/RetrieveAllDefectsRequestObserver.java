package edu.wpi.cs.wpisuitetng.modules.defecttracker.search.observers;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.controllers.RetrieveAllDefectsController;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

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
	public void responseSuccess(IRequest iReq) {
		// cast observable to request
		Request request = (Request) iReq;

		// get the response from the request
		ResponseModel response = request.getResponse();

		if (response.getStatusCode() == 200) {
			// parse the response				
			Defect[] defects = Defect.fromJSONArray(response.getBody());

			// notify the controller
			controller.receivedData(defects);
		}
		else {

		}
	}

	@Override
	public void responseError(IRequest iReq) {
		// an error occurred
		controller.errorReceivingData();
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		// an error occurred
		controller.errorReceivingData();
	}
}

package edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.IRequest;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.Response;

/**
 * Observer to respond when a lookup defect response is received
 */
public class LookupRequestObserver implements RequestObserver {

	/** The lookup defect controller */
	protected LookupDefectController controller;

	/**
	 * Construct the observer
	 * @param controller the lookup defect controller
	 */
	public LookupRequestObserver(LookupDefectController controller) {
		this.controller = controller;
	}

	@Override
	public void responseReceived(IRequest observable) {
		// If observable is a Request...
		if (Request.class.getName().equals(observable.getClass().getName())) {
			// cast observable to a Request
			Request request = (Request) observable;

			// get the response from the request
			Response response = request.getResponse();

			// check the response code of the request
			if (response.getResponseCode() != 200) {
				controller.requestFailed();
				return;
			}

			// parse the list of defects received from the core
			Gson parser = new Gson();
			Defect[] defects = parser.fromJson(response.getBody(), Defect[].class);

			// make sure that there is actually a defect in the body			
			if (defects.length > 0 && defects[0] != null) {
				controller.receivedResponse(defects[0]);
			}
			else {
				controller.requestFailed();
			}
		}
		// Otherwise...
		else {
			System.out.println("Observable is not a Request.");
		}
	}

	@Override
	public void responseError(IRequest o) {

		controller.requestFailed();
	}

	@Override
	public void requestFail(IRequest o) {

		controller.requestFailed();
	}

	@Override
	public void before(IRequest o) {
		// TODO Auto-generated method stub

	}
}

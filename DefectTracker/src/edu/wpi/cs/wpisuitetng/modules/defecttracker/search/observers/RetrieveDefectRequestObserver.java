package edu.wpi.cs.wpisuitetng.modules.defecttracker.search.observers;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.search.controllers.RetrieveDefectController;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * An observer for a request to retrieve a defect with the provided id
 */
public class RetrieveDefectRequestObserver implements RequestObserver {

	/** The retrieve defect controller using this observer */
	protected RetrieveDefectController controller;

	/**
	 * Construct a new observer
	 * @param controller the controller managing the request
	 */
	public RetrieveDefectRequestObserver(RetrieveDefectController controller) {
		this.controller = controller;
	}

	@Override
	public void responseSuccess(IRequest iReq) {
		// cast observable to a Request
		Request request = (Request) iReq;

		// get the response from the request
		ResponseModel response = request.getResponse();

		// check the response code of the request
		if (response.getStatusCode() != 200) {
			controller.errorRetrievingDefect();
			return;
		}

		// parse the defect received from the core
		Defect[] defects = Defect.fromJSONArray(response.getBody());
		if (defects.length > 0 && defects[0] != null) {
			controller.showDefect(defects[0]);
		}
		else {
			controller.errorRetrievingDefect();
		}
	}

	@Override
	public void responseError(IRequest iReq) {
		controller.errorRetrievingDefect();
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		// TODO deal with exception
		controller.errorRetrievingDefect();
	}
}

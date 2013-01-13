package edu.wpi.cs.wpisuitetng.modules.defecttracker.search;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.IRequest;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.Response;

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
	public void success(IRequest iReq) {
		if (Request.class.getName().equals(iReq.getClass().getName())) {
			// cast observable to a Request
			Request request = (Request) iReq;

			// get the response from the request
			Response response = request.getResponse();

			// check the response code of the request
			if (response.getResponseCode() != 200) {
				controller.errorRetrievingDefect();
				return;
			}

			// parse the defect received from the core
			Gson parser = new Gson();
			Defect[] defects = parser.fromJson(response.getBody(), Defect[].class);
			if (defects.length > 0 && defects[0] != null) {
				controller.showDefect(defects[0]);
			}
			else {
				controller.errorRetrievingDefect();
			}
		}
		else {
			// TODO deal with this error
		}
	}

	@Override
	public void error(IRequest iReq) {
		controller.errorRetrievingDefect();
	}

	@Override
	public void fail(IRequest iReq, String errorMessage) {
		controller.errorRetrievingDefect();
	}
}

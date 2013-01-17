package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

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
	public void responseSuccess(IRequest iReq) {
		// cast observable to a Request
		Request request = (Request) iReq;

		// get the response from the request
		ResponseModel response = request.getResponse();

		// print the body
		System.out.println("Received response: " + response.getBody()); //TODO change this to logger

		// parse the defect from the body
		Gson parser = new Gson();
		Defect defect = parser.fromJson(response.getBody(), Defect.class);

		// make sure the defect isn't null
		if (defect != null) {
			((DefectPanel) view.getDefectPanel()).updateModel(defect);
			view.setEditModeDescriptors(defect);
		}
		else {
			// TODO notify user of server error
		}
	}

	@Override
	public void responseError(IRequest iReq) {
		// TODO Auto-generated method stub
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		// TODO Auto-generated method stub
	}
}

package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import javax.swing.JOptionPane;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * An Observer for a Request to create a Defect.
 */
public class CreateDefectRequestObserver implements RequestObserver {

	private final DefectView view;

	/**
	 * Constructs a new CreateDefectRequestObserver
	 * 
	 * @param view	The DefectView that will be affected by any updates.
	 */
	public CreateDefectRequestObserver(DefectView view) {
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
		Defect defect = Defect.fromJSON(response.getBody());

		// make sure the defect isn't null
		if (defect != null) {
			((DefectPanel) view.getDefectPanel()).updateModel(defect);
			view.setEditModeDescriptors(defect);
		}
		else {
			JOptionPane.showMessageDialog(null, "Unable to parse defect received from server.");
		}
		
		always();
	}

	@Override
	public void responseError(IRequest iReq) {
		JOptionPane.showMessageDialog(null, "Received " + iReq.getResponse().getStatusCode() + " error from server: " + iReq.getResponse().getStatusMessage());
		always();
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		JOptionPane.showMessageDialog(null, "Unable to complete request: " + exception.getMessage());
		always();
	}
	
	/**
	 * Should always be run when an update method is called.
	 */
	private void always() {
		view.setInputEnabled(true);
	}
}

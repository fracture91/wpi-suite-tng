package edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar;

import java.util.Observable;
import java.util.Observer;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.Response;

public class LookupRequestObserver implements Observer {

	protected LookupDefectController controller;
	
	public LookupRequestObserver(LookupDefectController controller) {
		this.controller = controller;
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

			// parse the list of defects received from the core
			Gson parser = new Gson();
			Defect[] defects = parser.fromJson(response.getBody(), Defect[].class);
			if (defects.length > 0) {
				controller.receivedResponse(defects[0]);
			}
		}
		// Otherwise...
		else {
			System.out.println("Observable is not a Request.");
		}
	}
}

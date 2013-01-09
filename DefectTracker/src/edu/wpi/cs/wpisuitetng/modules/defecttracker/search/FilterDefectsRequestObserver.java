package edu.wpi.cs.wpisuitetng.modules.defecttracker.search;

import java.util.Observable;
import java.util.Observer;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.Response;

public class FilterDefectsRequestObserver implements Observer {

	protected FilterDefectsController controller;
	
	public FilterDefectsRequestObserver(FilterDefectsController controller) {
		this.controller = controller;
	}

	@Override
	public void update(Observable observable, Object arg) {
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
				// an error occurred
				controller.errorReceivingData();
			}
			
		}
		else {
			// an error occurred
			controller.errorReceivingData();
		}
	}
}

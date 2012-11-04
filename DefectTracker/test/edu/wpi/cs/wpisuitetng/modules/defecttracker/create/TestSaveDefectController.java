package edu.wpi.cs.wpisuitetng.modules.defecttracker.create;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import edu.wpi.cs.wpisuitetng.janeway.Configuration;
import edu.wpi.cs.wpisuitetng.janeway.network.Request;
import edu.wpi.cs.wpisuitetng.janeway.network.Request.RequestMethod;
import edu.wpi.cs.wpisuitetng.janeway.network.Response;

public class TestSaveDefectController extends SaveDefectController {
	
	protected SaveRequestObserver observer;
	protected Request request;

	public TestSaveDefectController(CreateDefectPanel view) {
		super(view);
		// TODO Auto-generated constructor stub
	}

	public void actionPerformed(ActionEvent e) {
		request = new Request(Configuration.getInstance().getCoreURL()) {
			public void send() throws IllegalStateException {
				running = false;
				this.setResponse(new Response(200, "Success", new HashMap<String, List<String>>(), "Response body"));
			}
		};
		
		request.setRequestMethod(RequestMethod.POST);
		request.setRequestBody(view.getModel().toJSON());
		observer = new SaveRequestObserver() {
			public void update(Observable observable, Object arg) {
				Request request = (Request) observable;
				response = request.getResponse();
			}
		};
		request.addObserver(observer);
		request.send();
	}
}

package edu.wpi.cs.wpisuitetng.modules.postboard.controller;

import edu.wpi.cs.wpisuitetng.modules.postboard.model.PostBoardMessage;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * This observer handles responses to requests for all
 * post board messages.
 * 
 * @author Chris Casola
 *
 */
public class GetMessagesRequestObserver implements RequestObserver {
	
	public GetMessagesController controller;
	
	public GetMessagesRequestObserver(GetMessagesController controller) {
		this.controller = controller;
	}

	/*
	 * Parse the messages out of the response body and pass them to the controller
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		PostBoardMessage[] messages = PostBoardMessage.fromJsonArray(iReq.getResponse().getBody());
		controller.receivedMessages(messages);
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		fail(iReq, null);
	}

	/*
	 * Put an error message in the PostBoardPanel if the request fails.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		PostBoardMessage[] errorMessage = {new PostBoardMessage("Error retrieving messages.")};
		controller.receivedMessages(errorMessage);
	}

}

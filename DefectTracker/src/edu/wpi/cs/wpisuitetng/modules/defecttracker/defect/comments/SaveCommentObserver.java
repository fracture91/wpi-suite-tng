package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.comments;

import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * This observer handles responses from requests to save a comment
 */
public class SaveCommentObserver implements RequestObserver {

	private final SaveCommentController controller;
	
	public SaveCommentObserver(SaveCommentController controller) {
		this.controller = controller;
	}
	
	/*
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		controller.success(iReq.getResponse());
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		controller.failure(iReq.getResponse());
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		controller.failure(iReq.getResponse());
	}

}

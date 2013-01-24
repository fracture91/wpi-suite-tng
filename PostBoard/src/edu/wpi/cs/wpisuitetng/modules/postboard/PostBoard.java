package edu.wpi.cs.wpisuitetng.modules.postboard;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

public class PostBoard implements IJanewayModule {
	
	List<JanewayTabModel> tabs;
	
	public PostBoard() {

		tabs = new ArrayList<JanewayTabModel>();		
	}

	@Override
	public String getName() {
		return "PostBoard";
	}

	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}

}

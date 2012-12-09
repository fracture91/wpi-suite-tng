package edu.wpi.cs.wpisuitetng.modules.defecttracker.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.JPlaceholderTextField;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectView;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.Request.RequestMethod;

public class LookupDefectController implements ActionListener {

	protected MainTabController tabController;
	
	protected JPlaceholderTextField searchField;
	
	protected boolean waitingForResponse = false;
	
	public LookupDefectController(MainTabController tabController, JPlaceholderTextField searchField) {
		this.tabController = tabController;
		this.searchField = searchField;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JTextField source = (JTextField) e.getSource();
		if (waitingForResponse == false) {
			waitingForResponse = true;
			
			Request request;
			try {
				request = Network.getInstance().makeRequest("defecttracker/defect/" + source.getText(), RequestMethod.GET);
				request.addObserver(new LookupRequestObserver(this));
				request.send();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NullPointerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void receivedResponse(Defect defect) {
		DefectView defectView = new DefectView(defect);
		tabController.addTab("Defect #" + defect.getId(), new ImageIcon(), defectView, "View defect " + defect.getTitle());
		defectView.requestFocus();
		searchField.clearText();
		waitingForResponse = false;
	}
	
	public boolean getWaitingFlag() {
		return waitingForResponse;
	}
}

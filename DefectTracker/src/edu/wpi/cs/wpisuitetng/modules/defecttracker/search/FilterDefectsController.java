package edu.wpi.cs.wpisuitetng.modules.defecttracker.search;

import java.net.MalformedURLException;
import java.util.Observer;

import javax.swing.JOptionPane;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.Request.RequestMethod;

public class FilterDefectsController {

	protected SearchDefectsView view;
	
	protected Defect[] data = null;
	
	public FilterDefectsController(SearchDefectsView view) {
		this.view = view;
	}
	
	public void refreshData() {		
		final Observer requestObserver = new FilterDefectsRequestObserver(this);
		Request request;
		try {
			request = Network.getInstance().makeRequest("defecttracker/defect", RequestMethod.GET);
			request.addObserver(requestObserver);
			request.send();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receivedData(Defect[] defects) {	
		if (defects.length > 0) {
			// save the data
			this.data = defects;
			
			// set the column names
			String[] columnNames = {"ID", "Title", "Description", "Creator", "Created", "Last Modified"};
			view.getSearchPanel().getResultsPanel().getModel().setColumnNames(columnNames);
			
			// put the data in the table
			Object[][] entries = new Object[defects.length][6];
			for (int i = 0; i < defects.length; i++) {
				entries[i][0] = String.valueOf(defects[i].getId());
				entries[i][1] = defects[i].getTitle();
				entries[i][2] = defects[i].getDescription();
				entries[i][3] = defects[i].getCreator().getName();
				entries[i][4] = defects[i].getCreationDate();
				entries[i][5] = defects[i].getLastModifiedDate();
			}
			view.getSearchPanel().getResultsPanel().getModel().setData(entries);
			view.getSearchPanel().getResultsPanel().getModel().fireTableStructureChanged();
		}
		else {
			// do nothing, there are no defects
		}
	}
	
	public void errorReceivingData() {
		JOptionPane.showMessageDialog(view, "An error occurred retrieving defects from the server.", "Error Communicating with Server", JOptionPane.ERROR_MESSAGE);
	}
}

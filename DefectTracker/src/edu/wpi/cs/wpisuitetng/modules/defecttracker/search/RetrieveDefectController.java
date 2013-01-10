package edu.wpi.cs.wpisuitetng.modules.defecttracker.search;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectView;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.Request.RequestMethod;

/**
 * Controller to handle retrieving one defect from the server
 */
public class RetrieveDefectController extends MouseAdapter {

	/** The results panel */
	protected ResultsPanel view;

	/**
	 * Construct the controller
	 * 
	 * @param view the parent view 
	 */
	public RetrieveDefectController(ResultsPanel view) {
		this.view = view;
	}

	/**
	 * @see java.awt.event.MouseAdapter#mouseClicked(MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent me) {
		if (me.getClickCount() == 2) { /* only respond to double clicks */
			
			// Get the defect ID in the row that was clicked
			JTable resultsTable = (JTable) me.getSource();
			int row = resultsTable.getSelectedRow();
			String defectId = (String) resultsTable.getValueAt(row, 0);

			// Create and send a request for the defect with the given ID
			Request request;
			try {
				request = Network.getInstance().makeRequest("defecttracker/defect/" + defectId, RequestMethod.GET);
				request.addObserver(new RetrieveDefectRequestObserver(this));
				request.send();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Called by {@link RetrieveDefectRequestObserver} when the response
	 * is received from the server.
	 * @param defect the defect that was retrieved
	 */
	public void showDefect(Defect defect) {
		// Make a new defect view to display the defect that was received
		DefectView defectView = new DefectView(defect, Mode.EDIT);
		view.getTabController().addTab("Defect #" + defect.getId(), new ImageIcon(), defectView, "View defect " + defect.getTitle());
		defectView.requestFocus();
	}
	
	/**
	 * Called by {@link RetrieveDefectRequestObserver} when an error
	 * occurred retrieving the defect from the server.
	 */
	public void errorRetrievingDefect() {
		JOptionPane.showMessageDialog(view, "An error occurred opening the defect you selected.", "Error opening defect", JOptionPane.ERROR_MESSAGE);
	}
}

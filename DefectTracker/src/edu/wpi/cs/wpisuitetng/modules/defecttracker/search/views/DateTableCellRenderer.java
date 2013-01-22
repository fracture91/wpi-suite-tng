package edu.wpi.cs.wpisuitetng.modules.defecttracker.search.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * Custom cell renderer for JTables. Displays dates in the
 * following format: MM/dd/yy hh:mm am/pm
 *
 */
@SuppressWarnings("serial")
public class DateTableCellRenderer extends DefaultTableCellRenderer {
	
	/** The date formatter */
	protected SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy hh:mm a");

	/**
	 * @see java.swing.table.DefaultTableCellRenderer#setValue
	 */
	@Override
	public void setValue(Object value) {
		if (value instanceof Date) {			
			setText(formatter.format((Date) value));
		}
	}
}

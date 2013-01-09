package edu.wpi.cs.wpisuitetng.modules.defecttracker.search;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class DateTableCellRenderer extends DefaultTableCellRenderer {
	
	protected SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy hh:mm a");

	public void setValue(Object value) {
		if (value instanceof Date) {			
			setText(formatter.format((Date) value));
		}
	}
}

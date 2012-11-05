package edu.wpi.cs.wpisuitetng.modules.defecttracker.tabs;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * This provides a tab component with a close button to the left of the title.
 */
@SuppressWarnings("serial")
public class ClosableTabComponent extends JPanel implements ActionListener {
	
	private final JTabbedPane tabbedPane;
	
	/**
	 * Create a closable tab component belonging to the given tabbedPane.
	 * The title is extracted with {@link JTabbedPane#getTitleAt(int)}.
	 * @param tabbedPane  The JTabbedPane this tab component belongs to
	 */
	public ClosableTabComponent(JTabbedPane tabbedPane) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.tabbedPane = tabbedPane;
		setOpaque(false);
		
		final JButton closeButton = new JButton("X");
		closeButton.setFont(closeButton.getFont().deriveFont((float) 8));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.addActionListener(this);
		add(closeButton);
		
		final JLabel label = new JLabel() {
			// display the title according to what's set on our JTabbedPane
			@Override
			public String getText() {
				final JTabbedPane tabbedPane = ClosableTabComponent.this.tabbedPane;
				final int index = tabbedPane.indexOfTabComponent(ClosableTabComponent.this);
				return index > -1 ? tabbedPane.getTitleAt(index) : "";
			}
		};
		label.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
		add(label);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// close this tab when close button is clicked
		final int index = tabbedPane.indexOfTabComponent(this);
		if(index > -1) {
			tabbedPane.remove(index);
		}
	}
	
}

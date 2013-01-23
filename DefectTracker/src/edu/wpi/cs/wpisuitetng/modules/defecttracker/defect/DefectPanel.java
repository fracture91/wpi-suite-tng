package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectStatus;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Tag;

/**
 * Panel to display the fields of a Defect and allow editing
 */
@SuppressWarnings("serial")
public class DefectPanel extends JPanel {
	public enum Mode {
		CREATE,
		EDIT;
	}

	protected DefectView parent;
	
	protected Defect model;

	protected JTextField txtTitle;
	protected JTextArea txtDescription;
	protected JComboBox cmbStatus;
	protected JLabel txtCreatedDate;
	protected JLabel txtModifiedDate;
	protected JTextField txtCreator;
	protected JTextField txtAssignee;
	protected TagPanel tagPanel;
	protected DefectEventView defectEventView;
	
	protected JLabel lblCreatedDate;
	protected JLabel lblModifiedDate;

	protected final TextUpdateListener txtTitleListener;
	protected final TextUpdateListener txtDescriptionListener;
	protected final ComboUpdateListener cmbStatusListener;
	protected final TextUpdateListener txtCreatorListener;
	protected final TextUpdateListener txtAssigneeListener;

	protected boolean inputEnabled;
	protected Mode editMode;

	protected static final int HORIZONTAL_PADDING = 5;
	protected static final int VERTICAL_PADDING = 15;
	protected static final int LABEL_ALIGNMENT = JLabel.TRAILING;

	/**
	 * Constructs a DefectPanel for creating or editing a given Defect.
	 * 
	 * @param parent	The parent DefectView.
	 * @param defect	The Defect to edit.
	 * @param mode		Whether or not the given Defect should be treated as if it already exists 
	 * 					on the server ({@link Mode#EDIT}) or not ({@link Mode#CREATE}).
	 */
	public DefectPanel(DefectView parent, Defect defect, Mode mode) {
		this.parent = parent;
		editMode = mode;
		
		// Indicate that input is enabled
		inputEnabled = true;

		this.model = defect;

		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);

		addComponents(layout);

		// Add TextUpdateListeners. These check if the text component's text differs from the panel's Defect 
		// model and highlights them accordingly every time a key is pressed.
		txtTitleListener = new TextUpdateListener(this, txtTitle);
		txtTitle.addKeyListener(txtTitleListener);

		txtDescriptionListener = new TextUpdateListener(this, txtDescription);
		txtDescription.addKeyListener(txtDescriptionListener);
		
		cmbStatusListener = new ComboUpdateListener(this, cmbStatus);
		cmbStatus.addItemListener(cmbStatusListener);

		txtCreatorListener = new TextUpdateListener(this, txtCreator);
		txtCreator.addKeyListener(txtCreatorListener);

		txtAssigneeListener = new TextUpdateListener(this, txtAssignee);
		txtAssignee.addKeyListener(txtAssigneeListener);
		
		// prevent tab key from inserting tab characters into the description field
		txtDescription.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						txtDescription.transferFocus();
					}
					else {
						txtDescription.transferFocusBackward();
					}
					event.consume();
				}
			}
		});
		
		// Populate the form with the contents of the Defect model and update the TextUpdateListeners.
		updateFields();
	}

	/**
	 * Adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 * @param layout the layout manager
	 */
	protected void addComponents(SpringLayout layout) {
		txtTitle = new JTextField(50);
		txtDescription = new JTextArea();
		txtDescription.setLineWrap(true);
		txtDescription.setWrapStyleWord(true);
		txtDescription.setBorder(txtTitle.getBorder());
		String[] defectStatusValues = new String[DefectStatus.values().length];
		for (int i = 0; i < DefectStatus.values().length; i++) {
			defectStatusValues[i] = DefectStatus.values()[i].toString();
		}
		cmbStatus = new JComboBox(defectStatusValues);
		txtCreatedDate = new JLabel();
		txtModifiedDate = new JLabel("");
		txtCreator = new JTextField(20);
		txtCreator.setEnabled(false);
		txtAssignee = new JTextField(20);
		tagPanel = new TagPanel(model);
		defectEventView = new DefectEventView(model);
		
		if (editMode == Mode.CREATE) {
			cmbStatus.setEnabled(false);
		}

		// Set text component names. These names correspond to method names in the Defect model (ex: "Title" => Defect#getTitle()).
		// These are required for TextUpdateListener to be able to get the correct field from panel's Defect model.
		txtTitle.setName("Title");
		txtDescription.setName("Description");
		cmbStatus.setName("Status");
		txtCreator.setName("Creator");
		txtAssignee.setName("Assignee");
		
		// set maximum widths of components so they are not stretched
		txtTitle.setMaximumSize(txtTitle.getPreferredSize());
		cmbStatus.setMaximumSize(cmbStatus.getPreferredSize());
		txtCreator.setMaximumSize(txtCreator.getPreferredSize());
		txtAssignee.setMaximumSize(txtAssignee.getPreferredSize());
		tagPanel.setMaximumSize(tagPanel.getPreferredSize());

		JLabel lblTitle = new JLabel("Title:", LABEL_ALIGNMENT);
		JLabel lblDescription = new JLabel("Description:", LABEL_ALIGNMENT);
		JLabel lblStatus = new JLabel("Status:", LABEL_ALIGNMENT);
		lblCreatedDate = new JLabel("", LABEL_ALIGNMENT);
		lblModifiedDate = new JLabel("", LABEL_ALIGNMENT);
		JLabel lblCreator = new JLabel("Creator:", LABEL_ALIGNMENT);
		JLabel lblAssignee = new JLabel("Assignee:", LABEL_ALIGNMENT);

		int labelWidth = lblDescription.getPreferredSize().width;

		layout.putConstraint(SpringLayout.NORTH, lblTitle, VERTICAL_PADDING, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, lblTitle, 15, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, lblTitle, labelWidth, SpringLayout.WEST, lblTitle);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, txtTitle, 0, SpringLayout.VERTICAL_CENTER, lblTitle);
		layout.putConstraint(SpringLayout.WEST, txtTitle, HORIZONTAL_PADDING, SpringLayout.EAST, lblTitle);
		layout.putConstraint(SpringLayout.EAST, txtTitle, txtTitle.getPreferredSize().width, SpringLayout.WEST, txtTitle);

		layout.putConstraint(SpringLayout.NORTH, txtDescription, VERTICAL_PADDING, SpringLayout.SOUTH, txtTitle);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblDescription, 0, SpringLayout.VERTICAL_CENTER, txtDescription);
		layout.putConstraint(SpringLayout.WEST, lblDescription, 0, SpringLayout.WEST, lblTitle);
		layout.putConstraint(SpringLayout.EAST, lblDescription, labelWidth, SpringLayout.WEST, lblDescription);
		layout.putConstraint(SpringLayout.WEST, txtDescription, HORIZONTAL_PADDING, SpringLayout.EAST, lblDescription);
		layout.putConstraint(SpringLayout.EAST, txtDescription, 0, SpringLayout.EAST, txtTitle);
		layout.putConstraint(SpringLayout.SOUTH, txtDescription, txtTitle.getPreferredSize().height * 4, SpringLayout.NORTH, txtDescription);

		layout.putConstraint(SpringLayout.NORTH, cmbStatus, VERTICAL_PADDING, SpringLayout.SOUTH, txtDescription);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblStatus, 0, SpringLayout.VERTICAL_CENTER, cmbStatus);
		layout.putConstraint(SpringLayout.WEST, lblStatus, 0, SpringLayout.WEST, lblTitle);
		layout.putConstraint(SpringLayout.EAST, lblStatus, labelWidth, SpringLayout.WEST, lblStatus);
		layout.putConstraint(SpringLayout.WEST, cmbStatus, HORIZONTAL_PADDING, SpringLayout.EAST, lblStatus);
		
		layout.putConstraint(SpringLayout.NORTH, txtCreatedDate, VERTICAL_PADDING, SpringLayout.SOUTH, cmbStatus);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblCreatedDate, 0, SpringLayout.VERTICAL_CENTER, txtCreatedDate);
		layout.putConstraint(SpringLayout.WEST, lblCreatedDate, 0, SpringLayout.WEST, lblTitle);
		layout.putConstraint(SpringLayout.EAST, lblCreatedDate, labelWidth, SpringLayout.WEST, lblCreatedDate);
		layout.putConstraint(SpringLayout.WEST, txtCreatedDate, HORIZONTAL_PADDING, SpringLayout.EAST, lblCreatedDate);
		
		layout.putConstraint(SpringLayout.NORTH, txtModifiedDate, VERTICAL_PADDING, SpringLayout.SOUTH, txtCreatedDate);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblModifiedDate, 0, SpringLayout.VERTICAL_CENTER, txtModifiedDate);
		layout.putConstraint(SpringLayout.WEST, lblModifiedDate, 0, SpringLayout.WEST, lblTitle);
		layout.putConstraint(SpringLayout.EAST, lblModifiedDate, labelWidth, SpringLayout.WEST, lblModifiedDate);
		layout.putConstraint(SpringLayout.WEST, txtModifiedDate, HORIZONTAL_PADDING, SpringLayout.EAST, lblModifiedDate);
		
		layout.putConstraint(SpringLayout.NORTH, txtCreator, VERTICAL_PADDING, SpringLayout.SOUTH, txtModifiedDate);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblCreator, 0, SpringLayout.VERTICAL_CENTER, txtCreator);
		layout.putConstraint(SpringLayout.WEST, lblCreator, 0, SpringLayout.WEST, lblTitle);
		layout.putConstraint(SpringLayout.EAST, lblCreator, labelWidth, SpringLayout.WEST, lblCreator);
		layout.putConstraint(SpringLayout.WEST, txtCreator, HORIZONTAL_PADDING, SpringLayout.EAST, lblCreator);

		layout.putConstraint(SpringLayout.NORTH, txtAssignee, VERTICAL_PADDING, SpringLayout.SOUTH, txtCreator);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, lblAssignee, 0, SpringLayout.VERTICAL_CENTER, txtAssignee);
		layout.putConstraint(SpringLayout.WEST, lblAssignee, 0, SpringLayout.WEST, lblTitle);
		layout.putConstraint(SpringLayout.EAST, lblAssignee, labelWidth, SpringLayout.WEST, lblAssignee);
		layout.putConstraint(SpringLayout.WEST, txtAssignee, HORIZONTAL_PADDING, SpringLayout.EAST, lblAssignee);

		layout.putConstraint(SpringLayout.NORTH, tagPanel, VERTICAL_PADDING * 3, SpringLayout.NORTH, txtAssignee);
		layout.putConstraint(SpringLayout.WEST, tagPanel, 0, SpringLayout.WEST, lblTitle);
		layout.putConstraint(SpringLayout.EAST, tagPanel, 0, SpringLayout.EAST, txtTitle);
		
		layout.putConstraint(SpringLayout.NORTH, defectEventView, VERTICAL_PADDING, SpringLayout.SOUTH, tagPanel);
		layout.putConstraint(SpringLayout.WEST, defectEventView, 0, SpringLayout.WEST, tagPanel);
		layout.putConstraint(SpringLayout.EAST, defectEventView, 0, SpringLayout.EAST, tagPanel);
		layout.putConstraint(SpringLayout.SOUTH, this, VERTICAL_PADDING, SpringLayout.SOUTH, defectEventView);

		SpringLayout.Constraints defectPanelConstraint = layout.getConstraints(this);
		defectPanelConstraint.setHeight(Spring.sum(Spring.constant(defectEventView.getHeight()), defectPanelConstraint.getConstraint(SpringLayout.SOUTH)));
		

		add(lblTitle);
		add(txtTitle);
		add(lblDescription);
		add(txtDescription);
		add(lblStatus);
		add(cmbStatus);
		add(lblCreatedDate);
		add(txtCreatedDate);
		add(lblModifiedDate);
		add(txtModifiedDate);
		add(lblCreator);
		add(txtCreator);
		add(lblAssignee);
		add(txtAssignee);
		add(tagPanel);
		add(defectEventView);
	}

	/**
	 * Sets whether input is enabled for this panel and its children. This should be used instead of 
	 * JComponent#setEnabled because setEnabled does not affect its children.
	 * 
	 * @param enabled	Whether or not input is enabled.
	 */
	protected void setInputEnabled(boolean enabled) {
		inputEnabled = enabled;

		txtTitle.setEnabled(enabled);
		txtDescription.setEnabled(enabled);
		cmbStatus.setEnabled(enabled);
		txtAssignee.setEnabled(enabled);
		tagPanel.setInputEnabled(enabled);
	}
	
	/**
	 * Updates the DefectPanel's model to contain the values of the given Defect and sets the 
	 * DefectPanel's editMode to {@link Mode#EDIT}.
	 * 
	 * @param defect	The Defect which contains the new values for the model.
	 */
	protected void updateModel(Defect defect) {
		updateModel(defect, Mode.EDIT);
	}

	/**
	 * Updates the DefectPanel's model to contain the values of the given Defect.
	 * 
	 * @param	defect	The Defect which contains the new values for the model.
	 * @param	mode	The new editMode.
	 */
	protected void updateModel(Defect defect, Mode mode) {
		editMode = mode;
		
		model.setId(defect.getId());
		model.setTitle(defect.getTitle());
		model.setDescription(defect.getDescription());
		model.setAssignee(defect.getAssignee());
		model.setCreator(defect.getCreator());
		model.setCreationDate(defect.getCreationDate());
		model.setLastModifiedDate(defect.getLastModifiedDate());
		model.setStatus(defect.getStatus());
		tagPanel.updateModel(defect);
		
		updateFields();
	}

	/**
	 * Updates the DefectPanel's fields to match those in the current model.
	 */
	private void updateFields() {
		txtTitle.setText(model.getTitle());
		txtDescription.setText(model.getDescription());
		for (int i = 0; i < cmbStatus.getItemCount(); i++) {
			if (model.getStatus() == DefectStatus.valueOf((String) cmbStatus.getItemAt(i))) {
				cmbStatus.setSelectedIndex(i);
			}
		}
		if (editMode == Mode.EDIT) {
			lblCreatedDate.setText("Created:");
			txtCreatedDate.setText(model.getCreationDate().toString());
		}
		if (editMode == Mode.EDIT) {
			lblModifiedDate.setText("Modified:");
			txtModifiedDate.setText(model.getLastModifiedDate().toString());
		}
		if (model.getCreator() != null) {
			txtCreator.setText(model.getCreator().getUsername());
		}
		if (model.getAssignee() != null) {
			txtAssignee.setText(model.getAssignee().getUsername());
		}
		
		txtTitleListener.checkIfUpdated();
		txtDescriptionListener.checkIfUpdated();
		cmbStatusListener.checkIfUpdated();
		txtCreatorListener.checkIfUpdated();
		txtAssigneeListener.checkIfUpdated();
	}

	/**
	 * Returns a boolean representing whether or not input is enabled for the DefectPanel and its children.
	 * 
	 * @return	A boolean representing whether or not input is enabled for the DefectPanel and its children.
	 */
	public boolean getInputEnabled() {
		return inputEnabled;
	}

	/**
	 * Gets the DefectPanel's internal model.
	 * @return
	 */
	public Defect getModel() {
		return model;
	}
	
	/**
	 * Returns the parent DefectView.
	 * 
	 * @return the parent DefectView.
	 */
	public DefectView getParent() {
		return parent;
	}

	/**
	 * Returns the edit {@link Mode} for this DefectPanel.
	 * 
	 * @return	The edit {@link Mode} for this DefectPanel.
	 */
	public Mode getEditMode() {
		return editMode;
	}

	/**
	 * Returns the model object represented by this view's fields.
	 * 
	 * TODO: Do some basic input verification
	 * @return the model represented by this view
	 */
	public Defect getEditedModel() {
		Defect defect = new Defect();
		defect.setId(model.getId());
		defect.setTitle(txtTitle.getText());
		defect.setDescription(txtDescription.getText());
		defect.setStatus(DefectStatus.valueOf((String) cmbStatus.getSelectedItem()));
		if (!(txtAssignee.getText().equals(""))) {
			defect.setAssignee(new User("", txtAssignee.getText(), "", -1));
		}
		if (!(txtCreator.getText().equals(""))) {
			defect.setCreator(new User("", txtCreator.getText(), "", -1));
		}
		HashSet<Tag> tags = new HashSet<Tag>();
		for (int i = 0; i < tagPanel.lmTags.getSize(); i++) {
			tags.add(new Tag((String)tagPanel.lmTags.get(i)));
		}
		defect.setTags(tags);
		
		return defect;
	}

	/**
	 * Returns the creator text field
	 * @return the creator text field
	 */
	public JTextField getCreatorField() {
		return txtCreator;
	}
}

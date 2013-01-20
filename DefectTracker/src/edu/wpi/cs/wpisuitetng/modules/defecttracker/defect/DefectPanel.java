package edu.wpi.cs.wpisuitetng.modules.defecttracker.defect;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
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

	protected Defect model;

	protected JTextField txtTitle;
	protected JTextArea txtDescription;
	protected JTextField txtCreator;
	protected JTextField txtAssignee;
	protected TagPanel tagPanel;
	protected DefectEventView defectEventView;

	protected final TextUpdateListener txtTitleListener;
	protected final TextUpdateListener txtDescriptionListener;
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
	 * @param defect	The Defect to edit.
	 * @param mode		Whether or not the given Defect should be treated as if it already exists 
	 * 					on the server ({@link Mode#EDIT}) or not ({@link Mode#CREATE}).
	 */
	public DefectPanel(Defect defect, Mode mode) {
		editMode = mode;

		// Indicate that input is enabled
		inputEnabled = true;

		this.model = defect;

		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);

		addComponents(layout);

		// Add TextUpdateListeners
		txtTitleListener = new TextUpdateListener(this, txtTitle);
		txtTitle.addKeyListener(txtTitleListener);

		txtDescriptionListener = new TextUpdateListener(this, txtDescription);
		txtDescription.addKeyListener(txtDescriptionListener);

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
		txtDescription.setBorder(txtTitle.getBorder());
		txtCreator = new JTextField(20);
		txtCreator.setEnabled(false);
		txtAssignee = new JTextField(20);
		tagPanel = new TagPanel(model);
		defectEventView = new DefectEventView(model);

		// set component names
		txtTitle.setName("Title");
		txtDescription.setName("Description");
		txtCreator.setName("Creator");
		txtAssignee.setName("Assignee");
		
		// set widths
		txtTitle.setMaximumSize(txtTitle.getPreferredSize());
		//txtDescription.setMaximumSize(txtDescription.getPreferredSize());
		txtCreator.setMaximumSize(txtCreator.getPreferredSize());
		txtAssignee.setMaximumSize(txtAssignee.getPreferredSize());
		tagPanel.setMaximumSize(tagPanel.getPreferredSize());

		JLabel lblTitle = new JLabel("Title:", LABEL_ALIGNMENT);
		JLabel lblDescription = new JLabel("Description:", LABEL_ALIGNMENT);
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

		layout.putConstraint(SpringLayout.NORTH, txtCreator, VERTICAL_PADDING, SpringLayout.SOUTH, txtDescription);
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

		add(lblTitle);
		add(txtTitle);
		add(lblDescription);
		add(txtDescription);
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
	public void setInputEnabled(boolean enabled) {
		inputEnabled = enabled;

		txtTitle.setEnabled(enabled);
		txtDescription.setEnabled(enabled);
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
		model.setTags(defect.getTags());

		//TODO model.setPermission(p, u);
		
		updateFields();
	}

	/**
	 * Updates the DefectPanel's fields to match those in the current model.
	 */
	private void updateFields() {
		txtTitle.setText(model.getTitle());
		txtDescription.setText(model.getDescription());
		if (model.getCreator() != null) {
			txtCreator.setText(model.getCreator().getUsername());
		}
		if (model.getAssignee() != null) {
			txtAssignee.setText(model.getAssignee().getUsername());
		}
		if (model.getTags() != null) {
			tagPanel.lmTags.clear();
			Iterator<Tag> tagsI = model.getTags().iterator();
			Tag nextTag;
			while (tagsI.hasNext()) {
				nextTag = tagsI.next();
				tagPanel.lmTags.addElement(nextTag.getName());
			}
		}
		
		// TODO expand this?
		
		txtTitleListener.checkIfUpdated();
		txtDescriptionListener.checkIfUpdated();
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
	 * TODO: Change return type to the abstract class / interface
	 * TODO: Ensure that if id field is set to -1, that a new defect is created on the server
	 * TODO: Do some basic input verification
	 * TODO: Deal with tags and other assignee
	 * @return the model represented by this view
	 */
	public Defect getEditedModel() {
		Defect defect = new Defect();
		defect.setId(model.getId());
		defect.setTitle(txtTitle.getText());
		defect.setDescription(txtDescription.getText());
		if (!(txtAssignee.getText().equals(""))) {
			defect.setAssignee(new User("", txtAssignee.getText(), "", -1));
		}
		defect.setCreator(new User("", txtCreator.getText(), "", -1));
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

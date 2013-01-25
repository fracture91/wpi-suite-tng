package edu.wpi.cs.wpisuitetng.modules.postboard.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * This is a model for the post board. It contains all of the messages
 * to be displayed on the board. It extends AbstractListModel so that
 * it can provide the model data to the JList component in the BoardPanel.
 * 
 * @author Chris Casola
 *
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class PostBoardModel extends AbstractListModel {
	
	/** The list of messages on the board */
	private final List<String> messages;
	
	/**
	 * Constructs a new board with no messages.
	 */
	public PostBoardModel() {
		messages = new ArrayList<String>();
	}
	
	/**
	 * Constructs a new board with the given list of messages
	 * 
	 * @param messages the list of messages to initialize the board with
	 */
	public PostBoardModel(List<String> messages) {
		this();
		for (String s : messages) {
			this.messages.add(s);
		}
	}

	/**
	 * Adds the given message to the board
	 * 
	 * @param newMessage the new message to add
	 */
	public void addMessage(String newMessage) {
		messages.add(newMessage);
	}
	
	/**
	 * Returns the list of messages
	 * @return the list of messages
	 */
	public List<String> getMessages() {
		return messages;
	}
	
	/* 
	 * Returns the message at the given index. This method is called
	 * internally by the JList in BoardPanel. Note this method returns
	 * elements in reverse order, so newest messages are returned first.
	 * 
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public Object getElementAt(int index) {
		return messages.get(messages.size() - 1 - index);
	}

	/*
	 * Returns the number of messages in the model. Also used internally
	 * by the JList in BoardPanel.
	 * 
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize() {
		return messages.size();
	}
}

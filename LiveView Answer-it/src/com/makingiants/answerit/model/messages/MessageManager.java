package com.makingiants.answerit.model.messages;

import android.content.Context;

import com.makingiants.answerit.model.dao.MessagesDAO;

/**
 * Manage the messages list
 */
public class MessageManager {
	
	// ****************************************************************
	// Attributes
	// ****************************************************************
	
	private String[] messages;
	private int actualMessage;
	
	// ****************************************************************
	// Constructor
	// ****************************************************************
	
	public MessageManager(Context context, int numberOfMessages) {
		
		this.actualMessage = 0;
		this.messages = MessagesDAO.getMessages(context, numberOfMessages);
	}
	
	// ****************************************************************
	// Accessor methods
	// ****************************************************************
	
	/**
	 * Add a message to the list if there is no message in the indexed position
	 * @param index must be > 0
	 * @param message
	 */
	public void addMessage(int index, String message) {
		messages[index] = message;
	}
	
	/**
	 * Get the actual message
	 * @return
	 */
	public String getActualMessage() {
		return messages[actualMessage];
	}
	
	/**
	 * Get the next message and update the actual message with it
	 */
	public String getNextMessage() {
		String message = null;
		
		do {
			if (++actualMessage >= messages.length) {
				actualMessage = 0;
			}
			message = messages[actualMessage];
			
		} while (message == null || message.equals(""));
		
		return messages[actualMessage];
	}
	
	/**
	 * Get the previous message and update the actual message with it
	 */
	public String getPreviousMessage() {
		String message = null;
		do {
			if (--actualMessage < 0) {
				actualMessage = messages.length - 1;
			}
			message = messages[actualMessage];
			
		} while (message == null || message.equals(""));
		
		return messages[actualMessage];
	}
	
}

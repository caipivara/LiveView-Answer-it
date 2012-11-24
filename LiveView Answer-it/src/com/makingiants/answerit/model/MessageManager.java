package com.makingiants.answerit.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MessageManager {
	
	private String[] messages;
	private int actualMessage;
	
	public MessageManager(Context context, int numberOfMessages) {
		this.actualMessage = 0;
		
		this.messages = new String[numberOfMessages];
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		numberOfMessages++;
		
		for (int i = 1; i < numberOfMessages; i++) {
			messages[i-1] = prefs.getString("message_" + i, "");
		}
		
	}
	
	/**
	 * 
	 * @param messageNumber must be > 0
	 * @param message
	 */
	public void addMessage(int messageNumber, String message) {
		messages[messageNumber - 1] = message;
	}
	
	public String getActualMessage() {
		return messages[actualMessage];
	}
	
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

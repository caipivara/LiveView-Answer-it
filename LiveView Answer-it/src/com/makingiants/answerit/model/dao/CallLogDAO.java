package com.makingiants.answerit.model.dao;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import com.makingiants.answerit.model.calls.Call;

/**
 * Know how to collect the call log
 */
public class CallLogDAO {
	
	/**
	 * Get the actual call log on the device
	 * 
	 * @param calls number of maximum calls to find
	 * @param context 
	 * 
	 * @return list of calls with size == calls
	 */
	public static ArrayList<Call> getCallLog(int calls, Context context) {
		ArrayList<Call> callLogs = new ArrayList<Call>();
		Set<String> callsAdded = new LinkedHashSet<String>();
		
		int maxCounter = 0;
		
		// Define Wich data query
		String[] projection = new String[] { CallLog.Calls.NUMBER, CallLog.Calls.DATE,
		        CallLog.Calls.TYPE, CallLog.Calls.CACHED_NAME };
		
		// Where will be the query
		Uri contacts = CallLog.Calls.CONTENT_URI;
		Cursor cursor = context.getContentResolver().query(contacts, projection, null, null,
		        CallLog.Calls.DATE + " DESC");
		// Make the query, date is used to get the list descending (first the last calls received)
		/*Cursor cursor = activity.managedQuery(contacts, projection, null, null, CallLog.Calls.DATE
		        + " DESC");*/
		try {
			if (cursor.moveToFirst()) {
				String name = null;
				String number = null;
				int type = 0;
				
				do {
					name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
					number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
					type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
					
					// Accept only INCOMING_TYPE and MISSED_TYPE calls
					// and unique numbers 
					if (type != CallLog.Calls.OUTGOING_TYPE && !callsAdded.contains(number)) {
						callLogs.add(new Call(name, number));
						callsAdded.add(number);
						maxCounter++;
					}
				} while (cursor.moveToNext() && maxCounter < calls);
			}
		} finally {
			cursor.close();
		}
		
		return callLogs;
	}
}

package com.makingiants.answerit.model.calls;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.makingiants.answerit.R;
import com.makingiants.answerit.model.dao.CallLogDAO;

/**
 * Manage the CallLog list
 */
public class CallManager {
	
	// ****************************************************************
	// Attributes
	// ****************************************************************
	
	private ArrayList<Call> calls;
	private int numberLogCalls;
	private int actualCall;
	
	// ****************************************************************
	// Constructor
	// ****************************************************************
	
	public CallManager(final Context context) {
		
		numberLogCalls = context.getResources().getInteger(R.integer.number_log_calls);
		this.calls = CallLogDAO.getCallLog(numberLogCalls, context);
		
		for (int i = 0; i < calls.size(); i++) {
			Log.e("MEssage", calls.get(i).getName() + " " + calls.get(i).getNumber());
		}
		
		this.actualCall = 0;
		
	}
	
	/**
	 * Update the actual list of calls
	 * @param context
	 */
	public void updateCalls(final Context context) {
		this.calls = CallLogDAO.getCallLog(numberLogCalls, context);
	}
	
	// ****************************************************************
	// Accessor Methods
	// ****************************************************************
	
	/**
	 * Get the actual call
	 * @return actual call
	 */
	public Call getActualCall() {
		return calls.get(actualCall);
	}
	
	/**
	 * Get the next call and update the actual call with it
	 * @return new call
	 */
	public Call getNextCall() {
		
		if (++actualCall >= calls.size()) {
			actualCall = 0;
		}
		return calls.get(actualCall);
		
	}
	
	/**
	 * Get the previous call and update the actual call with it
	 * @return new call
	 */
	public Call getPreviousCall() {
		
		if (--actualCall < 0) {
			actualCall = calls.size() - 1;
		}
		return calls.get(actualCall);
		
	}
	
}

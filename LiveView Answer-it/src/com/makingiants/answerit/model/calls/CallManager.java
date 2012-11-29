package com.makingiants.answerit.model.calls;

import java.util.ArrayList;

import android.content.Context;

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
	 * @return actual call if it exist, else return null
	 */
	public Call getActualCall() {
		if (calls.size() != 0) {
			return calls.get(actualCall);
		} else {
			return null;
		}
	}
	
	/**
	 * Get the next call and update the actual call with it
	 * @return next call if it exist, else return null
	 */
	public Call getNextCall() {
		if (calls.size() != 0) {
			if (++actualCall >= calls.size()) {
				actualCall = 0;
			}
			return calls.get(actualCall);
		} else {
			return null;
		}
		
	}
	
	/**
	 * Get the previous call and update the actual call with it
	 * @return previous call if it exist, else return null
	 */
	public Call getPreviousCall() {
		if (calls.size() != 0) {
			if (--actualCall < 0) {
				actualCall = calls.size() - 1;
			}
			return calls.get(actualCall);
		} else {
			return null;
		}
	}
	
	/**
	 * Return the number of calls
	 * @return
	 */
	public int getCallsLength() {
		return calls.size();
	}
}

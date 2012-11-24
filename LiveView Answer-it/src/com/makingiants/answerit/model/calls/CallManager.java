package com.makingiants.answerit.model.calls;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.makingiants.answerit.R;
import com.makingiants.answerit.model.dao.CallLogDAO;

public class CallManager {
	
	// ****************************************************************
	// Attributes
	// ****************************************************************
	
	private int numberLogCalls;
	private ArrayList<Call> calls;
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
	
	public void updateCalls(final Context context) {
		this.calls = CallLogDAO.getCallLog(numberLogCalls, context);
	}
	
	// ****************************************************************
	// Accessor Methods
	// ****************************************************************
	
	public Call getActualCall() {
		return calls.get(actualCall);
	}
	
	// ****************************************************************
	// Jump Methods
	// ****************************************************************
	
	public Call getNextCall() {
		
		if (++actualCall >= calls.size()) {
			actualCall = 0;
		}
		return calls.get(actualCall);
		
	}
	
	public Call getPreviousCall() {
		
		if (--actualCall < 0) {
			actualCall = calls.size() - 1;
		}
		return calls.get(actualCall);
		
	}
	
}

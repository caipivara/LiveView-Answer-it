package com.makingiants.answerit.model.calls;

public class Call {
	
	private String name;
	private String number;
	
	public Call(String name, String callLogs) {
		super();
		this.name = name;
		this.number = callLogs;
		
	}
	
	public String getName() {
		if (name != null) {
			return name;
		} else {
			return "Unknown";
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String call) {
		this.number = call;
	}
	
}

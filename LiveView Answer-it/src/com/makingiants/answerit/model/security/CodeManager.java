package com.makingiants.answerit.model.security;

public class CodeManager {
	
	public static boolean checkCode(String code) {
		if (code.equals("MakinGIANTS*ThankYou")) {
			return true;
		} else {
			return false;
		}
		
	}
}

package com.pomodoit.util;

public class DateFormater {

	public static String yyyymmddToDDMMYYYY(String date) throws Exception {
		String DD = date.substring(7, 8);
		String MM = date.substring(5, 6);
		String YYYY = date.substring(4);
		String sep = "/";
		
		return DD + sep + MM + sep + YYYY;
	}
	
}

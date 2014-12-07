package com.pomodoit.util;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
	
	/**
	 * Simple method to display a message on screen (Toast)
	 * For duration, 	2000ms = Toast.LENGTH_SHORT
	 * 					3500ms = Toast.LENGTH_LONG
	 *  
	 * @param c				Context
	 * @param txt			Message to display
	 * @param duration		Time to display the message
	 * @throws Exception	In case of error
	 */
	public static void displayToast(Context c, String txt, int duration) 
			throws Exception {
		Toast.makeText(c, txt, duration).show();
	}
	
	public static void displayToast(Context c, String txt) {
		// if duration is empty, the message'll be displayed for a shorty time
		Toast.makeText(c, txt, Toast.LENGTH_SHORT).show();
	}
}

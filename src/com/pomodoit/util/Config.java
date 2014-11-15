package com.pomodoit.util;

import com.pomodoit.joeybr.R;

import android.app.Application;

public class Config extends Application {
	
	// Global variables
	public static String new_session;
	public static String history;
	public static String help;
	public static String settings;

	@Override
	public void onCreate() {
		super.onCreate();
		
		// Retrieve data from resources
		new_session = getString(R.string.new_session);
		history = getString(R.string.history);
		help = getString(R.string.help);
		settings = getString(R.string.settings);
	}

}

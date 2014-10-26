package com.pomodoit.views;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Switch;

import com.example.pomodoit.R;
import com.pomodoit.db.MySQLiteHelper;
import com.pomodoit.util.Toaster;

public class SettingsActivity extends Activity {
	
	// Global Variables
	MySQLiteHelper db = new MySQLiteHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Action Bar Color
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.fontRed))));
		
		// Get Plane Mode
		boolean planeMode = db.getPlaneMode();
		Toaster.displayToast(this, "Plane mode: " + planeMode);
		
		// Update Switch Button
		updatePlaneButton(planeMode);
		
		// TODO: Listener on switch change value
		// if change to true : db.updatePlaneMode("yes")
		// if change to false: db.updatePlaneMode("yes")
	}
	
	private void updatePlaneButton(boolean p) {
		
		Switch switchPlane = (Switch) SettingsActivity.this.findViewById(R.id.switchPlaneMode);
		
		if (p == true) {
			switchPlane.setChecked(true);
		} else {
			switchPlane.setChecked(false);
		}
	}
}

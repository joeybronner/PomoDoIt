package com.pomodoit.views;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import com.pomodoit.joeybr.R;
import com.pomodoit.db.MySQLiteHelper;

public class SettingsActivity extends Activity {
	
	// Global Variables
	MySQLiteHelper db = new MySQLiteHelper(this);
	Switch switchPlane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Action Bar Color
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.fontRed))));
		
		// Switch from view
		switchPlane = (Switch) SettingsActivity.this.findViewById(R.id.switchPlaneMode);
		
		// Get Plane Mode
		boolean planeMode = db.getPlaneMode();
		
		// Update Switch Button
		updatePlaneButton(planeMode);
		
		// Listener on Switch Button
		switchPlane.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked==true) {
		        	db.updatePlaneMode("yes");
		        } else {
		        	db.updatePlaneMode("no");
		        }
		    }
		});
	}
	
	private void updatePlaneButton(boolean p) {
		if (p == true) {
			switchPlane.setChecked(true);
		} else {
			switchPlane.setChecked(false);
		}
	}
}

package com.pomodoit.views;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.pomodoit.db.MySQLiteHelper;
import com.pomodoit.joeybr.R;

public class SettingsActivity extends Activity {
	
	// Global Variables
	MySQLiteHelper db = new MySQLiteHelper(this);
	Switch switchSound, switchScreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Action Bar Color
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.fontRed))));
		
		// Switch from view
		switchSound = (Switch) SettingsActivity.this.findViewById(R.id.switchSoundMode);
		switchScreen = (Switch) SettingsActivity.this.findViewById(R.id.switchScreenMode);
		
		// Get Plane Mode
		boolean soundMode = db.getSoundMode();
		boolean screenMode = db.getScreenMode();
		
		// Update Switch Button
		updateSoundButton(soundMode);
		updateScreenButton(screenMode);
		
		// Listener on Switch Button
		switchSound.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		    @Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked==true) {
		        	db.updateSoundMode("yes");
		        } else {
		        	db.updateSoundMode("no");
		        }
		    }
		});
		switchScreen.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		    @Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked==true) {
		        	db.updateScreenMode("yes");
		        } else {
		        	db.updateScreenMode("no");
		        }
		    }
		});
	}
	
	private void updateSoundButton(boolean p) {
		if (p == true) {
			switchSound.setChecked(true);
		} else {
			switchSound.setChecked(false);
		}
	}
	
	private void updateScreenButton(boolean p) {
		if (p == true) {
			switchScreen.setChecked(true);
		} else {
			switchScreen.setChecked(false);
		}
	}
}

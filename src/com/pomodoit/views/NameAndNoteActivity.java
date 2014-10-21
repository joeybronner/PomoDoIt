package com.pomodoit.views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.pomodoit.R;

public class NameAndNoteActivity extends Activity
{	
	/* global vars */
	private TextView tvFelicitations;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name_and_note);
	}
	
	//
	// PROTECTED
	//

	protected static boolean isFieldEmpty(String txt) {
		String trimtxt = txt.trim();
		if (trimtxt.matches("")) {
		    return true;
		} else {
			return false;
		}
	}
	
	//
	// PRIVATE
	//

	@SuppressWarnings("unused")
	private String getTvFelicitations() {
		return tvFelicitations.getText().toString();
	}

	@SuppressWarnings("unused")
	private void setTvFelicitations(String txt) {
		this.tvFelicitations.setText(txt);
	}
}

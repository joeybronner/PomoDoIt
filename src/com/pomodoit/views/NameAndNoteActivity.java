package com.pomodoit.views;

import com.example.pomodoit.R;
import com.example.pomodoit.R.id;
import com.example.pomodoit.R.layout;
import com.example.pomodoit.R.menu;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
}

package com.pomodoit.views;

import com.example.pomodoit.R;
import com.example.pomodoit.R.id;
import com.example.pomodoit.R.layout;
import com.example.pomodoit.R.menu;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PauseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pause);
		
		/* action bar color */
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#197519")));
	}
}

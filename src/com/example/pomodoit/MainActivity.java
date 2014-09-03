package com.example.pomodoit;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity
{
	
	/* global vars */
	private long startTime = 0L;
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	protected ProgressBar mProgressBar;
	
	/* elements */
	private TextView tvTimer, tvMessage;
	private Button bt;
	
	private Handler customHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        /* action bar color */
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c0392b")));
        
        /* new type face */
        Typeface tf = Typeface.createFromAsset(this.getAssets(),"fonts/basictitlefont.ttf");
        
        /* progress bar */
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        
        /* type face font for timer */
        tvTimer = (TextView) findViewById(R.id.timerValue);
        tvTimer.setTypeface(tf);
        tvTimer.setTextSize(120);
        
        /* type face font for message textview */
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvMessage.setTypeface(tf);
        		
        /* type face font for the button */
        bt = (Button) findViewById(R.id.btMain);
        bt.setTypeface(tf);
        bt.setTextSize(40);
        bt.setOnClickListener(new View.OnClickListener()
        {
        	@Override
			public void onClick(View view)
        	{
        		if (bt.getText().equals("Commencer!"))
        		{
        			tvMessage.setText("REGLE NR 1\nCONCENTRE-TOI\nSUR UNE TACHE\nA LA FOIS.");
        			startTime = SystemClock.uptimeMillis();
            	    customHandler.postDelayed(updateTimerThread, 0);
            	    bt.setText("Arreter");
        		}
        		else if (bt.getText().equals("Arreter"))
        		{
        			timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                    bt.setText("Fin...");
        		}
        		else if (bt.getText().equals("Fin..."))
        		{
        			tvTimer.setText("25:00:00");
        			bt.setText("Commencer!");
        		}
        	}
        });

	}
	
	private Runnable updateTimerThread = new Runnable()
	{
		@Override
		public void run()
		{
			timeSwapBuff = 60000*25;
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;	
			updatedTime = timeSwapBuff - timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) ((updatedTime / 100) % 10);
			tvTimer.setText("" + mins 
					+ ":"
					+ String.format("%02d", secs)
					+ ":"
					+ String.format("%02d", milliseconds));
			customHandler.postDelayed(this, 0);
			
			/* progressbar update */
			final int progress = (int) (mProgressBar.getMax() * timeInMilliseconds / timeSwapBuff);
	        mProgressBar.setProgress(progress);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

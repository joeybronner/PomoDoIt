package com.pomodoit.views;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.pomodoit.R;
import com.pomodoit.util.Constants;

public class PauseActivity extends Activity {

	// Global Varibales
	private long startTime = 0L;
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	protected ProgressBar mProgressBar;
	
	// Elements
	private TextView tvPauseTitle, tvTimerPause;
	
	// Others
	private Handler customHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pause);
		
		// Action Bar Color
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.fontGreen))));
        
        // Get Pause TextView and Set font (size&fontfamily)
        tvPauseTitle = (TextView) findViewById(R.id.tvPauseTitle);
        tvPauseTitle.setTypeface(Constants.tf);
        tvPauseTitle.setTextSize(30);
        
        // Viewflipper declaration and Start Flipping
        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.startFlipping();
        
        // Calculate and Set Flipper's Padding        
        int paddingPixel = 80;
        float density = this.getResources().getDisplayMetrics().density;
        int paddingDp = (int)(paddingPixel * density);
        flipper.setPadding(paddingDp,paddingDp,paddingDp,paddingDp);
        
        // ProgressBar
        mProgressBar = (ProgressBar)findViewById(R.id.progressBarPause);
        
        // Timer
        tvTimerPause = (TextView) findViewById(R.id.timerPauseValue);
        tvTimerPause.setTypeface(Constants.tf);
        tvTimerPause.setTextSize(80);
        
        // Start pause for 5 minutes
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
	}
	
	private Runnable updateTimerThread = new Runnable()
	{
		@Override
		public void run()
		{
			//timeSwapBuff = 60000*5; // 5 minutes
			timeSwapBuff = 1000*25;
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;	
			updatedTime = timeSwapBuff - timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) ((updatedTime / 100) % 10);
			tvTimerPause.setText(String.format("%02d", mins) 
						+ 	":"
						+ 	String.format("%02d", secs)
						+ 	":"
						+ 	String.format("%02d", milliseconds));
			customHandler.postDelayed(this, 0);
			
			/* progressbar update */
			final int progress = (int) (mProgressBar.getMax() * timeInMilliseconds / timeSwapBuff);
	        mProgressBar.setProgress(progress);
	        
	        if (isFinishedTimer(mins, secs, milliseconds))
	        {
	        	customHandler.removeCallbacks(this);
	        	// "Voulez-vous commencer une nouvelle t�che de 25 minutes ?"
	        }
		}
	};
	
	private boolean isFinishedTimer(int _m, int _s, int _ms)
	{
		if(_m == 0 && _s == 0 && _ms == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
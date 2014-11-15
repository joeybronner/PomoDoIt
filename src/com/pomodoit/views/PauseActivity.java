package com.pomodoit.views;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.pomodoit.db.MySQLiteHelper;
import com.pomodoit.joeybr.R;
import com.pomodoit.util.Constants;
import com.pomodoit.util.Utilities;

public class PauseActivity extends Activity {

	// Global Varibales
	private long startTime = 0L;
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	protected ProgressBar mProgressBar;
	MySQLiteHelper db = new MySQLiteHelper(this);
	
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
        
		// Stay screen on if parameter is true
		if (db.getScreenMode()==true) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
        
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
			
			/* Progress Bar Update */
			final int progress = (int) (mProgressBar.getMax() * timeInMilliseconds / timeSwapBuff);
	        mProgressBar.setProgress(progress);
	        
	        if (Utilities.isFinishedTimer(mins, secs, milliseconds))
	        {
	        	customHandler.removeCallbacks(this);
	        	showNewSession();
	        }
		}
	};
	
	private void showNewSession()
	{
		// create the new dialog
		final Dialog dialog = new Dialog(tvTimerPause.getContext());
		// no title
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		dialog.setCancelable(false);
		// content of the dialog
		dialog.setContentView(R.layout.activity_new_session);
		// Button No --> Go home
		Button btNo = (Button) dialog.findViewById(R.id.btNo);
		btNo.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				finish();
				showHomeView();
			}
		});
		// Button Yes --> 
		Button btYes = (Button) dialog.findViewById(R.id.btYes);
		btYes.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				finish();
				showMainView();
			}
		});

		dialog.show();
	}
	
	private void showHomeView()
	{
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}
	
	private void showMainView()
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}

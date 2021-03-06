package com.pomodoit.views;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pomodoit.db.MySQLiteHelper;
import com.pomodoit.db.Session;
import com.pomodoit.joeybr.R;
import com.pomodoit.util.Constants;
import com.pomodoit.util.Toaster;
import com.pomodoit.util.Utilities;

public class MainActivity extends Activity
{	
	// Global Variables
	private long startTime = 0L;
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	protected ProgressBar mProgressBar;
	protected static Context cont;
	MySQLiteHelper db = new MySQLiteHelper(this);
	Resources res;
	MediaPlayer mp;

	// Elements
	private TextView tvTimer, tvMessage;
	private Button bt;

	// Texts
	String[] quotes;
	int i = 0;
	boolean change = true;

	public Handler customHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cont = this.getBaseContext();

		// Action Bar Color
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.fontRed))));

		// Progress Bar 
		mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

		// Type Face Font for timer
		tvTimer = (TextView) findViewById(R.id.timerValue);
		tvTimer.setTypeface(Constants.tf);
		tvTimer.setTextSize(80);

		// Type Face Font for message text view
		tvMessage = (TextView) findViewById(R.id.tvMessage);
		tvMessage.setTypeface(Constants.tf);

		// Resources
		res = getResources();

		// Stay screen on if parameter is true
		if (db.getScreenMode()==true) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}

		/* type face font for the button */
		bt = (Button) findViewById(R.id.btMain);
		bt.setTypeface(Constants.tf);
		bt.setTextSize(30);
		bt.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				// Start Session
				if (bt.getText().equals(getResources().getString(R.string.btStart)))
				{
					startTime = SystemClock.uptimeMillis();
					customHandler.postDelayed(updateTimerThread, 0);
					bt.setText(getResources().getString(R.string.btStop));
				}
				// Stop Session
				else if (bt.getText().equals(getResources().getString(R.string.btStop)))
				{
					timeSwapBuff += timeInMilliseconds;
					customHandler.removeCallbacks(updateTimerThread);
					bt.setText(getResources().getString(R.string.btEnd));
				}
				// End Session
				else if (bt.getText().equals(getResources().getString(R.string.btEnd)))
				{
					tvTimer.setText(getResources().getString(R.string.timerVal));
					bt.setText(getResources().getString(R.string.btStart));
					tvMessage.setBackground(res.getDrawable(R.drawable.im_clock));
					Constants.round = 0;
				}
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// Stop timer thread and kill activity 
		customHandler.removeCallbacks(updateTimerThread);
		this.finish();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		onDestroy();
		super.onBackPressed();
	}

	private Runnable updateTimerThread = new Runnable()
	{
		@Override
		public void run()
		{
			timeSwapBuff = 60000*25; // 25 minutes
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;	
			updatedTime = timeSwapBuff - timeInMilliseconds;

			// Get all quotes from resources
			quotes = MainActivity.this.getResources().getStringArray(R.array.msg_motive); 

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) ((updatedTime / 100) % 10);
			tvTimer.setText(String.format("%02d", mins) 
					+ 	":"
					+ 	String.format("%02d", secs)
					+ 	":"
					+ 	String.format("%02d", milliseconds));
			customHandler.postDelayed(this, 0);
			
			// Change background image (clock image)
			tvMessage.setBackground(getImageClock(secs));

			// Change quotes
			if (secs == 59 && milliseconds == 0.00 && change == true) {
				tvMessage.setText(quotes[i]);
				change = false;
				if (i==9) {
					i = 0;
				} else {
					i++;
				}
			}

			// Progress Bar Update
			final int progress = (int) (mProgressBar.getMax() * timeInMilliseconds / timeSwapBuff);
			mProgressBar.setProgress(progress);

			if (Utilities.isFinishedTimer(mins, secs, milliseconds))
			{
				customHandler.removeCallbacks(this);
				// Play sound if activated (in settings)
				if (db.getSoundMode()==true) {
					playSound();
				}
				showNameAndNote();
			}
		}
	};

	private void playSound() {
		try {	
			mp = MediaPlayer.create(this, R.raw.sound);
			mp.start();
		} catch (Exception err) {
			Toaster.displayToast(MainActivity.this.getBaseContext(),
					getResources().getString(R.string.err_activate_planemode));
		}
	}

	private void showPauseView()
	{
		Utilities.openView(this, PauseActivity.class);
	}

	private void showNameAndNote()
	{
		// create the new dialog
		final Dialog dialog = new Dialog(tvTimer.getContext());
		// no title
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		dialog.setCancelable(false);

		// content of the dialog
		dialog.setContentView(R.layout.activity_name_and_note);
		Button btSubmit = (Button) dialog.findViewById(R.id.btSubmit);
		btSubmit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EditText etName = (EditText) dialog.findViewById(R.id.etName);
				RatingBar stars = (RatingBar) dialog.findViewById(R.id.ratingSession);
				try { 
					if (NameAndNoteActivity.isFieldEmpty(etName.getText().toString())) {
						Toaster.displayToast(MainActivity.this.getBaseContext(), 
								getResources().getString(R.string.err_noname), 
								3000);
					} else {
						// Increment Round (number of rounds)
						Constants.round++;

						// Send Name & Note to Database
						String name = etName.getText().toString();
						float mark = stars.getRating();
						Session s = new Session(name, mark);
						if (db.uniqueSession(s)==true) {
							db.addSession(s);
							dialog.dismiss();
							showPauseView();
							finish();
						} else {
							Toaster.displayToast(MainActivity.this.getBaseContext(), 
									getResources().getString(R.string.err_nameexists), 
									3000);
						}
					}
				} catch (Exception ex) {
					Log.v("PomoDoIt", "Error while send new session in detabase.");
				}
			}
		});
		dialog.show();
	}

	private Drawable getImageClock(int secs) {
		// Image Clock
		if (secs<5) {	
			return res.getDrawable(R.drawable.im_clock55);
		} else if (secs > 5 && secs <= 10) {
			return res.getDrawable(R.drawable.im_clock50);
		} else if (secs > 10 && secs <= 15) {
			return res.getDrawable(R.drawable.im_clock45);
		} else if (secs > 15 && secs <= 20) {
			return res.getDrawable(R.drawable.im_clock40);
		} else if (secs > 20 && secs <= 25) {
			return res.getDrawable(R.drawable.im_clock35);
		} else if (secs > 25 && secs <= 30) {
			change = true;
			return res.getDrawable(R.drawable.im_clock30);
		} else if (secs > 30 && secs <= 35) {
			return res.getDrawable(R.drawable.im_clock25);
		} else if (secs > 35 && secs <= 40) {
			return res.getDrawable(R.drawable.im_clock20);
		} else if (secs > 40 && secs <= 45) {
			return res.getDrawable(R.drawable.im_clock15);
		} else if (secs > 45 && secs <= 50) {
			return res.getDrawable(R.drawable.im_clock10);
		} else if (secs > 50 && secs <= 55) {
			return res.getDrawable(R.drawable.im_clock05);
		} else if (secs > 55) {
			return res.getDrawable(R.drawable.im_clock00);
		} else {
			return res.getDrawable(R.drawable.im_clock00);
		}
	}
}

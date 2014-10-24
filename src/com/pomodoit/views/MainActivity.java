package com.pomodoit.views;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pomodoit.R;
import com.pomodoit.db.MySQLiteHelper;
import com.pomodoit.db.Session;
import com.pomodoit.util.Constants;
import com.pomodoit.util.Toaster;

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


	/* elements */
	private TextView tvTimer, tvMessage;
	private Button bt;

	public Handler customHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cont = this.getBaseContext();

		/* action bar color */
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.fontRed))));

		/* progress bar */
		mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

		/* type face font for timer */
		tvTimer = (TextView) findViewById(R.id.timerValue);
		tvTimer.setTypeface(Constants.tf);
		tvTimer.setTextSize(80);

		/* type face font for message textview */
		tvMessage = (TextView) findViewById(R.id.tvMessage);
		tvMessage.setTypeface(Constants.tf);

		/* type face font for the button */
		bt = (Button) findViewById(R.id.btMain);
		bt.setTypeface(Constants.tf);
		bt.setTextSize(30);
		bt.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (bt.getText().equals(getResources().getString(R.string.btStart)))
				{
					tvMessage.setText(getResources().getString(R.string.msg_motive_1));
					startTime = SystemClock.uptimeMillis();
					customHandler.postDelayed(updateTimerThread, 0);
					bt.setText(getResources().getString(R.string.btStop));
				}
				else if (bt.getText().equals(getResources().getString(R.string.btStop)))
				{
					timeSwapBuff += timeInMilliseconds;
					customHandler.removeCallbacks(updateTimerThread);
					bt.setText(getResources().getString(R.string.btEnd));
				}
				else if (bt.getText().equals(getResources().getString(R.string.btEnd)))
				{
					tvTimer.setText(getResources().getString(R.string.timerVal));
					bt.setText(getResources().getString(R.string.btStart));
				}
			}
		});

	}

	private Runnable updateTimerThread = new Runnable()
	{
		@Override
		public void run()
		{
			//timeSwapBuff = 60000*25;
			timeSwapBuff = 100*25;
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;	
			updatedTime = timeSwapBuff - timeInMilliseconds;

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

			/* Progress Bar Update */
			final int progress = (int) (mProgressBar.getMax() * timeInMilliseconds / timeSwapBuff);
			mProgressBar.setProgress(progress);

			if (isFinishedTimer(mins, secs, milliseconds))
			{
				customHandler.removeCallbacks(this);
				showNameAndNote();
			}
		}
	};

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
						db.addSession(new Session(name, mark));
						dialog.dismiss();
						finish();
						showPauseView();		
					}
				} catch (Exception ex) {
					// Nothing.
				}
			}
		});

		dialog.show();
	}

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

	private void showPauseView()
	{
		Intent intent = new Intent(this, PauseActivity.class);
		startActivity(intent);
	}


}

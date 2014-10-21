package com.pomodoit.views;

import java.util.List;

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
import com.pomodoit.util.Constants;
import com.pomodoit.util.Toaster;
import com.pomodoit.db.MySQLiteHelper;
import com.pomodoit.db.Session;

public class MainActivity extends Activity
{	
	/* global vars */
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

	private Handler customHandler = new Handler();

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
								"Erreur, veuillez entrer un nom à votre activité.", 
								3000);
					} else {
						// --> send values into database (2 values (name & note))
						String name = etName.getText().toString();
						float mark = stars.getRating();
						db.addSession(new Session(name, mark));
						List<Session> sess = db.getAllSessions();
						for (final Session s : sess) {
							Toaster.displayToast(MainActivity.this.getBaseContext(), 
									"name: " + s.getName() + "\n" +
									"mark: " + s.getMark() + "\n" + 
									"date: " + s.getDate());
						}
						
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

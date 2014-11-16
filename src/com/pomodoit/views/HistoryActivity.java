package com.pomodoit.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pomodoit.adapter.ListViewAdapter;
import com.pomodoit.db.MySQLiteHelper;
import com.pomodoit.db.Session;
import com.pomodoit.joeybr.R;
import com.pomodoit.util.DateFormater;

@SuppressWarnings("rawtypes")
public class HistoryActivity extends Activity {

	// Global variables
	private ArrayList<HashMap> list;
	public MySQLiteHelper db = new MySQLiteHelper(this); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		// Action Bar color
		ActionBar bar = getActionBar();
		ColorDrawable red = new ColorDrawable(Color.parseColor(getResources().getString(R.color.fontRed)));
		bar.setBackgroundDrawable(red);
		
		// Refresh number of activities
		final TextView tvCount = (TextView) findViewById(R.id.tvCount);
		tvCount.setText(String.valueOf(db.getSizeSessions()));
		
		ListView lview = (ListView) findViewById(R.id.listview);
		ImageView ivEmoticon = (ImageView) findViewById(R.id.ivEmoticon);
		if (db.getSizeSessions()==0) {
			// Hide Lsit View and show smiley :(
			lview.setVisibility(View.INVISIBLE);
			ivEmoticon.setVisibility(View.VISIBLE);
		}

		// ListView of items
		try {
			populateList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ListViewAdapter adapter = new ListViewAdapter(this, list);
		lview.setAdapter(adapter);
	}

	private void populateList() throws Exception {

		list = new ArrayList<HashMap>();

		List<Session> sess = db.getAllSessions();
		for (final Session s : sess) {
			HashMap<String, String> temp = new HashMap<String, String>();
			String date = DateFormater.yyyymmddToDDMMYYYY(s.getDate());
			temp.put("First",date);
			temp.put("Second",s.getName());
			temp.put("Third", getStars(HistoryActivity.this.getApplicationContext(), 
					String.valueOf(s.getMark())));
			list.add(temp);
		}
	}
	
	private static String getStars(Context c, String s) throws Exception {
		if (s.equals("0.0") || s.equals("0.5")) {
			return "-";
		}
		else if (s.equals("1.0") || s.equals("1.5")) {
			return c.getResources().getString(R.string.one_star);
		}
		else if (s.equals("2.0") || s.equals("2.5")) {
			return c.getResources().getString(R.string.two_star);
		}
		else if (s.equals("3.0") || s.equals("3.5")) {
			return c.getResources().getString(R.string.three_star);
		}
		else if (s.equals("4.0") || s.equals("4.5")) {
			return c.getResources().getString(R.string.four_star);
		}
		else if (s.equals("5.0")) {
			return c.getResources().getString(R.string.five_star);
		}
		else {
			return "-";
		}
	}

	// --------------------------------------------------------------------- //
	// MENU BAR																 //
	// --------------------------------------------------------------------- //

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_delete_history) {
			AlertDialog.Builder ab = new AlertDialog.Builder(HistoryActivity.this);
			ab.setMessage(getResources().getString(R.string.msg_delete_history))
			.setPositiveButton(getResources().getString(R.string.oui), 
					dialogDemoListener)
			.setNegativeButton(getResources().getString(R.string.non), 
					dialogDemoListener).show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	DialogInterface.OnClickListener dialogDemoListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			switch (which)
			{
			case DialogInterface.BUTTON_POSITIVE:
				// Delete History
				db.deleteAllSessions();
				onResume();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				// Nothing
				break;
			}
		}
	};
	
	@Override
	public void onResume() {
	   super.onResume();
	   super.onCreate(null);
	}
	

}

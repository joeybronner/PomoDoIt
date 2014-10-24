package com.pomodoit.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.pomodoit.R;
import com.pomodoit.adapter.ListViewAdapter;
import com.pomodoit.db.MySQLiteHelper;
import com.pomodoit.db.Session;
import com.pomodoit.util.DateFormater;

@SuppressWarnings("rawtypes")
public class HistoryActivity extends Activity {

	// Global variables
	private ArrayList<HashMap> list;
	MySQLiteHelper db = new MySQLiteHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		// Action Bar color
		ActionBar bar = getActionBar();
		ColorDrawable red = new ColorDrawable(Color.parseColor(getResources().getString(R.color.fontRed)));
		bar.setBackgroundDrawable(red);

		// ListView of items
		ListView lview = (ListView) findViewById(R.id.listview);
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

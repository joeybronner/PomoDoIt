package com.pomodoit.views;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.pomodoit.R;
import com.pomodoit.adapter.ListViewAdapter;

@SuppressWarnings("rawtypes")
public class HistoryActivity extends Activity {
	
	// Global variables
	private ArrayList<HashMap> list;

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
        populateList();
        ListViewAdapter adapter = new ListViewAdapter(this, list);
        lview.setAdapter(adapter);
	}
	
	private void populateList() {
    	 
        list = new ArrayList<HashMap>();
 
        for (int i=0; i<50; i++) {
            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put("First","YYYY/MM/JJ");
            temp.put("Second", "Nom de l'activité");
            temp.put("Third", "Note");
        list.add(temp);
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

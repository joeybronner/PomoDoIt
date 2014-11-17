package com.pomodoit.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pomodoit.db.MySQLiteHelper;
import com.pomodoit.joeybr.R;
import com.pomodoit.util.Utilities;
import com.pomodoit.views.HistoryActivity;

@SuppressWarnings("rawtypes")
public class ListViewAdapter extends BaseAdapter
{

	public ArrayList<HashMap> list;
	Activity activity;
	String sessionName;

	public ListViewAdapter(Activity activity, ArrayList<HashMap> list) {
		super();
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private class ViewHolder {
		TextView txtFirst;
		TextView txtSecond;
		TextView txtThird;
	}

	@SuppressLint("ResourceAsColor") @Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		final ViewHolder holder;
		LayoutInflater inflater =  activity.getLayoutInflater();

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.listview_history, null);
			holder = new ViewHolder();
			holder.txtFirst = (TextView) convertView.findViewById(R.id.FirstText);
			holder.txtSecond = (TextView) convertView.findViewById(R.id.SecondText);
			holder.txtThird = (TextView) convertView.findViewById(R.id.ThirdText);

			// Alternate color line of ListView
			if(position%2==0) {
				holder.txtFirst.setBackgroundColor(Color.parseColor("#C0392B"));
				holder.txtSecond.setBackgroundColor(Color.parseColor("#C0392B"));
				holder.txtThird.setBackgroundColor(Color.parseColor("#C0392B"));
			}
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap map = list.get(position);
		holder.txtFirst.setText((CharSequence)map.get("First"));
		holder.txtSecond.setText((CharSequence)map.get("Second"));
		holder.txtThird.setText((CharSequence)map.get("Third"));
		
		convertView.setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{ 
				sessionName = holder.txtSecond.getText().toString();
				AlertDialog.Builder ab = new AlertDialog.Builder(parent.getContext());
				ab.setMessage(parent.getResources().getString(R.string.msg_delete_activity))
				.setPositiveButton(parent.getResources().getString(R.string.oui), 
						dialogDeleteLine)
				.setNegativeButton(parent.getResources().getString(R.string.non), 
						dialogDeleteLine).show();
				return true;
			}
		});

		return convertView;
	}

	DialogInterface.OnClickListener dialogDeleteLine = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			final MySQLiteHelper db = new MySQLiteHelper(activity.getBaseContext());
			switch (which)
			{
			case DialogInterface.BUTTON_POSITIVE:
				// Delete entry and refresh view
				db.deleteSession(sessionName);
				notifyDataSetChanged();
				Utilities.openView(activity, HistoryActivity.class);
				// Close this one
				activity.finish();
			case DialogInterface.BUTTON_NEGATIVE:
				// Nothing
				break;
			}
		}
	};
}
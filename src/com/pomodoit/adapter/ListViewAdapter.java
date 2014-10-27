package com.pomodoit.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import com.pomodoit.joeybr.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class ListViewAdapter extends BaseAdapter
{

	public ArrayList<HashMap> list;
	Activity activity;

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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater =  activity.getLayoutInflater();

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.listview_history, null);
			holder = new ViewHolder();
			holder.txtFirst = (TextView) convertView.findViewById(R.id.FirstText);
			holder.txtSecond = (TextView) convertView.findViewById(R.id.SecondText);
			holder.txtThird = (TextView) convertView.findViewById(R.id.ThirdText);
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

		return convertView;
	}
}
package com.pomodoit.views;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pomodoit.R;
import com.pomodoit.util.Constants;

public class HomeActivity extends Activity implements OnItemClickListener
{

	//static final String EXTRA_MAP = "map";
	private String _errormsg;
	static final LauncherIcon[] ICONS =
		{
		// LauncherIcon (<icon>, <text>, <id>)
		new LauncherIcon(R.drawable.im_start, "Nouvelle activité", "new_activity"),
		new LauncherIcon(R.drawable.im_histo, "Historique", "historique"),
		new LauncherIcon(R.drawable.im_aide, "Aide", "help"),
		new LauncherIcon(R.drawable.im_preferences, "Préférences", "settings"),
		};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		GridView gridview = (GridView) findViewById(R.id.dashboard_grid);
		gridview.setAdapter(new ImageAdapter(this));
		gridview.setOnItemClickListener(this);

		// Action Bar Color
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.fontRed))));

		// Type face 
		Constants.tf = Typeface.createFromAsset(this.getAssets(),"fonts/OpenSans-Light.ttf");

		// Reset the number of rounds
		Constants.round = 0;
		
		// Hack to disable GridView scrolling
		gridview.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return event.getAction() == MotionEvent.ACTION_MOVE;
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	{
		// here, redirect to the good view
		String icon_selected = ICONS[position].map;
		if (icon_selected.equals("new_activity"))
		{
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		else if (icon_selected.equals("historique"))
		{
			Intent intent = new Intent(this, HistoryActivity.class);
			startActivity(intent);
		}
		else if (icon_selected.equals("help"))
		{

		}
		else if (icon_selected.equals("settings"))
		{

		}
		else
		{
			_errormsg = "Erreur lors de l'ouverture d'une vue.";
			Toast toast = Toast.makeText(this, "Erreur: " + _errormsg, Toast.LENGTH_LONG);
			toast.show();
		}
	}

	static class LauncherIcon
	{
		final String text;
		final int imgId;
		final String map;

		public LauncherIcon(int imgId, String text, String map)
		{
			super();
			this.imgId = imgId;
			this.text = text;
			this.map = map;
		}

	}

	static class ImageAdapter extends BaseAdapter
	{
		private Context mContext;

		public ImageAdapter(Context c)
		{
			mContext = c;
		}

		@Override
		public int getCount()
		{
			return ICONS.length;
		}

		@Override
		public LauncherIcon getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return 0;
		}

		static class ViewHolder
		{
			public ImageView icon;
			public TextView text;
		}

		// Create a new ImageView for each item referenced by the Adapter
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v = convertView;
			ViewHolder holder;
			if (v == null)
			{
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);

				v = vi.inflate(R.layout.dashboard_icon, null);
				holder = new ViewHolder();
				holder.text = (TextView) v.findViewById(R.id.dashboard_icon_text);
				holder.icon = (ImageView) v.findViewById(R.id.dashboard_icon_img);
				v.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) v.getTag();
			}

			holder.icon.setImageResource(ICONS[position].imgId);
			holder.text.setText(ICONS[position].text);

			return v;
		}
	}
}
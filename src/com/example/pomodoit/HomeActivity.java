package com.example.pomodoit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
 
public class HomeActivity extends Activity implements OnItemClickListener
{
 
    static final String EXTRA_MAP = "map";
    private TextView tvTitre;
 
    static final LauncherIcon[] ICONS =
    {
    	new LauncherIcon(R.drawable.im_start, "Nouvelle activité", "metro.png"),
        new LauncherIcon(R.drawable.im_stats, "Statistiques", "rer.png"),
        new LauncherIcon(R.drawable.im_aide, "Aide", "bus.png"),
        new LauncherIcon(R.drawable.im_preferences, "Préférences", "noctilien.png"),
    };
 
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
 
        GridView gridview = (GridView) findViewById(R.id.dashboard_grid);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(this);
        
        /* action bar color */
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c0392b")));
 
        // Hack to disable GridView scrolling
        gridview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
    }
 
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MAP, ICONS[position].map);
        startActivity(intent);
    }
 
    static class LauncherIcon {
        final String text;
        final int imgId;
        final String map;
 
        public LauncherIcon(int imgId, String text, String map) {
            super();
            this.imgId = imgId;
            this.text = text;
            this.map = map;
        }
 
    }
 
    static class ImageAdapter extends BaseAdapter {
        private Context mContext;
 
        public ImageAdapter(Context c) {
            mContext = c;
        }
 
        @Override
        public int getCount() {
            return ICONS.length;
        }
 
        @Override
        public LauncherIcon getItem(int position) {
            return null;
        }
 
        @Override
        public long getItemId(int position) {
            return 0;
        }
 
        static class ViewHolder {
            public ImageView icon;
            public TextView text;
        }
 
        // Create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
 
                v = vi.inflate(R.layout.dashboard_icon, null);
                holder = new ViewHolder();
                holder.text = (TextView) v.findViewById(R.id.dashboard_icon_text);
                holder.icon = (ImageView) v.findViewById(R.id.dashboard_icon_img);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
 
            holder.icon.setImageResource(ICONS[position].imgId);
            holder.text.setText(ICONS[position].text);
 
            return v;
        }
    }
}
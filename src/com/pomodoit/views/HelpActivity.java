package com.pomodoit.views;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.pomodoit.joeybr.R;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		// Action Bar Color
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.fontRed))));

		ImageView imYoutubePomo = (ImageView) findViewById(R.id.ivYoutubePomodoro);
		imYoutubePomo.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// If Youtube is installed it will be launched
				// Without it you will be prompted with a list of the application to choose.
				String video_path = getResources().getString(R.string.video_link);
				Uri uri = Uri.parse(video_path);
				uri = Uri.parse("vnd.youtube:"  + uri.getQueryParameter("v"));

				// Start playing video (Youtube or other application)
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}
}

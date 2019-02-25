package io.waffle.srafi2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class StartActivity extends Activity {
	private VideoView myVideoView;
	private int backButtonCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		backButtonCount = 0;

		myVideoView = (VideoView) findViewById(R.id.bg);

		try {
			String path = "android.resource://" + getPackageName() + "/" + R.raw.bg_anim;
			myVideoView.setVideoURI(Uri.parse(path));

		} catch (Exception e) {
			e.printStackTrace();
		}

		myVideoView.requestFocus();
		myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			public void onPrepared(MediaPlayer mediaPlayer) {
				myVideoView.seekTo(0);
				myVideoView.start();
			}
		});
		myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mediaPlayer) {
				myVideoView.seekTo(0);
				myVideoView.start();
			}
		});

		TextView title = (TextView) findViewById(R.id.title);
		SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		int clr = settings.getInt("colorTheme", 0);

		switch (clr) {
			case 0:
				title.setTextColor(Color.BLUE);
				break;
			case 1:
				title.setTextColor(Color.RED);
				break;
			case 2:
				title.setTextColor(Color.YELLOW);
				break;
			case 3:
				title.setTextColor(Color.parseColor("#ffa500"));
				break;
			case 4:
				title.setTextColor(Color.GREEN);
				break;
			case 5:
				title.setTextColor(Color.parseColor("#800080"));
				break;
		}
	}

	public void playClassic(View view) {
		Intent intent = new Intent(this, AndroidLauncher.class);
		intent.putExtra("gamemode", Gamemode.CLASSIC.getValue());
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	public void gamemodes(View view) {
		Intent intent = new Intent(this, Gamemodes.class);
		startActivity(intent);
		overridePendingTransition(R.anim.up_in, R.anim.up_out);
	}

	public void settings(View view) {
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

	public void rate(View view) {
		Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=io.waffle.srafi2");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	@Override
	public void onBackPressed()	{
		if (backButtonCount >= 1) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
			backButtonCount++;
		}
	}
}

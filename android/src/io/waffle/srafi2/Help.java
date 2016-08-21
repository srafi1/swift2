package io.waffle.srafi2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Help extends Activity {
	String sightTitle, sightText;
	String snowballTitle, snowballText;
	String uphillTitle, uphillText;
	String feedTitle, feedText;
	String scrollTitle, scrollText;
	String jumpTitle, jumpText;
	String errorTitle, errorText;
	int mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		sightTitle = "Sight";
		sightText = "The ball moves up the screen instead of the camera view. The view only advances when you get to the top.";
		snowballTitle = "Snowball";
		snowballText = "The ball gets larger over time. Crash into gray rocks to decrease size. Crashing into a black rock is game over.";
		uphillTitle = "Uphill";
		uphillText = "The ball gets smaller over time. Collect small circles to grow. Crashing into a rock is game over";
		feedTitle = "Feed";
		feedText = "The ball changes to the opposite color over time (reaching it is game over). Eat obstacles to retain color";
		scrollTitle = "Scroll";
		scrollText = "The ball moves left and right automatically. Scroll obstacles up and down to dodge obstacles.";
		jumpTitle = "Jump";
		jumpText = "The ball falls with gravity. Tap to jump back up.";
		errorTitle = "Error";
		errorText = "Error in getting gamemode data";

		TextView title = (TextView) findViewById(R.id.help_title);
		TextView content = (TextView) findViewById(R.id.help_text);

		mode = getIntent().getIntExtra("gamemode", 0);
		if (mode == Gamemode.CLASSIC.getValue()) {
			title.setText(errorTitle);
			content.setText(errorText);
		} else if (mode == Gamemode.SIGHT.getValue()) {
			title.setText(sightTitle);
			content.setText(sightText);
		} else if (mode == Gamemode.SNOWBALL.getValue()) {
			title.setText(snowballTitle);
			content.setText(snowballText);
		} else if (mode == Gamemode.UPHILL.getValue()) {
			title.setText(uphillTitle);
			content.setText(uphillText);
		} else if (mode == Gamemode.FEED.getValue()) {
			title.setText(feedTitle);
			content.setText(feedText);
		} else if (mode == Gamemode.SCROLL.getValue()) {
			title.setText(scrollTitle);
			content.setText(scrollText);
		} else if (mode == Gamemode.JUMP.getValue()) {
			title.setText(jumpTitle);
			content.setText(jumpText);
		}
	}

	public void play(View view) {
		Intent intent = new Intent(this, AndroidLauncher.class);
		intent.putExtra("gamemode", mode);
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	public void back(View view) {
		Intent intent = new Intent(this, Gamemodes.class);
		startActivity(intent);
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

	@Override
	public void onBackPressed() {
		back(new View(this));
	}
}

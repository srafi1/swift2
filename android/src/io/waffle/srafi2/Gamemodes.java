package io.waffle.srafi2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Gamemodes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamemodes);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, StartActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.down_in, R.anim.down_out);
	}

	public void sight(View view) {
		gamemode(Gamemode.SIGHT);
	}

	public void sightHelp(View view) {
		help(Gamemode.SIGHT);
	}

	public void snowball(View view) {
		gamemode(Gamemode.SNOWBALL);
	}

	public void snowballHelp(View view) {
		help(Gamemode.SNOWBALL);
	}

	public void uphill(View view) {
		gamemode(Gamemode.UPHILL);
	}

	public void uphillHelp(View view) {
		help(Gamemode.UPHILL);
	}

	public void feed(View view) {
		gamemode(Gamemode.FEED);
	}

	public void feedHelp(View view) {
		help(Gamemode.FEED);
	}

	public void scroll(View view) {
		gamemode(Gamemode.SCROLL);
	}

	public void scrollHelp(View view) {
		help(Gamemode.SCROLL);
	}

	public void jump(View view) {
		gamemode(Gamemode.JUMP);
	}

	public void jumpHelp(View view) {
		help(Gamemode.JUMP);
	}

	private void help(Gamemode mode) {
		Intent intent = new Intent(this, Help.class);
		intent.putExtra("gamemode", mode.getValue());
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	private void gamemode(Gamemode mode) {
		Intent intent = new Intent(this, AndroidLauncher.class);
		intent.putExtra("gamemode", mode.getValue());
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}
}

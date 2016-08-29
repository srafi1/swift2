package io.waffle.srafi2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.appszoom.appszoomsdk.Appszoom;

public class GameOverActivity extends Activity {
	private int mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gameover);

		mode = getIntent().getIntExtra("mode", 0);
		int score = getIntent().getIntExtra("score", 0);

		TextView modeView = (TextView) findViewById(R.id.gamemode);
		String modeName = "";
		switch (mode) {
			case 0:
				modeName = "Classic";
				break;
			case 1:
				modeName = "Sight";
				break;
			case 2:
				modeName = "Snowball";
				break;
			case 3:
				modeName = "Uphill";
				break;
			case 4:
				modeName = "Pacman";
				break;
			case 5:
				modeName = "Scroll";
				break;
			case 6:
				modeName = "Jump";
				break;
		}
		modeView.setText(modeName);

		TextView scoreView = (TextView) findViewById(R.id.score);
		scoreView.setText("Score: " + score);

		SharedPreferences highscores = getSharedPreferences("highscores", MODE_PRIVATE);
		int highscore = highscores.getInt(""+mode, 0);
		if (score > highscore) {
			highscore = score;
			SharedPreferences.Editor edit = highscores.edit();
			edit.putInt(""+mode, highscore);
			edit.commit();
		}
		TextView highscoreView = (TextView) findViewById(R.id.highscore);
		highscoreView.setText("Highcore: " + highscore);

		TextView title = (TextView) findViewById(R.id.gameOverTitle);
		int clr = getSharedPreferences("settings", MODE_PRIVATE).getInt("colorTheme", 0);
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

		Appszoom.start(this);
		int gameNum = highscores.getInt("gamenum", 0);
		gameNum++;
		if (gameNum > 4) {
			Appszoom.showAd(this);
			gameNum = 0;
		}
		SharedPreferences.Editor edit = highscores.edit();
		edit.putInt("gamenum", gameNum);
		edit.commit();
	}

	public void playAgain(View view) {
		Intent intent = new Intent(this, AndroidLauncher.class);
		intent.putExtra("gamemode", mode);
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	public void back(View view) {
		Intent intent = new Intent(this, StartActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

	@Override
	public void onBackPressed() {
		back(new View(this));
	}
}

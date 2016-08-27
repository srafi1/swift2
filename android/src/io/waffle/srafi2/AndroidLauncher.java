package io.waffle.srafi2;

import android.content.Intent;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication implements GameOver {
	private int mode;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		mode = getIntent().getIntExtra("gamemode", 0);

		switch (mode) {
			case 0:
				initialize(new Classic(this), config);
				break;
			case 1:
				initialize(new Sight(this), config);
				break;
			case 2:
				initialize(new Snowball(this), config);
				break;
			case 3:
				initialize(new Uphill(this), config);
				break;
			case 4:
				initialize(new Feed(this), config);
				break;
			case 5:
				initialize(new Scroll(this), config);
				break;
			case 6:
				initialize(new Jump(), config);
				break;
		}
	}

	public void die(int score) {
		Intent intent = new Intent(this, GameOverActivity.class);
		intent.putExtra("mode", mode);
		intent.putExtra("score", score);
		startActivity(intent);
	}

	@Override
	public  void onBackPressed() {

	}
}

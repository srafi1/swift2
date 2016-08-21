package io.waffle.srafi2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		setDisplays();
	}

	private void setDisplays() {
		SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);

		Button control;
		int ctrl = settings.getInt("controls", 0);
		if (ctrl == 0) control = (Button) findViewById(R.id.touch);
		else control = (Button) findViewById(R.id.tilt);
		control.setTextColor(Color.BLACK);
		control.setBackgroundColor(Color.WHITE);
		if (ctrl == 0) control = (Button) findViewById(R.id.tilt);
		else control = (Button) findViewById(R.id.touch);
		control.setTextColor(Color.WHITE);
		control.setBackgroundColor(Color.BLACK);

		TextView colorTheme = (TextView) findViewById(R.id.color);
		int clr = settings.getInt("colorTheme", 0);
		switch (clr) {
			case 0:
				colorTheme.setTextColor(Color.BLUE);
				break;
			case 1:
				colorTheme.setTextColor(Color.RED);
				break;
			case 2:
				colorTheme.setTextColor(Color.YELLOW);
				break;
			case 3:
				colorTheme.setTextColor(Color.parseColor("#ffa500"));
				break;
			case 4:
				colorTheme.setTextColor(Color.GREEN);
				break;
			case 5:
				colorTheme.setTextColor(Color.parseColor("#800080"));
				break;
		}
	}

	public void setTouch(View view) {
		SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt("controls", 0);
		edit.commit();
		setDisplays();
	}

	public void setTilt(View view) {
		SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt("controls", 1);
		edit.commit();
		setDisplays();
	}

	public void setBlue(View view) {
		SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt("colorTheme", 0);
		edit.commit();
		setDisplays();
	}

	public void setRed(View view) {
		SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt("colorTheme", 1);
		edit.commit();
		setDisplays();
	}

	public void setYellow(View view) {
		SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt("colorTheme", 2);
		edit.commit();
		setDisplays();
	}

	public void setOrange(View view) {
		SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt("colorTheme", 3);
		edit.commit();
		setDisplays();
	}

	public void setGreen(View view) {
		SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt("colorTheme", 4);
		edit.commit();
		setDisplays();
	}

	public void setPurple(View view) {
		SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt("colorTheme", 5);
		edit.commit();
		setDisplays();
	}

	public void mainMenu(View view) {
		Intent intent = new Intent(this, StartActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	@Override
	public void onBackPressed() {
		mainMenu(new View(this));
	}
}

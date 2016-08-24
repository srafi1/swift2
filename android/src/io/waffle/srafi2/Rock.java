package io.waffle.srafi2;

import com.badlogic.gdx.Gdx;

public class Rock {
	public float x, y, screenWidth;
	public int type;
	public boolean hit;

	public Rock () {
		screenWidth = Gdx.graphics.getWidth();
		x = (float) (Math.random()*screenWidth);
		y = Gdx.graphics.getHeight()*1.2f;
		type = (int)(Math.random()*3);
		hit = false;
	}

	public void move(float speed) {
		y -= speed;
	}
}

package io.waffle.srafi2;

import com.badlogic.gdx.Gdx;

public class Obstacle {
	public float x, y, screenWidth;
	public int type, xdir;

	public Obstacle () {
		screenWidth = Gdx.graphics.getWidth();
		x = (float) (Math.random()*screenWidth*10/12f) + screenWidth/12;
		y = Gdx.graphics.getHeight()*1.2f;
		type = (int)(Math.random()*6) - 2;
		xdir = (int) Math.round(Math.random());
		if (xdir == 0) xdir = -1;
	}

	public void move(float speed, float ballX, boolean ymv) {
		if (ymv) y -= speed;

		if (type == 1) {
			x += speed*xdir*3/4f;
			if (x < 0 || x > screenWidth) xdir *= -1;
		} else if (type == 2) {
			if (x > ballX + speed) x -= speed/3;
			if (x < ballX - speed) x += speed/3;
		}
	}
}

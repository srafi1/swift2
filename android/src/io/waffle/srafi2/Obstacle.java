package io.waffle.srafi2;

import com.badlogic.gdx.Gdx;

class Obstacle {
	float x, y, screenWidth;
	int type, xdir;
	boolean eaten;

	Obstacle() {
		screenWidth = Gdx.graphics.getWidth();
		x = (float) (Math.random()*screenWidth*10/12f) + screenWidth/12;
		y = Gdx.graphics.getHeight()*1.2f;
		type = (int)(Math.random()*6) - 2;
		xdir = (int) Math.round(Math.random());
		if (xdir == 0) xdir = -1;
		eaten = false;
	}

	void move(float speed, float ballX, boolean ymv) {
		if (ymv) y -= speed;

		if (type == 1) {
			x += speed*xdir*3/4f;
			if (x < screenWidth/12 || x > screenWidth*11/12) xdir *= -1;
		} else if (type == 2) {
			if (x > ballX + speed) x -= speed/3;
			if (x < ballX - speed) x += speed/3;
		}
	}

	void move(float speed, float ballX) {
		y -= speed;

		if (type == 1) {
			x += speed*xdir*3/4f;
			if (x < screenWidth/12 || x > screenWidth*11/12) xdir *= -1;
		} else if (type == 2) {
			if (x > ballX + speed) x += speed/3;
			else if (x < ballX - speed) x -= speed/3;
			if (x < screenWidth/12) x = screenWidth/12;
			else if (x > screenWidth*11/12) x = screenWidth*11/12;
		}
	}
}

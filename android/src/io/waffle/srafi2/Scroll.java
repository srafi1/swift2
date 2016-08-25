package io.waffle.srafi2;

import com.badlogic.gdx.Gdx;

public class Scroll extends Classic {
	private int dir;
	private float initialTouchY, ydiff;

	Scroll(GameOver go) {
		super(go);
	}

	@Override
	public void create() {
		super.create();

		for (int i = 0; i < 3; i++) {
			obstacles.add(new Obstacle());
			obstacles.get(i).y += screenHeight/3*(i - 1);
		}

		dir = 1;
		ydiff = 0;
	}

	@Override
	public void update() {
		updateRealSpeed();
		moveBall();

		for (int i = 0; i < obstacles.size(); i++) {
			obstacles.get(i).move(realSpeed, ballX, false);
			obstacles.get(i).y -= ydiff;
		}

		if (obstacles.size() == 0) return;

		if (dist(obstacles.get(0).x, obstacles.get(0).y, ballX, ballY) < ballRadius*2) end.die(score);
		if (obstacles.get(0).y < 0 - base) {
			obstacles.remove(0);
			score++;
			obstacles.add(new Obstacle());
		}
	}

	@Override
	public void moveBall() {
		if (ballX <= ballRadius || ballX >= screenWidth - ballRadius) dir*=-1;
		ballX += realSpeed*dir*0.8;

		if (inputType == 0 && initialTouchY < touchY && touchY >= 0) {
			ydiff = touchY - initialTouchY;
			initialTouchY += ydiff;
		} else if (inputType == 1 && Gdx.input.getAccelerometerZ() > 0) {
			ydiff = realSpeed*Gdx.input.getAccelerometerZ()/10;
		} else ydiff = 0;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		super.touchDown(screenX, screenY, pointer, button);
		initialTouchY = screenY;

		return inputType == 0;
	}
}

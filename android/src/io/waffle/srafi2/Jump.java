package io.waffle.srafi2;

import com.badlogic.gdx.Gdx;

public class Jump extends Classic {
	private double grav;

	Jump(GameOver go) {
		super(go);
	}

	@Override
	public void create() {
		super.create();

		grav = 0f;
		for (int i = 0; i < 3; i++) {
			obstacles.add(new Obstacle());
			obstacles.get(i).y += screenHeight/3*i;
		}
	}

	@Override
	public void update() {
		updateRealSpeed();
		if (realSpeed == 0) return;
		if (grav < -2 || grav > 2) grav = 0;
		moveBall();

		for (int i = 0; i < obstacles.size(); i++) obstacles.get(i).move(realSpeed, ballX, false);

		if (ballY < 0) end.die(score);

		Obstacle firstOb = obstacles.get(0);
		if (dist(firstOb.x, firstOb.y, ballX, ballY) < ballRadius*2) end.die(score);
		if (firstOb.y < 0 - base) {
			obstacles.remove(0);
			score++;
			obstacles.add(new Obstacle());
		}
		grav += 2f / Gdx.graphics.getFramesPerSecond();
	}

	@Override
	public void moveBall() {
		super.moveBall();

		if (ballY - grav*realSpeed > screenHeight/4) for (int i = 0; i < obstacles.size(); i++) obstacles.get(i).y += grav*realSpeed;
		else ballY -= grav*realSpeed;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (screenX > screenWidth - 150 && screenY < 150) {
			paused = !paused;
			nextSpawn = totalTime + 1 - score/1000f;
			return true;
		}

		touchX = screenX;
		touchY = screenY;

		grav = -1.2;

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
}

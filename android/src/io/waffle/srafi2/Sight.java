package io.waffle.srafi2;

public class Sight extends Classic {
	private boolean down;

	public Sight(GameOver go) {
		super(go);
	}

	@Override
	public void create() {
		super.create();
		down = false;
		spawn();
	}

	@Override
	public void update() {
		updateRealSpeed();
		moveBall();

		if (ballY > screenHeight - ballRadius) down = true;
		else if (ballY <= ballRadius) {
			down = false;
			spawn();
		}

		if (!down) for (int i = 0; i < obstacles.size(); i++) obstacles.get(i).move(realSpeed, ballX, false);
		if (down) for (int i = 0; i < obstacles.size(); i++) obstacles.get(i).y -= realSpeed;
		if (obstacles.get(0).y < 0 - ballRadius) {
			obstacles.remove(0);
			score++;
		}

		for (int i = 0; i < obstacles.size(); i++) if (dist(obstacles.get(i).x, obstacles.get(i).y, ballX, ballY) < ballRadius*2) end.die(score);

		if (down) ballY -= realSpeed;
	}

	@Override
	public void moveBall() {
		super.moveBall();
		if (!down) ballY += realSpeed;
	}

	@Override
	public void spawn() {
		for (int i = 0; i < 3; i++) obstacles.add(new Obstacle());
		for (int i = 0; i < 3; i++) {
			obstacles.get(obstacles.size() - 3 + i).y += screenHeight / 3 * i;
//			if (obstacles.get(obstacles.size() - 3 + i).type == 3) obstacles.get(obstacles.size() - 3 + i).type = 0;
		}
	}
}

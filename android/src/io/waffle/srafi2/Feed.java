package io.waffle.srafi2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

class Feed extends Classic {
	private int energy;
	private Texture cookie, cake, icecream, bomb, batt_5, batt_4, batt_3, batt_2, batt_1;
	private TextureAtlas pacman_atlas;
	private Animation pacman;

	Feed(GameOver go) {
		super(go);
	}

	@Override
	public void create() {
		super.create();

		energy = 5;
		cookie = new Texture(Gdx.files.internal("cookie.png"));
		cake = new Texture(Gdx.files.internal("cake.png"));
		icecream = new Texture(Gdx.files.internal("icecream.png"));
		bomb = new Texture(Gdx.files.internal("badlogic.jpg"));

		pacman_atlas = new TextureAtlas(Gdx.files.internal("pacman.atlas"));
		pacman = new Animation(1/24f, pacman_atlas.getRegions());

		batt_1 = new Texture(Gdx.files.internal("batt_1.png"));
		batt_2 = new Texture(Gdx.files.internal("batt_2.png"));
		batt_3 = new Texture(Gdx.files.internal("batt_3.png"));
		batt_4 = new Texture(Gdx.files.internal("batt_4.png"));
		batt_5 = new Texture(Gdx.files.internal("batt_5.png"));
	}

	@Override
	public void render() {
		if (!paused) update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);batch.begin();
		totalTime += Gdx.graphics.getDeltaTime();

		batch.draw(pacman.getKeyFrame(totalTime, true), ballX - base, ballY - base, ballRadius*2, ballRadius*2l);

		for (int i = 0; i < obstacles.size(); i++) {
			switch (obstacles.get(i).type) {
				case -2:
					if (!obstacles.get(i).eaten) batch.draw(cookie, obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					break;
				case -1:
					if (!obstacles.get(i).eaten) batch.draw(cookie, obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					break;
				case 0:
					if (!obstacles.get(i).eaten) batch.draw(cookie, obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					break;
				case 1:
					if (!obstacles.get(i).eaten) batch.draw(icecream, obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					break;
				case 2:
					if (!obstacles.get(i).eaten) batch.draw(cake, obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					break;
				case 3:
					if (!obstacles.get(i).eaten) batch.draw(bomb, obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					break;
			}
		}
		font.draw(batch, "" + score, 20, screenHeight - 20);
		if (!paused) batch.draw(pause, screenWidth - 150, screenHeight - 150, 150, 150);
		else batch.draw(play, screenWidth - 150, screenHeight - 150, 150, 150);

		switch (energy) {
			case 1:
				batch.draw(batt_1, screenWidth - 150, screenHeight - 460, 140, 300);
				break;
			case 2:
				batch.draw(batt_2, screenWidth - 150, screenHeight - 460, 140, 300);
				break;
			case 3:
				batch.draw(batt_3, screenWidth - 150, screenHeight - 460, 140, 300);
				break;
			case 4:
				batch.draw(batt_4, screenWidth - 150, screenHeight - 460, 140, 300);
				break;
			case 5:
				batch.draw(batt_5, screenWidth - 150, screenHeight - 460, 140, 300);
				break;
		}

		batch.end();
	}

	@Override
	public void update() {
		updateRealSpeed();
		if (totalTime >= nextSpawn) spawn();
		moveBall();

		for (int i = 0; i < obstacles.size(); i++) obstacles.get(i).move(realSpeed, ballX);

		if (obstacles.size() == 0) return;

		Obstacle firstOb = obstacles.get(0);
		if (dist(firstOb.x, firstOb.y, ballX, ballY) < ballRadius*2 && !firstOb.eaten) {
			if (firstOb.type == 3) end.die(score);
			score++;
			obstacles.get(0).eaten = true;
		}
		if (firstOb.y < 0 - base) {
			obstacles.remove(0);
			if (!firstOb.eaten && firstOb.type < 3) energy--;
			if (energy == 0) end.die(score);
		}
	}
}

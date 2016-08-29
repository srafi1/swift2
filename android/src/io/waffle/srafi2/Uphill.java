package io.waffle.srafi2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Uphill extends Snowball {
	private float grow;

	public Uphill(GameOver go) {
		super(go);
		grow = 0;
	}

	@Override
	public void render() {
		if (!paused) update();

		Gdx.gl.glClearColor(179/255f, 229/255f, 252/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ballRenderer.begin(ShapeRenderer.ShapeType.Filled);
		ballRenderer.setColor(ballColor);
		ballRenderer.circle(ballX, ballY, ballRadius);
		for (int i = 0; i < rocks.size(); i++) {
			Rock rock = rocks.get(i);
			if (rock.type == 2 && !rock.hit) ballRenderer.circle(rock.x, rock.y, base/2);
		}
		ballRenderer.end();

		totalTime += Gdx.graphics.getDeltaTime();

		batch.begin();
		for (int i = 0; i < rocks.size(); i++) {
			Rock rock = rocks.get(i);
			if (rock.type < 2) batch.draw(blackRock, rock.x - base, rock.y - base, base*2, base*2);
		}

		font.draw(batch, "" + score, 20, screenHeight - 20);
		if (!paused) batch.draw(pause, screenWidth - 150, screenHeight - 150, 150, 150);
		else batch.draw(play, screenWidth - 150, screenHeight - 150, 150, 150);
		batch.end();
	}

	@Override
	public void update() {
		updateRealSpeed();
		if (realSpeed == 0) return;
		if (totalTime >= nextRockSpawn) spawnRock();
		moveBall();

		for (int i = 0; i < rocks.size(); i++) rocks.get(i).move(realSpeed);

		if (rocks.size() == 0) return;

		Rock firstRock = rocks.get(0);
		if (!firstRock.hit) {
			if (firstRock.type == 2 && dist(firstRock.x, firstRock.y, ballX, ballY) <= ballRadius + base / 2) {
				if (ballRadius > base) grow += ballRadius * 0.1;
				else grow += base *0.1;
				score++;
				rocks.get(0).hit = true;
			} else if (firstRock.type < 2 && dist(firstRock.x, firstRock.y, ballX, ballY) <= ballRadius + base) {
				end.die(score);
				rocks.get(0).hit = true;
			}
		}

		if (firstRock.y <= 0 - base) {
			rocks.remove(0);
			score++;
		}

		if (grow > 0) {
			ballRadius++;
			grow--;
		} else {
			float mult = (float) (1 - 0.04/ Gdx.graphics.getFramesPerSecond());
			if (mult < 0.95) mult = 1;
			ballRadius *= mult;
		}

		if (ballRadius <= 1) end.die(score);
	}
}

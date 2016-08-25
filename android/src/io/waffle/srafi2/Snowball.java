package io.waffle.srafi2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Snowball extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	float ballX, ballY, ballRadius, base, touchX, touchY, speed, realSpeed, screenWidth, screenHeight;
	Color ballColor;
	ShapeRenderer ballRenderer;
	float totalTime = 0, nextRockSpawn;
	int inputType, score;
	ArrayList<Rock> rocks;
	BitmapFont font;
	boolean paused;
	Texture pause, play, greyRock, blackRock;
	GameOver end;

	Snowball(GameOver go) {
		this.end = go;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();

		Preferences settings = Gdx.app.getPreferences("settings");

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		base = screenWidth/12;
		ballX = base*6;
		ballY = base*2;
		ballRadius = base;
		switch (settings.getInteger("colorTheme", 0)) {
			case 0:
				ballColor = Color.BLUE;
				break;
			case 1:
				ballColor = Color.RED;
				break;
			case 2:
				ballColor = Color.YELLOW;
				break;
			case 3:
				ballColor = Color.ORANGE;
				break;
			case 4:
				ballColor = Color.GREEN;
				break;
			case 5:
				ballColor = Color.PURPLE;
				break;
		}

		ballRenderer = new ShapeRenderer();

		inputType = settings.getInteger("controls", 1);
		speed = 12*base; //movement in one second
		score = 0;

		rocks = new ArrayList<>(0);
		nextRockSpawn = 0.5f;

		paused = false;
		pause = new Texture(Gdx.files.internal("pause_dark.png"));
		play = new Texture(Gdx.files.internal("play_dark.png"));
		greyRock = new Texture(Gdx.files.internal("rock_grey.png"));
		blackRock = new Texture(Gdx.files.internal("rock_black.png"));

		try {
			font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
		} catch (Exception e){
			e.printStackTrace();
			font = new BitmapFont();
		}
		font.setColor(Color.BLACK);

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		if (!paused) update();

		Gdx.gl.glClearColor(179/255f, 229/255f, 252/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ballRenderer.begin(ShapeRenderer.ShapeType.Filled);
		ballRenderer.setColor(ballColor);
		ballRenderer.circle(ballX, ballY, ballRadius);
		ballRenderer.end();

		totalTime += Gdx.graphics.getDeltaTime();

		batch.begin();
		for (int i = 0; i < rocks.size(); i++) {
			Rock rock = rocks.get(i);
			if (rock.type < 2) batch.draw(greyRock, rock.x - base, rock.y - base, base*2, base*2);
			else batch.draw(blackRock, rock.x - base, rock.y - base, base*2, base*2);
		}
		font.draw(batch, "" + score, 20, screenHeight - 20);
		if (!paused) batch.draw(pause, screenWidth - 150, screenHeight - 150, 150, 150);
		else batch.draw(play, screenWidth - 150, screenHeight - 150, 150, 150);
		batch.end();
	}

	void update() {
		updateRealSpeed();
		if (totalTime >= nextRockSpawn) spawnRock();
		moveBall();

		for (int i = 0; i < rocks.size(); i++) rocks.get(i).move(realSpeed);

		if (rocks.size() == 0) return;

		Rock firstRock = rocks.get(0);
		if (dist(firstRock.x, firstRock.y, ballX, ballY) <= ballRadius + base && !rocks.get(0).hit) {
			if (firstRock.type == 2 || ballRadius <= base*.2) end.die(score);
			if (firstRock.type < 2 && !firstRock.hit) {
				if (ballRadius > base) ballRadius *= 0.9;
				else ballRadius -= base*.1;
			}
			rocks.get(0).hit = true;
		}

		if (firstRock.y <= 0 - base) {
			rocks.remove(0);
			score++;
		}

		float mult = (float) (1 + 0.05/Gdx.graphics.getFramesPerSecond());
		if (mult > 1.1) mult = 1;
		ballRadius *= mult;
	}

	void spawnRock() {
		rocks.add(new Rock());
		if (score < 500) nextRockSpawn = totalTime +  0.7f - score/1000f;
		else nextRockSpawn = totalTime + 0.3f;
	}

	void moveBall() {
		if (inputType == 0 && touchX > 0) {
			if (ballX > touchX + 5) ballX -= realSpeed;
			else if (ballX < touchX - 5) ballX += realSpeed;
		} else if (inputType == 1) {
			float maxspd = realSpeed;
			float spd = realSpeed*Gdx.input.getAccelerometerX()/3;
			if (spd > maxspd) spd = maxspd;
			if (spd < maxspd*-1) spd = maxspd*-1;
			ballX -= spd;
		}

		if (ballX <= ballRadius) ballX = ballRadius;
		else if (ballX >= Gdx.graphics.getWidth() - ballRadius) ballX = Gdx.graphics.getWidth() - ballRadius;
		if (Float.isNaN(ballX)) ballX = base*6;
	}

	void updateRealSpeed() {
		realSpeed = speed/Gdx.graphics.getFramesPerSecond();
		if (realSpeed > speed) realSpeed = speed/60;
	}

	@Override
	public void dispose () {
		batch.dispose();
		ballRenderer.dispose();
		font.dispose();
		pause.dispose();
		play.dispose();
	}

	double dist(float x1, float y1, float x2, float y2) {
		return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
	}

	@Override
	public void resume() {
		super.resume();
		nextRockSpawn = totalTime +  1 - score/1000f;
	}

	@Override
	public void pause() {
		super.pause();
		paused = true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchX = screenX;
		touchY = screenY;

		if (screenX > screenWidth - 150 && screenY < 150) {
			paused = !paused;
		}

		return inputType == 0;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touchX = -1;
		touchY = -1;
		return inputType == 0;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		touchX = screenX;
		touchY = screenY;
		return inputType == 0;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}

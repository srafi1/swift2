package io.waffle.srafi2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Classic extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;
	public float ballX, ballY, ballRadius, base, touchX, touchY, speed, realSpeed, screenWidth, screenHeight;
	private Color ballColor;
	private ShapeRenderer ballRenderer;
	public float totalTime = 0, nextSpawn;
	private TextureAtlas red_atlas, pink_atlas, pink_atlas_flip, green_atlas, purple_atlas;
	private Animation red_anim, pink_anim, pink_anim_flip, green_anim, purple_anim;
	public int inputType, score;
	public ArrayList<Obstacle> obstacles;
	private BitmapFont font;
	private boolean paused;
	private Texture pause, play;
	public GameOver end;

	public Classic(GameOver go) {
		super();
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
		red_atlas = new TextureAtlas(Gdx.files.internal("red_spin.atlas"));
		red_anim = new Animation(1/24f, red_atlas.getRegions());
		pink_atlas = new TextureAtlas(Gdx.files.internal("pink_spin.atlas"));
		pink_anim = new Animation(1/24f, pink_atlas.getRegions());
		pink_atlas_flip = new TextureAtlas(Gdx.files.internal("pink_spin_flip.atlas"));
		pink_anim_flip = new Animation(1/24f, pink_atlas_flip.getRegions());
		green_atlas = new TextureAtlas(Gdx.files.internal("green_spin.atlas"));
		green_anim = new Animation(1/24f, green_atlas.getRegions());
		purple_atlas = new TextureAtlas(Gdx.files.internal("purple_spin.atlas"));
		purple_anim = new Animation(1/24f, purple_atlas.getRegions());

		inputType = settings.getInteger("controls", 1);
		speed = 12*base; //movement in one second
		score = 0;

		obstacles = new ArrayList<>(0);
		nextSpawn = 0.5f;

		paused = false;
		pause = new Texture(Gdx.files.internal("pause.png"));
		play = new Texture(Gdx.files.internal("play.png"));

		try {
			font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
		} catch (Exception e){
			e.printStackTrace();
			font = new BitmapFont();
		}

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		if (!paused) update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ballRenderer.begin(ShapeRenderer.ShapeType.Filled);
		ballRenderer.setColor(ballColor);
		ballRenderer.circle(ballX, ballY, ballRadius);
		ballRenderer.end();

		batch.begin();
		totalTime += Gdx.graphics.getDeltaTime();
		for (int i = 0; i < obstacles.size(); i++) {
			switch (obstacles.get(i).type) {
				case -2:
					batch.draw(red_anim.getKeyFrame(totalTime, true), obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					break;
				case -1:
					batch.draw(red_anim.getKeyFrame(totalTime, true), obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					break;
				case 0:
					batch.draw(red_anim.getKeyFrame(totalTime, true), obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					break;
				case 1:
					if (obstacles.get(i).xdir > 0) batch.draw(pink_anim.getKeyFrame(totalTime, true), obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					else batch.draw(pink_anim_flip.getKeyFrame(totalTime, true), obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					break;
				case 2:
					batch.draw(green_anim.getKeyFrame(totalTime, true), obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					break;
				case 3:
					float currY = obstacles.get(i).y;
					if (currY < screenHeight/4 || currY > screenHeight - 5*base) batch.draw(purple_anim.getKeyFrame(totalTime, true), obstacles.get(i).x - base, obstacles.get(i).y - base, base*2, base*2);
					break;
			}
		}
		font.draw(batch, "" + score, 20, screenHeight - 20);
		if (!paused) batch.draw(pause, screenWidth - 150, screenHeight - 150, 150, 150);
		else batch.draw(play, screenWidth - 150, screenHeight - 150, 150, 150);
		batch.end();
	}

	public void update() {
		updateRealSpeed();
		if (totalTime >= nextSpawn) spawn();
		moveBall();

		for (int i = 0; i < obstacles.size(); i++) obstacles.get(i).move(realSpeed, ballX, true);

		if (obstacles.size() == 0) return;

		if (dist(obstacles.get(0).x, obstacles.get(0).y, ballX, ballY) < ballRadius*2) end.die(score);
		if (obstacles.get(0).y < 0 - base) {
			obstacles.remove(0);
			score++;
		}
	}

	public void updateRealSpeed() {
		realSpeed = speed/Gdx.graphics.getFramesPerSecond();
		if (realSpeed > speed) realSpeed = speed/60;
	}

	@Override
	public void resume() {
		super.resume();
		nextSpawn = totalTime +  1 - score/1000f;
	}

	@Override
	public void pause() {
		super.pause();
		paused = true;
	}

	public double dist(float x1, float y1, float x2, float y2) {
		return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
	}

	public void spawn() {
		obstacles.add(new Obstacle());
		if (score < 500) nextSpawn = totalTime +  0.7f - score/1000f;
		else nextSpawn = totalTime + 0.3f;
	}

	public void moveBall() {
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

	@Override
	public void dispose () {
		batch.dispose();
		ballRenderer.dispose();
		red_atlas.dispose();
		pink_atlas.dispose();
		green_atlas.dispose();
		purple_atlas.dispose();
		font.dispose();
		pause.dispose();
		play.dispose();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchX = screenX;
		touchY = screenY;

		if (screenX > screenWidth - 150 && screenY < 150) {
			if (paused) paused = false;
			else paused = true;
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

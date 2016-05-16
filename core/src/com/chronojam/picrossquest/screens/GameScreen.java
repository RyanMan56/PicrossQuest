package com.chronojam.picrossquest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chronojam.picrossquest.PicrossQuest;
import com.chronojam.picrossquest.entity.Player;
import com.chronojam.picrossquest.environment.Grass;
import com.chronojam.picrossquest.services.ImageProvider;

public class GameScreen implements Screen {
	private PicrossQuest game;
	private AssetManager assetManager;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private Viewport viewport;

	private Player player;
	private Grass grass[][];

	public GameScreen(PicrossQuest game, AssetManager assetManager) {
		this.game = game;
		this.assetManager = assetManager;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, ImageProvider.SCREEN_WIDTH, ImageProvider.SCREEN_HEIGHT);
		viewport = new FitViewport(ImageProvider.SCREEN_WIDTH, ImageProvider.SCREEN_HEIGHT, camera);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		player = new Player(assetManager, 50, 50);
		grass = new Grass[(int) Math.ceil(ImageProvider.SCREEN_WIDTH/60f)][(int) (Math.ceil(ImageProvider.SCREEN_HEIGHT/60f))];
		for(int i = 0; i < grass.length; i++)
			for(int j = 0; j < grass[i].length; j++)
			grass[i][j] = new Grass(i*60, j*60, assetManager);
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		batch.begin();
		for (int i = 0; i < grass.length; i++)
			for (int j = 0; j < grass[i].length; j++)
				grass[i][j].render(batch);
		//		batch.draw(floor, 0, 0, floor.getWidth()*2, floor.getHeight()*2);
		player.render(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

}

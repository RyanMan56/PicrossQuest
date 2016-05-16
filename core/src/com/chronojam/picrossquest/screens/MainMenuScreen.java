package com.chronojam.picrossquest.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.chronojam.picrossquest.PicrossQuest;

public class MainMenuScreen implements Screen {
	private PicrossQuest game;
	private AssetManager assetManager;
	private PicrossScreen picrossScreen;
	private GameScreen gameScreen;
	private boolean changedScreen = false;

	public MainMenuScreen(PicrossQuest game, AssetManager assetManager) {
		this.game = game;
		this.assetManager = assetManager;
		picrossScreen = new PicrossScreen(game, assetManager, this, "Main");
		gameScreen = new GameScreen(game, assetManager);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		if (!changedScreen) {
			game.setScreen(gameScreen);
			changedScreen = true;
		}
	}

	@Override
	public void resize(int width, int height) {

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

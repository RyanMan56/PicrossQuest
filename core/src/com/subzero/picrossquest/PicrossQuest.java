package com.subzero.picrossquest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.subzero.picrossquest.screens.GameScreen;
import com.subzero.picrossquest.screens.MainMenuScreen;

public class PicrossQuest extends Game {
	private MainMenuScreen mainMenuScreen;
	private GameScreen gameScreen;
	private AssetManager assetManager;
	private boolean loaded = false;
	
	@Override
	public void create () {
		assetManager = new AssetManager();
		
		assetManager.load("Nikola.png", Texture.class);
		assetManager.load("NikolaTemplate.png", Texture.class);
		assetManager.load("Happy.png", Texture.class);
		assetManager.load("HappyTemplate.png", Texture.class);
		assetManager.load("Square.png", Texture.class);
		assetManager.load("Cross.png", Texture.class);
		assetManager.load("Background.png", Texture.class);
		assetManager.load("Glint.png", Texture.class);
		assetManager.load("EndGlint.png", Texture.class);
		assetManager.load("EndGlintSide.png", Texture.class);
		assetManager.load("BattleCat.png", Texture.class);
		assetManager.load("BattleCatTemplate.png", Texture.class);
		assetManager.load("PenButton.png", Texture.class);
		assetManager.load("PenButtonSelected.png", Texture.class);
		assetManager.load("CrossButton.png", Texture.class);
		assetManager.load("CrossButtonSelected.png", Texture.class);
	}

	@Override
	public void render () {
		super.render();
		if(!loaded){
			if(assetManager.update()){
				mainMenuScreen = new MainMenuScreen(this, assetManager);
				setScreen(mainMenuScreen);
				loaded = true;
			}
		}
	}
}

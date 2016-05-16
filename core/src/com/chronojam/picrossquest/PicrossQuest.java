package com.chronojam.picrossquest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.chronojam.picrossquest.screens.PicrossScreen;
import com.chronojam.picrossquest.screens.MainMenuScreen;

public class PicrossQuest extends Game {
	private MainMenuScreen mainMenuScreen;
	private PicrossScreen gameScreen;
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
		assetManager.load("Ryan.png", Texture.class);
		assetManager.load("RyanTemplate.png", Texture.class);
		assetManager.load("Main.png", Texture.class);
		assetManager.load("MainTemplate.png", Texture.class);
		assetManager.load("Serpa.png", Texture.class);
		assetManager.load("SerpaTemplate.png", Texture.class);
		assetManager.load("MainWalkAnim.png", Texture.class);
		assetManager.load("Floor.png", Texture.class);
		assetManager.load("FloorAesthetic.png", Texture.class);
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

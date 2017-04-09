package com.chronojam.picrossquest.environment;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Dirt extends Tile {

	public Dirt(float x, float y, AssetManager assetManager) {
		super(x, y, assetManager);
		texture = assetManager.get("Dirt.png", Texture.class);

	}

}

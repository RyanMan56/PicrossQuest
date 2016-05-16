package com.chronojam.picrossquest.environment;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Grass extends Tile{
	
	public Grass(float x, float y, AssetManager assetManager){
		this.x = x;
		this.y = y;
		this.assetManager = assetManager;
		this.texture = assetManager.get("Floor.png", Texture.class);
				
	}

}

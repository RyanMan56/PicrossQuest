package com.chronojam.picrossquest.environment;

import com.badlogic.gdx.assets.AssetManager;

public class Tile extends Environment{
	
	public Tile(float x, float y, AssetManager assetManager){
		this.x = x;
		this.y = y;
		this.assetManager = assetManager;
	}

}

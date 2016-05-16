package com.chronojam.picrossquest.environment;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Environment {
	protected float x, y;
	protected Texture texture;
	protected boolean animated = false;
	protected AssetManager assetManager;
	protected float scale = 2;
	
	public void render(SpriteBatch batch){
		batch.draw(texture, x, y, texture.getWidth()*scale, texture.getHeight()*scale);
	}
	
	public Texture getTexture(){
		return texture;
	}

}

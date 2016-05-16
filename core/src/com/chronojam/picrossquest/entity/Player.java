package com.chronojam.picrossquest.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Player extends Entity{
	private float speed = 1;
	private float scale = 2;
	
	public Player(AssetManager assetManager, float x, float y){
		this.assetManager = assetManager;
		this.x = x;
		this.y = y;
		looping = true;
		
		textureRegion = new TextureRegion(assetManager.get("MainWalkAnim.png", Texture.class));
		TextureRegion[][] allFrames;
		allFrames = textureRegion.split(24, 40);
		animatedTextures = new TextureRegion[allFrames.length*allFrames[0].length];
		int count = 0;
		for(int i = 0; i < allFrames.length; i++)
			for(int j = 0; j < allFrames[i].length; j++)
				animatedTextures[count++] = allFrames[i][j];
		animation = new Animation(period, animatedTextures);
		animation.setPlayMode(PlayMode.LOOP);
		sprite = new Sprite(animation.getKeyFrame(elapsedTime, looping));
		sprite.setScale(scale);
		sprite.setPosition(x, y);
		bounds = new Rectangle(x, y, 24*scale, 40*scale);
	}
	
	@Override
	public void render(SpriteBatch batch){
		super.render(batch);
		
		dx = 0;
		dy = 0;
		walking = false;
		
		if(health > 0){
			if(Gdx.input.isKeyPressed(Keys.A)){
				walking = true;
				lastDirection = LEFT;
				dx -= speed;
			}
			if(Gdx.input.isKeyPressed(Keys.D)){
				walking = true;
				lastDirection = RIGHT;
				dx += speed;
			}
			if(Gdx.input.isKeyPressed(Keys.W)){
				walking = true;
				dy += speed;
			}
			if(Gdx.input.isKeyPressed(Keys.S)){
				walking = true;
				dy -= speed;
			}
			
			
		}
	}
	
}

package com.chronojam.picrossquest.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Entity {
	protected AssetManager assetManager;
	protected TextureRegion textureRegion;
	protected TextureRegion[] animatedTextures;
	protected float period = 1 / 10f;
	protected float elapsedTime = 0;
	protected Animation<TextureRegion> animation;
	protected Sprite sprite;
	protected float health = 100;
	protected float x, y, oldX, oldY;
	protected float speed = 2;
	protected float dx, dy;
	protected Rectangle bounds;
	protected TextureRegion region;
	protected boolean looping = true;
	protected boolean walking = false;
	protected final int LEFT = -1, RIGHT = 1;
	protected int lastDirection = RIGHT;
	protected boolean canMoveRight = true, canMoveLeft = true, canMoveUp = true, canMoveDown = true;

	public void render(SpriteBatch batch) {
		oldX = x;
		oldY = y;
		
		x += dx;
		y += dy;
		bounds.x = x;
		bounds.y = y;

		if (health > 0) {
			if (walking) {
				elapsedTime += Gdx.graphics.getDeltaTime();
			} else {
//				elapsedTime = 0;
			}
			if (elapsedTime > 0 || region == null)
				region = animation.getKeyFrame(elapsedTime, looping);
			if (dx < 0 || lastDirection == LEFT) {
				if (region.isFlipX())
					region.flip(true, false);
			} else if (dx > 0 || lastDirection == RIGHT)
				if (!region.isFlipX())
					region.flip(true, false);
			sprite.setRegion(region);
		}
		sprite.setPosition(x, y);
		sprite.draw(batch);
	}
	
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	
	public float getOldX(){
		return oldX;
	}
	public float getOldY(){
		return oldY;
	}
	
	public float getWidth(){
		return bounds.width;
	}
	public float getHeight(){
		return bounds.height;
	}
	
	public Rectangle getBounds(){
		return bounds;
	}
	
	public float getSpeed(){
		return speed;
	}
	
	public void setPos(float x, float y){
		this.x = x;
		this.y = y;
	}
	public void setX(float x){
		this.x = x;
	}
	public void setY(float y){
		this.y = y;
	}
	
	public void setCanMoveRight(boolean canMoveRight){
		this.canMoveRight = canMoveRight;
	}
	public void setCanMoveLeft(boolean canMoveLeft){
		this.canMoveLeft = canMoveLeft;
	}
	public void setCanMoveUp(boolean canMoveUp){
		this.canMoveUp = canMoveUp;
	}
	public void setCanMoveDown(boolean canMoveDown){
		this.canMoveDown = canMoveDown;
	}

}

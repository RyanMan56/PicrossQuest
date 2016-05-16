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
	protected Animation animation;
	protected Sprite sprite;
	protected float health = 100;
	protected float x, y;
	protected float dx, dy;
	protected Rectangle bounds;
	protected TextureRegion region;
	protected boolean looping = true;
	protected boolean walking = false;
	protected final int LEFT = -1, RIGHT = 1;
	protected int lastDirection = RIGHT;

	public void render(SpriteBatch batch) {
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

}

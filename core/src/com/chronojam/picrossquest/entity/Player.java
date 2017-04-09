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

public class Player extends Entity {
	private float scale = 2;

	public Player(AssetManager assetManager, float x, float y) {
		this.assetManager = assetManager;
		this.x = x;
		this.y = y;
		looping = true;

		textureRegion = new TextureRegion(assetManager.get("MainWalkAnim.png", Texture.class));
		TextureRegion[][] allFrames;
		allFrames = textureRegion.split(24, 40);
		animatedTextures = new TextureRegion[allFrames.length * allFrames[0].length];
		int count = 0;
		for (int i = 0; i < allFrames.length; i++)
			for (int j = 0; j < allFrames[i].length; j++)
				animatedTextures[count++] = allFrames[i][j];
		animation = new Animation(period, animatedTextures);
		animation.setPlayMode(PlayMode.LOOP);
		sprite = new Sprite(animation.getKeyFrame(elapsedTime, looping));
		sprite.setOrigin(0, 0);
		sprite.setScale(scale);
		sprite.setPosition(x, y);
		bounds = new Rectangle(sprite.getBoundingRectangle());
	}

	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);

		dx = 0;
		dy = 0;
		walking = false;

		if (health > 0) {
			if (Gdx.input.isKeyPressed(Keys.A)) {
				if (canMoveLeft) {
					walking = true;
					lastDirection = LEFT;
					dx -= speed;
				}
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				if (canMoveRight) {
					walking = true;
					lastDirection = RIGHT;
					dx += speed;
				}
			}
			if (Gdx.input.isKeyPressed(Keys.W)) {
				if (canMoveUp) {
					walking = true;
					dy += speed;
				}
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				if (canMoveDown) {
					walking = true;
					dy -= speed;
				}
			}

		}
	}

}

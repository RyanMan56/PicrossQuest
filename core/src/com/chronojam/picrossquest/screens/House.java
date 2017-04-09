package com.chronojam.picrossquest.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.chronojam.picrossquest.PicrossQuest;

public class House extends GameScreen {
	MapLayer objectLayer;
	RectangleMapObject wallObject;
	Rectangle[] wallBounds;

	public House(PicrossQuest game, AssetManager assetManager, String mapFile) {
		super(game, assetManager, mapFile);
		
		player.setPos(275, 20);
		camera.position.x = player.getX()+player.getWidth()/2;
		backgroundLayers = new int[]{0};
		foregroundLayers = new int[]{};
		objectLayer = tiledMap.getLayers().get("Objects");
		wallBounds = new Rectangle[5];
		for (int i = 0; i < wallBounds.length; i++) {
			wallObject = (RectangleMapObject) objectLayer.getObjects().get("Wall" + (i + 1));
			wallBounds[i] = new Rectangle(wallObject.getRectangle().x * 2, wallObject.getRectangle().y * 2, wallObject.getRectangle().width * 2, wallObject.getRectangle().height * 2);
		}
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);

		checkCollisions(player, wallBounds);
		
	}

}

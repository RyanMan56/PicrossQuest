package com.chronojam.picrossquest.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.chronojam.picrossquest.PicrossQuest;

public class Village extends GameScreen {
	MapLayer objectLayer;
	RectangleMapObject houseObject;
	Rectangle[] houseBounds;
	House house;
	Rectangle doorBounds;
	PicrossQuest game;

	public Village(PicrossQuest game, AssetManager assetManager, String mapFile) {
		super(game, assetManager, mapFile);
		this.game = game;

		backgroundLayers = new int[] { 0, 1 };
		foregroundLayers = new int[] { 2, 3 };
		objectLayer = tiledMap.getLayers().get("Objects");
		houseBounds = new Rectangle[3];
		for (int i = 0; i < 3; i++) {
			houseObject = (RectangleMapObject) objectLayer.getObjects().get("House" + (i + 1));
			houseBounds[i] = new Rectangle(houseObject.getRectangle().x * 2, houseObject.getRectangle().y * 2, houseObject.getRectangle().width * 2, houseObject.getRectangle().height * 2);
		}
		houseObject = (RectangleMapObject) objectLayer.getObjects().get("Door");
		doorBounds = new Rectangle(houseObject.getRectangle().x * 2, houseObject.getRectangle().y * 2, houseObject.getRectangle().width * 2, houseObject.getRectangle().height * 2);

		house = new House(game, assetManager, "House.tmx");
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		checkCollisions(player, houseBounds);
		if(player.getBounds().overlaps(doorBounds))
			game.setScreen(house);
	}

}

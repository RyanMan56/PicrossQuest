package com.chronojam.picrossquest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chronojam.picrossquest.PicrossQuest;
import com.chronojam.picrossquest.entity.Entity;
import com.chronojam.picrossquest.entity.Player;
import com.chronojam.picrossquest.environment.Dirt;
import com.chronojam.picrossquest.environment.Grass;
import com.chronojam.picrossquest.services.ImageProvider;

public class GameScreen implements Screen {
	private PicrossQuest game;
	private AssetManager assetManager;
	private SpriteBatch batch;
	protected ShapeRenderer shapeRenderer;
	protected OrthographicCamera camera;
	private Viewport viewport;

	protected Player player;
	protected TiledMap tiledMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	private float screenMoveSpeed = 2;
	private float mapScale = 2;

	private float mapWidth, mapHeight, tileWidth, tileHeight, mapPixelWidth, mapPixelHeight;
	protected int[] backgroundLayers;
	protected int[] foregroundLayers;
	protected float camX, camY;

	public GameScreen(PicrossQuest game, AssetManager assetManager, String mapFile) {
		this.game = game;
		this.assetManager = assetManager;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, ImageProvider.SCREEN_WIDTH, ImageProvider.SCREEN_HEIGHT);
		viewport = new FitViewport(ImageProvider.SCREEN_WIDTH, ImageProvider.SCREEN_HEIGHT, camera);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		player = new Player(assetManager, 600, 200);
		camera.position.set(player.getX()+player.getWidth()/2, player.getY()+player.getHeight()/2, 0);
		tiledMap = assetManager.get(mapFile, TiledMap.class);
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, mapScale);
		mapRenderer.setView(camera);

		mapWidth = tiledMap.getProperties().get("width", Integer.class);
		mapHeight = tiledMap.getProperties().get("height", Integer.class);
		tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
		tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

		mapPixelWidth = mapWidth * tileWidth * mapScale;
		mapPixelHeight = mapHeight * tileHeight * mapScale;
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update();
		
		shapeRender();

		mapRenderer.setView(camera);
		mapRenderer.render(backgroundLayers);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		player.render(batch);
		batch.end();
		mapRenderer.render(foregroundLayers);
	}
	
	protected void shapeRender(){
		
	}

	private void update() {
		camX = camera.position.x - camera.viewportWidth / 2;
		camY = camera.position.y - camera.viewportHeight / 2;
		if (Gdx.input.isKeyPressed(Keys.D))
			if (camX + camera.viewportWidth < mapPixelWidth)
				if (player.getX() > viewport.unproject(new Vector3((Gdx.graphics.getWidth() /2f) - player.getWidth()/2, 0, 0)).x)
					camera.translate(screenMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.A))
			if (camX > 0)
				if (player.getX() < viewport.unproject(new Vector3((Gdx.graphics.getWidth() / 2f) - player.getWidth()/2, 0, 0)).x)
					camera.translate(-screenMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.W))
			if (camY + camera.viewportHeight < mapPixelHeight)
				if (player.getY() > viewport.unproject(new Vector3(0, (Gdx.graphics.getHeight() / 2f) + player.getHeight()/2, 0)).y)
					camera.translate(0, screenMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.S))
			if (camY > 0)
				if (player.getY() < viewport.unproject(new Vector3(0, (Gdx.graphics.getHeight() / 2f) + player.getHeight()/2, 0)).y)
					camera.translate(0, -screenMoveSpeed);
		camera.update();
	}

	protected boolean checkCollisions(Entity e, Rectangle[] r) {
		for (int i = 0; i < r.length; i++) {
			if (r[i].overlaps(e.getBounds())) {
				if((new Rectangle(e.getX()+e.getWidth()-e.getSpeed(), e.getY()+e.getSpeed(), e.getSpeed(), e.getHeight()-e.getSpeed()*2).overlaps(r[i])) || (new Rectangle(e.getX(), e.getY()+e.getSpeed(), e.getSpeed(), e.getHeight()-e.getSpeed()*2).overlaps(r[i])))
					e.setX(e.getOldX());
				if((new Rectangle(e.getX()+e.getSpeed(), e.getY()+e.getHeight()-e.getSpeed(), e.getWidth()-e.getSpeed()*2, e.getSpeed()).overlaps(r[i])) || (new Rectangle(e.getX()+e.getSpeed(), e.getY(), e.getWidth()-e.getSpeed()*2, e.getSpeed()).overlaps(r[i])))
					e.setY(e.getOldY());
//				return true;
			}
		}
		return false;
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

}

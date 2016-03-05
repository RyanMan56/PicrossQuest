package com.subzero.picrossquest.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.subzero.picrossquest.PicrossQuest;
import com.subzero.picrossquest.services.ImageProvider;

public class GameScreen implements Screen {
	private ImageProvider imageProvider;
	private PicrossQuest game;
	private AssetManager assetManager;
	private Screen oldScreen;
	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private Rectangle gridBounds;
	private float squareWidth, squareHeight;
	private int squaresOnWidth, squaresOnHeight;
	private Texture finalImage, finalImageColoured;
	private Pixmap pixmap;
	private String topNumbers[];
	private String sideNumbers[];
	private StringBuilder sb;
	private Label topLabels[][];
	private Label sideLabels[][];
	private ArrayList topValues[];
	private ArrayList sideValues[];
	private LabelStyle labelStyle;
	private BitmapFont bitmapFont;
	private int finishedGrid[][], gameGrid[][];
	private int brushState = -1;
	private final int UNSET = -1, REMOVING = 0, DRAWING = 1;
	private float scale = 0.1f;
	private float initialFontWidth, initialFontHeight;
	private int biggestNumber = 0, biggestSpace, biggestSideNumber = 0, biggestSideSpace;
	private boolean finished = false;
	private boolean stage1Finished = false, stage2Finished = false, stage3Finished = false;
	private int pixelsToDraw = 0;
	private long startTime;
	private long destTime = 50;
	private float alpha = 0;
	private float glintX, glintY;
	private Rectangle penButtonBounds, crossButtonBounds;
	private Texture penButton;
	private boolean penButtonPressed = true, crossButtonPressed;
	private boolean checkTopNumbers, checkSideNumbers;
	private boolean[][] greyedNumbersTop, greyedNumbersSide;

	public GameScreen(PicrossQuest game, AssetManager assetManager, Screen oldScreen) {
		this.game = game;
		this.assetManager = assetManager;
		this.oldScreen = oldScreen;
		imageProvider = new ImageProvider();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, imageProvider.getScreenWidth(), imageProvider.getScreenHeight());
		viewport = new FitViewport(imageProvider.getScreenWidth(), imageProvider.getScreenHeight(), camera);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		finalImage = assetManager.get("NikolaTemplate.png", Texture.class);
		finalImage.getTextureData().prepare();
		pixmap = finalImage.getTextureData().consumePixmap();

		finalImageColoured = assetManager.get("Nikola.png", Texture.class);

		squaresOnWidth = finalImage.getWidth();
		squaresOnHeight = finalImage.getHeight();
		gridBounds = new Rectangle(400, 20, imageProvider.getScreenWidth() - 20, imageProvider.getScreenHeight() - 100);
		//		squareWidth = (gridBounds.width - gridBounds.x) / squaresOnWidth;
		squareHeight = (gridBounds.height - gridBounds.y) / squaresOnHeight;
		squareWidth = squareHeight; // This instead to keep square aspect ratio

		topNumbers = new String[finalImage.getWidth()];
		sideNumbers = new String[finalImage.getHeight()];

		sb = new StringBuilder();

		int count = 0;

		for (int i = 0; i < topNumbers.length; i++) {
			for (int y = 0; y < squaresOnHeight; y++) {
				int color = pixmap.getPixel(i, y);
				if ((color & 0xff000000) == 0x0) {
					count++;
				} else {
					if (count != 0) {
						sb.append(count + "\n");
						count = 0;
					}
				}
			}
			if (count != 0) {
				sb.append(count + "\n");
				count = 0;
			}
			topNumbers[i] = sb.toString();
			sb = new StringBuilder();
		}

		for (int i = 0; i < sideNumbers.length; i++) {
			for (int x = 0; x < squaresOnWidth; x++) {
				int color = pixmap.getPixel(x, i);
				if ((color & 0xff000000) == 0x0) {
					count++;
				} else {
					if (count != 0) {
						sb.append(count + "  ");
						count = 0;
					}
				}
			}
			if (count != 0) {
				sb.append(count + "  ");
				count = 0;
			}
			sideNumbers[i] = sb.toString();
			sb = new StringBuilder();
		}

		topValues = new ArrayList[squaresOnWidth];
		for (int x = 0; x < squaresOnWidth; x++) {
			int length = 0;
			int start = 0;
			topValues[x] = new ArrayList<Integer>();
			for (int i = 0; i < topNumbers[x].length(); i++) {
				Character charAtI = topNumbers[x].charAt(i);
				if (!charAtI.equals('\n')) {
					length++;
				} else {
					topValues[x].add(Integer.parseInt(topNumbers[x].substring(start, start + length)));
					length = 0;
					if (i != topNumbers[x].length())
						start = i + 1;
				}
			}
		}

		sideValues = new ArrayList[squaresOnHeight];
		for (int y = 0; y < squaresOnHeight; y++) {
			int length = 0;
			int start = 0;
			sideValues[y] = new ArrayList<Integer>();
			for (int i = 0; i < sideNumbers[y].length(); i++) {
				Character charAtI = sideNumbers[y].charAt(i);
				if (!charAtI.equals(' ')) {
					length++;
				} else {
					if (length > 0)
						sideValues[y].add(Integer.parseInt(sideNumbers[y].substring(start, start + length)));
					length = 0;
					if (i != sideNumbers[y].length())
						start = i + 1;
				}
			}
		}

		greyedNumbersTop = new boolean[squaresOnWidth][];
		for (int x = 0; x < topValues.length; x++) {
			greyedNumbersTop[x] = new boolean[topValues[x].size()];
		}

		greyedNumbersSide = new boolean[squaresOnHeight][];
		for (int y = 0; y < sideValues.length; y++) {
			greyedNumbersSide[y] = new boolean[sideValues[y].size()];
		}

		//		bitmapFont = new BitmapFont(Gdx.files.internal("font.fnt"));
		bitmapFont = new BitmapFont();
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		labelStyle = new LabelStyle();
		labelStyle.font = bitmapFont;
		labelStyle.fontColor = Color.BLACK;
		topLabels = new Label[topNumbers.length][];
		for (int i = 0; i < topValues.length; i++) {
			topLabels[i] = new Label[topValues[i].size()];
		}

		sideLabels = new Label[sideNumbers.length][];
		for (int i = 0; i < sideValues.length; i++) {
			sideLabels[i] = new Label[sideValues[i].size()];
		}

		for (int i = 0; i < topLabels.length; i++) {
			int offset = 5;
			if (topNumbers[i].length() >= 3)
				if (!topNumbers[i].substring(topNumbers[i].length() - 3, topNumbers[i].length() - 1).contains("\n"))
					if (Integer.parseInt(topNumbers[i].substring(topNumbers[i].length() - 3, topNumbers[i].length() - 1)) >= 10)
						offset = 2;
			if (topNumbers[i].equals(""))
				topNumbers[i] = "0\n";

			int offsetH = 0;
			for (int j = 0; j < topLabels[i].length; j++) {
				topLabels[i][j] = new Label(topValues[i].get(j).toString(), labelStyle);
				//			topLabels[i].setFontScale(squareWidth/25);
				//				topLabels[i][j].setAlignment(Align.bottomLeft);
				//			topLabels[i].setPosition(gridBounds.x + squareWidth * i + offset + squareWidth / 2 - topLabels[i].getWidth() / 2, gridBounds.height - 20 * topLabels[i].getFontScaleY());
				topLabels[i][j].setPosition(gridBounds.x + squareWidth * i + offset + squareWidth / 2 - topLabels[i][j].getWidth() / 2, gridBounds.height - topLabels[i][j].getHeight() + offsetH);
				offsetH -= topLabels[i][j].getHeight();
			}
		}

		for (int i = 0; i < sideLabels.length; i++) {
			if (sideNumbers[i].equals(""))
				sideNumbers[i] = "0  ";
			int offsetW = 0;
			for (int j = 0; j < sideLabels[i].length; j++) {
				sideLabels[i][j] = new Label(sideValues[i].get(j).toString(), labelStyle);
				//			sideLabels[i].setFontScale(squareHeight/25);
				//				sideLabels[i].setAlignment(Align.right);
				sideLabels[i][j].setPosition(gridBounds.x - (sideLabels[i][j].getWidth()) + offsetW, gridBounds.y + gridBounds.height - squareHeight * (i + 1) + squareHeight / 2 - sideLabels[i][j].getHeight() / 2);
				offsetW += sideLabels[i][j].getHeight();
			}
		}

		for (int i = 0; i < topNumbers.length; i++) {
			if (topNumbers[i].length() > biggestNumber) {
				biggestNumber = topNumbers[i].length();
				biggestSpace = i;
			}
		}
		for (int i = 0; i < sideNumbers.length; i++) {
			if (sideNumbers[i].length() > biggestSideNumber) {
				biggestSideNumber = sideNumbers[i].length();
				biggestSideSpace = i;
			}
		}
		initialFontHeight = topLabels[biggestSpace][0].getHeight() * topValues[biggestSpace].size();
		setupScale();

		finishedGrid = new int[squaresOnHeight][squaresOnWidth];
		gameGrid = new int[squaresOnHeight][squaresOnWidth];
		for (int y = 0; y < squaresOnHeight; y++) {
			for (int x = 0; x < squaresOnWidth; x++) {
				int color = pixmap.getPixel(x, finalImage.getHeight() - 1 - y);
				if ((color & 0xff000000) == 0x0) {
					finishedGrid[y][x] = 1;
					//					gameGrid[y][x] = 1;
				} else {
					finishedGrid[y][x] = 0;
					//					gameGrid[y][x] = 0;
				}
			}
		}

		finalImageColoured.getTextureData().prepare();
		pixmap = finalImageColoured.getTextureData().consumePixmap();

		penButton = assetManager.get("PenButton.png", Texture.class);
		penButtonBounds = new Rectangle(0, imageProvider.getScreenHeight() - penButton.getHeight(), penButton.getWidth(), penButton.getHeight());
		crossButtonBounds = new Rectangle(0, penButtonBounds.y - penButtonBounds.height, penButton.getWidth(), penButton.getHeight());
	}

	private void fixHeight(){
//		if(topLabels[biggestSpace][0].getY() < gridBounds.y+gridBounds.height){
//			for(int i = 0; i < topLabels.length; i++){
//				for(int j = 0; j < topLabels[i].length; j++){
//					topLabels[i][j].setY(topLabels[i][j].getY() + 10);
//					fixHeight();
//				}
//			}
//			System.out.println(topLabels[biggestSpace][0].getY());
//		}
		System.out.println(topLabels[0][0].getText());
	}

	private void setupScale() {
		fixHeight();
	}

	//	private void setupScale() {
	//		if (topLabels[biggestSpace].getY() + topLabels[biggestSpace].getHeight() * scale + initialFontHeight > imageProvider.getScreenHeight()) {
	//			for (int i = 0; i < topLabels.length; i++) {
	//				topLabels[i].setFontScale(scale);
	//				topLabels[i].setAlignment(Align.bottomLeft);
	//				int offset = 5;
	//				if (topNumbers[i].length() >= 3)
	//					if (!topNumbers[i].substring(topNumbers[i].length() - 3, topNumbers[i].length() - 1).contains("\n"))
	//						if (Integer.parseInt(topNumbers[i].substring(topNumbers[i].length() - 3, topNumbers[i].length() - 1)) >= 10)
	//							offset = 2;
	//				topLabels[i].setPosition(gridBounds.x + squareWidth * i + offset * scale + squareWidth / 2 - topLabels[i].getWidth() / 2, gridBounds.height - 20 * topLabels[i].getFontScaleY());
	//			}
	//			for (int i = 0; i < sideLabels.length; i++) {
	//				sideLabels[i].setFontScale(scale);
	//				sideLabels[i].setPosition(gridBounds.x - (sideLabels[i].getWidth()), gridBounds.height - squareHeight * (i + 1) + squareHeight / 2 - sideLabels[i].getHeight() / 2);
	//			}
	//		} else {
	//			scale += 0.1f;
	//			setupScale();
	//		}
	//	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		update();

		drawBackground();

		drawGrid();
	}

	public void update() {
	}

	private void drawBackground() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(0, 0, imageProvider.getScreenWidth(), imageProvider.getScreenHeight());
		shapeRenderer.end();
	}

	private void drawGrid() {
		Color colour = batch.getColor();
		batch.setColor(colour.r, colour.g, colour.b, 1);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		if (checkTopNumbers) {
			for (int i = 0; i < greyedNumbersTop.length; i++)
				for (int j = 0; j < greyedNumbersTop[i].length; j++)
					greyedNumbersTop[i][j] = false;

			for (int x = 0; x < squaresOnWidth; x++) {
				int consecutiveSquares = 0;
				for (int y = 0; y < squaresOnHeight; y++) {
					if (gameGrid[y][x] == 1) {
						consecutiveSquares++;
					} else {
						for (int i = 0; i < topValues[x].size(); i++) {
							if (consecutiveSquares == (Integer) topValues[x].get(i)) {
								greyedNumbersTop[x][i] = true;
							}

						}
						consecutiveSquares = 0;
					}
				}
			}

			checkTopNumbers = false;
		}

		batch.draw(assetManager.get("Background.png", Texture.class), 0, 0);

		if (penButtonPressed)
			batch.draw(assetManager.get("PenButtonSelected.png", Texture.class), penButtonBounds.x, penButtonBounds.y, penButtonBounds.width, penButtonBounds.height);
		else
			batch.draw(assetManager.get("PenButton.png", Texture.class), penButtonBounds.x, penButtonBounds.y, penButtonBounds.width, penButtonBounds.height);

		if (crossButtonPressed)
			batch.draw(assetManager.get("CrossButtonSelected.png", Texture.class), crossButtonBounds.x, crossButtonBounds.y, crossButtonBounds.width, crossButtonBounds.height);
		else
			batch.draw(assetManager.get("CrossButton.png", Texture.class), crossButtonBounds.x, crossButtonBounds.y, crossButtonBounds.width, crossButtonBounds.height);

		for (int x = 0; x < squaresOnWidth / 2; x++) {
			batch.draw(assetManager.get("Glint.png", Texture.class), gridBounds.x + x * 2 * squareWidth, gridBounds.y, squareWidth, squaresOnHeight * squareHeight);
			//			batch.draw(assetManager.get("EndGlint.png", Texture.class), gridBounds.x + x * 2 * squareWidth, gridBounds.height, squareWidth, topLabels[biggestSpace].getHeight() * topLabels[biggestSpace].getFontScaleY());
		}
		for (int y = 0; y < squaresOnHeight / 2; y++) {
			batch.draw(assetManager.get("Glint.png", Texture.class), gridBounds.x, gridBounds.y + y * 2 * squareHeight + squareHeight, squaresOnWidth * squareWidth, squareHeight);
			//			batch.draw(assetManager.get("EndGlintSide.png", Texture.class), gridBounds.x - sideLabels[biggestSideSpace].getWidth() * sideLabels[biggestSideSpace].getFontScaleX() * 1.5f, gridBounds.y + y * 2 * squareHeight + squareHeight, sideLabels[biggestSideSpace].getWidth() * sideLabels[biggestSideSpace].getFontScaleX() * 1.5f, squareHeight);
		}

		for (int y = 0; y < squaresOnHeight; y++)
			for (int x = 0; x < squaresOnWidth; x++)
				if (gameGrid[y][x] == 1) {
					batch.draw(assetManager.get("Square.png", Texture.class), gridBounds.x + x * squareWidth, gridBounds.y + y * squareHeight, squareWidth, squareHeight);
				} else if (gameGrid[y][x] == 2) {
					batch.draw(assetManager.get("Cross.png", Texture.class), gridBounds.x + x * squareWidth, gridBounds.y + y * squareHeight, squareWidth, squareHeight);
				}

		for (int i = 0; i < topLabels.length; i++) {
			for (int j = 0; j < topLabels[i].length; j++) {
				//			if (i == 0)
				//				topLabels[0].getStyle().fontColor = Color.GRAY;
				//			else
				//				topLabels[i].getStyle().fontColor = Color.BLACK;
				topLabels[i][j].draw(batch, batch.getColor().a);
			}
		}
		for (int i = 0; i < sideLabels.length; i++)
			for (int j = 0; j < sideLabels[i].length; j++)
				sideLabels[i][j].draw(batch, batch.getColor().a);

		batch.end();

		boolean squaresFilled = true;
		if (!finished)
			for (int y = 0; y < squaresOnHeight; y++)
				for (int x = 0; x < squaresOnWidth; x++) {
					if (finishedGrid[y][x] == 1) {
						if (gameGrid[y][x] != 1)
							squaresFilled = false;
					} else if (finishedGrid[y][x] == 0) {
						if (gameGrid[y][x] == 1)
							squaresFilled = false;
					}
				}
		if (squaresFilled)
			finished = true;

		if (Gdx.input.isKeyJustPressed(Keys.W) || ((penButtonBounds.contains(mouseToWorld()))) && (Gdx.input.justTouched())) {
			if (penButtonPressed) {
				penButtonPressed = false;
			} else {
				crossButtonPressed = false;
				penButtonPressed = true;
			}
		} else if (Gdx.input.isKeyJustPressed(Keys.S) || ((crossButtonBounds.contains(mouseToWorld()))) && (Gdx.input.justTouched())) {
			if (crossButtonPressed) {
				crossButtonPressed = false;
			} else {
				penButtonPressed = false;
				crossButtonPressed = true;
			}
		}

		if (!finished) {
			shapeRenderer.begin(ShapeType.Filled);
			for (int y = 0; y < squaresOnHeight; y++)
				for (int x = 0; x < squaresOnWidth; x++) {

					if (penButtonPressed) {
						if (new Rectangle((gridBounds.x + x * squareWidth), (gridBounds.y + y * squareHeight), squareWidth, squareHeight).contains(mouseToWorld())) {
							if (Gdx.input.isTouched()) {
								if (gameGrid[y][x] == 1) {
									if (brushState != DRAWING) {
										gameGrid[y][x] = 0;
										brushState = REMOVING;
									}
								} else if (gameGrid[y][x] == 0) {
									if (brushState != REMOVING) {
										gameGrid[y][x] = 1;
										brushState = DRAWING;
									}
								}
								checkTopNumbers = true;
								checkSideNumbers = true;
							} else
								brushState = UNSET;
						}
					} else if (crossButtonPressed) {
						if (new Rectangle((gridBounds.x + x * squareWidth), (gridBounds.y + y * squareHeight), squareWidth, squareHeight).contains(mouseToWorld())) {
							if (Gdx.input.isTouched()) {
								if (gameGrid[y][x] == 2) {
									if (brushState != DRAWING) {
										gameGrid[y][x] = 0;
										brushState = REMOVING;
									}
								} else if (gameGrid[y][x] == 0) {
									if (brushState != REMOVING) {
										gameGrid[y][x] = 2;
										brushState = DRAWING;
									}
								}
								checkTopNumbers = true;
								checkSideNumbers = true;
							} else
								brushState = UNSET;
						}
					}
				}
			shapeRenderer.end();
		} else {
			if (!stage1Finished) {
				if (!(pixelsToDraw >= squaresOnWidth + squaresOnWidth - 2)) {
					for (int y = 0; y < squaresOnHeight; y++) {
						for (int x = y; x < squaresOnWidth + y; x++) {
							if (System.currentTimeMillis() - startTime >= destTime) {
								startTime = System.currentTimeMillis();
								pixelsToDraw++;
							}
							if (x <= pixelsToDraw)
								drawPixel(x - y, y);
						}
					}
				} else {
					alpha = 0;
					stage1Finished = true;
				}
			} else {
				//				if (stage2Finished) {
				if (!stage3Finished) {
					if (alpha < 1)
						alpha += 0.05f;
					else {
						alpha = 1;
						stage3Finished = true;
						//						}
					}
				}
			}
		}

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if ((stage1Finished) && (!stage3Finished))
			batch.draw(finalImageColoured, gridBounds.x, gridBounds.y, squaresOnWidth * squareWidth, squaresOnHeight * squareHeight);
		batch.end();

		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.GRAY);
		for (int y = 0; y < squaresOnHeight + 1; y++) {
			if (y > 0 && y != squaresOnHeight)
				if (y % 5 == 0)
					shapeRenderer.setColor(Color.GOLDENROD);
				else
					shapeRenderer.setColor(Color.GRAY);
			shapeRenderer.line(gridBounds.x, gridBounds.y + gridBounds.height - gridBounds.y - y * squareHeight, gridBounds.x + squaresOnWidth * squareWidth, gridBounds.y + gridBounds.height - gridBounds.y - y * squareHeight);
		}
		for (int x = 0; x < squaresOnWidth + 1; x++) {
			if (x > 0 && x != squaresOnWidth)
				if (x % 5 == 0)
					shapeRenderer.setColor(Color.GOLDENROD);
				else
					shapeRenderer.setColor(Color.GRAY);
			shapeRenderer.line(gridBounds.x + x * squareWidth, gridBounds.y, gridBounds.x + x * squareWidth, gridBounds.height);
		}
		shapeRenderer.end();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		boolean shouldPopScissors = false;

		if (stage1Finished && !stage2Finished) {
			if (glintX >= squaresOnWidth * squareWidth * 2.5f)
				stage2Finished = true;

			Rectangle scissors = new Rectangle();
			Rectangle clipBounds = new Rectangle(gridBounds.x, gridBounds.y, squaresOnWidth * squareWidth, squaresOnHeight * squareHeight);
			ScissorStack.calculateScissors(viewport.getCamera(), batch.getTransformMatrix(), clipBounds, scissors);
			shouldPopScissors = ScissorStack.pushScissors(scissors);
		}

		if (stage1Finished) {
			colour = batch.getColor();
			batch.setColor(colour.r, colour.g, colour.b, alpha);
			batch.draw(finalImageColoured, gridBounds.x, gridBounds.y, squaresOnWidth * squareWidth, squaresOnHeight * squareHeight);
		}
		if (stage1Finished && !stage2Finished) {
			batch.draw(assetManager.get("Glint.png", Texture.class), glintX += 20f, glintY, assetManager.get("Glint.png", Texture.class).getWidth() / 2, assetManager.get("Glint.png", Texture.class).getHeight() / 2, assetManager.get("Glint.png", Texture.class).getWidth(), gridBounds.height * 1.5f, 1, 1, 315, 0, 0, assetManager.get("Glint.png", Texture.class).getWidth(),
					assetManager.get("Glint.png", Texture.class).getWidth() / 2, false, false);
		}
		batch.end();
		if (shouldPopScissors)
			ScissorStack.popScissors();

	}

	private void drawPixel(int x, int y) {
		shapeRenderer.begin(ShapeType.Filled);
		Color colour = new Color();
		Color.rgba8888ToColor(colour, pixmap.getPixel(x, y));
		shapeRenderer.setColor(colour.r, colour.g, colour.b, colour.a);
		if (colour.a > 0)
			shapeRenderer.rect(gridBounds.x + x * squareWidth, gridBounds.height - squareHeight - (y * squareHeight), squareWidth, squareHeight);
		shapeRenderer.end();
	}

	private Vector2 mouseToWorld() {
		return new Vector2(viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())).x, viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())).y);
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

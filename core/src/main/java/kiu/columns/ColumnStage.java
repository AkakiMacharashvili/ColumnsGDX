package main.java.kiu.columns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import main.java.columns.Controller;
import main.java.columns.Graphics;
import main.java.columns.Model;
import main.java.columns.View;

public class ColumnStage extends Stage implements Graphics {

	static Color[] COLORS = {
			Color.DARK_GRAY, Color.RED, Color.GREEN,
			Color.BLUE, Color.CYAN, Color.YELLOW,
			Color.MAGENTA, Color.MAROON};

	private ShapeRenderer shape;
	private View view;
	private Model model;
	private Controller controller;
	private SpriteBatch batch;
	private BitmapFont font;
	private TextButton refreshButton;
	private TextButton quitButton;
	private Timer timer;
	private Timer.Task task;

	public ColumnStage() {
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false, 400, 700); // Set the camera's viewport width and height
		setViewport(new ScreenViewport(camera));

		shape = new ShapeRenderer();
		font = new BitmapFont(); // Load the default font
		batch = new SpriteBatch();

		createRefreshButton();
		addRefreshButton();
		refreshButton.setVisible(false);

		createQuitButton();
		addQuitButton();
		quitButton.setVisible(false);

		Gdx.graphics.setWindowedMode(400, 700);
		Gdx.input.setInputProcessor(this);
	}

	private void addQuitButton() {

		quitButton.setPosition(170, Gdx.graphics.getHeight() - 100);
		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
				quitButton.setVisible(false);
			}
		});
		addActor(quitButton);
	}

	private void createQuitButton() {

		BitmapFont font = new BitmapFont();
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;
		textButtonStyle.downFontColor = Color.GRAY;
		textButtonStyle.overFontColor = Color.YELLOW;

		quitButton = new TextButton("Quit", textButtonStyle);
	}

	private void createRefreshButton() {
		BitmapFont font = new BitmapFont();
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;
		textButtonStyle.downFontColor = Color.GRAY;
		textButtonStyle.overFontColor = Color.YELLOW;

		refreshButton = new TextButton("Refresh", textButtonStyle);
	}

	private void addRefreshButton() {
		refreshButton.setPosition(170, Gdx.graphics.getHeight() - 80);
		refreshButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				model.refresh();

				refreshButton.setVisible(false);
				quitButton.setVisible(false);
			}
		});
		addActor(refreshButton);
	}

	public void init() {
		model = new Model();
		view = new View(this);
		controller = new Controller(model, view);

		model.addListener(controller);

		timer = new Timer();
		scheduleTask();


		Gdx.input.setInputProcessor(this);

		addListener(new InputListener() {

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch (keycode) {
					case Input.Keys.LEFT:
						controller.tryMoveLeft();
						break;
					case Input.Keys.RIGHT:
						controller.tryMoveRight();
						break;
					case Input.Keys.SPACE:
						controller.dropFigure();
						break;
					case Input.Keys.UP:
						controller.rotateUp();
						break;
					case Input.Keys.DOWN:
						controller.rotateDown();
						break;
					case Input.Keys.EQUALS:
						controller.increaseLevel();
						break;
					case Input.Keys.MINUS:
						controller.decreaseLevel();
						break;
					case Input.Keys.P:
						controller.pause();
						break;
				}
				return true;
			}

		});

	}

	private void scheduleTask() {
		if (task != null) {
			task.cancel();
		}
		task = new Timer.Task() {
			@Override
			public void run() {
				controller.trySlideDown();
			}
		};

		timer.scheduleTask(task, 1.0f, (float) 1 / model.level);
	}

	@Override
	public void dispose() {
		shape.dispose();
		batch.dispose();
		font.dispose();
	}

	@Override
	public void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

		view.draw(model);

		super.draw();
	}

	@Override
	public void drawBoxAt(int x, int y, int size, int colorIndex) {
		shape.setProjectionMatrix(getViewport().getCamera().combined);
		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.setColor(COLORS[colorIndex]);

		// Adjust y to flip the coordinate system
		float adjustedY = Gdx.graphics.getHeight() - y - size;

		shape.rect(x, adjustedY, size, size);
		shape.end();
	}

	@Override
	public void setColor(Color color) {
		shape.setColor(color);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		shape.setProjectionMatrix(getViewport().getCamera().combined);
		shape.begin(ShapeRenderer.ShapeType.Line);
		shape.setColor(Color.BLACK); // Set default color, adjust if needed
		shape.line(x1, y1, x2, y2);
		shape.end();
	}

	@Override
	public void drawString(String message, int x, int y) {
		batch.setProjectionMatrix(getViewport().getCamera().combined);
		batch.begin();
		font.draw(batch, message, x, y);
		batch.end();
	}

	@Override
	public void drawScore() {
		font.getData().setScale(1f);
		batch.setProjectionMatrix(getViewport().getCamera().combined);
		batch.begin();
		font.setColor(Color.WHITE); // Set the color for text
		String scoreMessage = "Score: " + model.Score; // Retrieve the score from the model
		float xPosition = 50;
		float yPosition = Gdx.graphics.getHeight() - 20; // Y position of the text
		font.draw(batch, scoreMessage, xPosition, yPosition); // Draw the text
		batch.end();
	}

	@Override
	public void drawLevel() {
		font.getData().setScale(1f);
		batch.setProjectionMatrix(getViewport().getCamera().combined);
		batch.begin();
		font.setColor(Color.WHITE);
		int currentLevel = model.level;
		String scoreMessage = "Level: " + currentLevel;
		float xPosition = 300;
		float yPosition = Gdx.graphics.getHeight() - 20;
		font.draw(batch, scoreMessage, xPosition, yPosition);
		batch.end();
	}

	@Override
	public void gameOver() {
		font.getData().setScale(1.5f);
		batch.setProjectionMatrix(getViewport().getCamera().combined);
		batch.begin();
		font.setColor(Color.RED); // Set the color for text
		String scoreMessage = "Game Over"; // Retrieve the score from the model
		float xPosition = 150;
		int yPosition = Gdx.graphics.getHeight() - 100; // Y position of the text
		font.draw(batch, scoreMessage, xPosition, yPosition); // Draw the text
		batch.end();
	}

	@Override
	public void activateGameOver() {
		refreshButton.setVisible(true);
		quitButton.setVisible(true);
	}

	@Override
	public void updateLevel() {
		scheduleTask();
	}

}

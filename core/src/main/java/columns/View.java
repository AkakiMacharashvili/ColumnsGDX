package main.java.columns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class View {

	public static final int BOX_SIZE = 30;
	public static final int ORIGIN = 50;
	private Graphics graphics;
	private SpriteBatch batch;
	private BitmapFont font;

	public View(Graphics graphics) {
		this.graphics = graphics;
		batch = new SpriteBatch();
		font = new BitmapFont();
	}

	public void draw(Model model) {
		if (!model.finished) {
			drawData(new DrawDataParameter(model.newField, 0, 0, true));
			if(!model.blinkState){
				drawData(new DrawDataParameter(model.Fig.c, model.Fig.y, model.Fig.x, false));
			}
			drawGrid(model.newField.length, model.newField[0].length);
			drawScore();
			drawLevel();
		} else {
			gameOver();
			activateGameOver();
		}
	}

	private void activateGameOver() {
		graphics.activateGameOver();
	}

	public void gameOver() {
		graphics.gameOver();
	}

	private void drawData(DrawDataParameter parameterObject) {
		if (parameterObject.fs != null) {
			for (int r = 0; r < parameterObject.fs.length; r++) {
				for (int c = 0; c < parameterObject.fs[r].length; c++) {
					if (!parameterObject.drawBackground && parameterObject.fs[r][c] == 0)
						continue;
					drawBox(r + parameterObject.row, c + parameterObject.col, parameterObject.fs[r][c]);
				}
			}
		} else if (parameterObject.ft != null) {
			for (int c = 0; c < parameterObject.ft.length; c++) {
				drawBox(parameterObject.row + c, parameterObject.col, parameterObject.ft[c]);
			}
		}
	}

	private void drawBox(int row, int col, int value) {
		// Calculate the drawing coordinates
		int x = ORIGIN + col * BOX_SIZE;
		int y = ORIGIN + row * BOX_SIZE;
		graphics.drawBoxAt(x, y, BOX_SIZE, value);
	}

	private void drawGrid(int rows, int cols) {
		graphics.setColor(Color.GRAY);
		for (int r = 0; r <= rows; r++) {

			int y1 = Gdx.graphics.getHeight() - (ORIGIN + r * BOX_SIZE);
			int y2 = Gdx.graphics.getHeight() - (ORIGIN + r * BOX_SIZE);
			int x1 = ORIGIN;
			int x2 = ORIGIN + cols * BOX_SIZE;

			graphics.drawLine(x1, y1, x2, y2);
		}
		for (int c = 0; c <= cols; c++) {

			int x1 = ORIGIN + c * BOX_SIZE;
			int x2 = ORIGIN + c * BOX_SIZE;
			int y1 = Gdx.graphics.getHeight() - ORIGIN;
			int y2 = Gdx.graphics.getHeight() - (ORIGIN + rows * BOX_SIZE);
			graphics.drawLine(x1, y1, x2, y2);
		}
	}

	private void drawScore() {
		graphics.drawScore();
	}

	private void drawLevel() {
		graphics.drawLevel();
	}

	public void updateLevel(int level) {
		graphics.updateLevel();
	}

	public void updateField(Model model) {
		drawData(new DrawDataParameter(model.newField, 0, 0, true));
	}


}

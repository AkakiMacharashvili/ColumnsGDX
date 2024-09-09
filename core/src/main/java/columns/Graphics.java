package main.java.columns;

import com.badlogic.gdx.graphics.Color; // Import libGDX Color class

public interface Graphics {

	void drawBoxAt(int i, int j, int size, int value);

	void setColor(Color color); // Use libGDX Color here

	void drawLine(int origin, int i, int i1, int i2);

	void drawString(String message, int origin, int y);

	void drawScore();

	void drawLevel();

	void gameOver();

	void activateGameOver();

	void updateLevel();
}

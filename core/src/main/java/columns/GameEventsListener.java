package main.java.columns;

public interface GameEventsListener {
	void trySlideDown();
	void tryMoveRight();
	void tryMoveLeft();
	void dropFigure();
	void rotateUp();
	void rotateDown();
	void pause();
	void increaseLevel();
	void decreaseLevel();
}

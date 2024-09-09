package main.java.columns;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Model implements GameEventsListener{
	static final int FigToDropForNextLevel = 33;
	static final int MaxLevel = 7;
	static final int Height = 20;
	static final int Width = 10;

	public int MinLevel = 1;
	public int level = 1;
	public int removedCellsCounter;
	public int Score;
	public int DropScore;
	public Figure Fig;
	public int[][] newField;
	public int[][] oldField;
	public boolean noChanges = true;
	public boolean paused = false;
	public boolean firstMove = false;
	List<ModelListener> listeners = new ArrayList<>();
	public boolean finished = false;
	public boolean blinkState = false; // Flag for blinking state
	public boolean show = true;
	List<List<Integer>> pairs = new ArrayList<>();


	void packField() {
		for (int i = 0; i < Model.Width; i++) {
			int n = Model.Height - 1;
			for (int j = Model.Height - 1; j >= 0; j--) {
				if (oldField[j][i] > 0) {
					newField[n][i] = oldField[j][i];
					n--;
				}
			}

			for (int k = n; k >= 0; k--)
				newField[k][i] = 0;
		}
		fireFieldUpdated();
	}

	boolean mayFigureSlideDown() {
		return (Fig.y < Model.Height - 3) && (newField[Fig.y + 3][Fig.x] == 0);
	}

	final static int[][] SHIFTS = { { 0, -1, 0, 1 }, { -1, 0, 1, 0 }, { -1, -1, 1, 1 }, { 1, -1, -1, 1 }, };

	public void trySlideDown() {
		if(paused){
			blinkState = !blinkState;
			return;
		}
		blinkState = false;
//		if(firstMove){
//			firstMove = false;
//			return;
//		}
		if (mayFigureSlideDown()) {
			Fig.y++;
//			fireFigureMovedEvent(Fig.x, Fig.y - 1);
//			fireUpdateFigure();
		} else {
			processFigureCantMoveDown();
		}
	}

	//------------------------------------------------------------Initializing------------------------------------------------------------------------

	public Model(){
		initModel();
		initMembers();
		initMatrixes();
		Fig = new Figure();
	}

	void initModel() {
		newField = new int[Model.Height][Model.Width];
		oldField = new int[Model.Height][Model.Width];
		firstMove = true;
		finished = false;
	}

	void initMembers() {
		level = 1;
		Score = 0;
		MinLevel = 1;
		removedCellsCounter = 0;
	}

	void initMatrixes() {
		for (int i = 0; i < Model.Height; i++) {
			for (int j = 0; j < Model.Width; j++) {
				newField[i][j] = 0;
				oldField[i][j] = 0;
			}
		}
	}

	//------------------------------------------------------------General Operations------------------------------------------------------------------------

	public void addListener(ModelListener listener) {
		listeners.add(listener);
	}

	// something wrong
	void checkIfFieldIsFull() {
//		for (int i = 0; i < Model.Width; i++) {
//			if (newField[i][2] > 0) {
//				fireGameOver();
//				this.finished = true;
//				return;
//			}
//		}
		if(Fig.x == Model.Width / 2 - 1 && Fig.y == 0 && newField[2][Model.Width / 2 - 1] > 0){
			finished = true;
			fireGameOver();
			return;
		}
		createNewFigure();
	}

	void testField() {
		copyField();
		for (int i = 0; i < Model.Width; i++) {
			for (int j = 0; j < Model.Height; j++) {
				if (newField[j][i] > 0) {
					for (int[] s : SHIFTS) {
						if (borderCheck(j, i, s) &&
								!checkNeighbours(j + s[0], i + s[1], j + s[2], i + s[3], i, j))
							continue;
						for (ModelListener modelListener : listeners) {
							modelListener.foundNeighboursAt(j + s[0], i + s[1], j + s[2], i + s[3], i, j);
						}
					}
				}
			}
		}
	}

	private static boolean borderCheck(int j, int i, int[] s) {
		return (0 <= j + s[0] && j + s[0] < Height) &&
				(0 <= j + s[2] && j + s[2] < Height) &&
				(0 <= i + s[1] && i + s[1] < Width) &&
				(0 <= i + s[3] && i + s[3] < Width);
	}

	public boolean checkBorder(int direction){
		return (direction == 1 ? Fig.x < Model.Width - 1: Fig.x > 0);
	}

	void changeScore() {
		Score += DropScore;
		MinLevel = Score / 250;
		MinLevel++;


		if(MinLevel > level){
			changeLevel(1);
		}
	}

	private void copyField() {
		for (int i = 0; i < Model.Width; i++) {
			for (int j = 0; j < Model.Height; j++) {
				oldField[j][i] = newField[j][i];
			}
		}
	}

	@Override
	public void pause() {
		this.paused = !paused;
	}

	//------------------------------------------------------------Figure Operations------------------------------------------------------------------------

	void createNewFigure() {
		Fig = new Figure();
	}

	void processFigureCantMoveDown() {
		DropScore = 0;
		pasteFigure(Fig);
		removeSimilarCellsRepeatedly();
		checkIfFieldIsFull();
	}

	public void dropFigure() {
		int zz;
		if (Fig.y < Model.Height - 2) {
			int oldX = Fig.x;
			int oldY = Fig.y;
			zz = Model.Height - 1;
			while (newField[zz][Fig.x] > 0)
				zz--;
			DropScore = (((level + 1) * (Model.Width * 2 - Fig.y - zz) * 2) % 5) * 5;
			Fig.y = zz - 2;
			processFigureCantMoveDown();
		}
	}

	void pasteFigure(Figure f) {
		int[] c = f.getCells();
		newField[f.y][f.x] = c[0];
		newField[f.y + 1][f.x] = c[1];
		newField[f.y + 2][f.x] = c[2];
		fireFieldUpdated();
	}

	//------------------------------------------------------------Neighbour Checker------------------------------------------------------------------------

	boolean areNeighbours(int a, int b, int c, int d, int i, int j) {
		return (newField[j][i] == newField[a][b]) && (newField[j][i] == newField[c][d]);
	}

	boolean checkNeighbours(int a, int b, int c, int d, int i, int j) {
		if (!areNeighbours(a, b, c, d, i, j))
			return false;
		oldField[a][b] = 0;
      	oldField[j][i] = 0;
		oldField[c][d] = 0;

//		pairs.add(List.of(a, b));
//		pairs.add(List.of(j, i));
//		pairs.add(List.of(c, d));

		noChanges = false;
		Score += (level + 1) * 10;
		removedCellsCounter++;
		return true;
	}

	void removeSimilarNeighboringCells() {
		noChanges = true;
		testField();
		if (noChanges)
			return;

		packField();
		changeScore();
		if (removedCellsCounter < Model.FigToDropForNextLevel)
			return;
		removedCellsCounter = 0;
		if (level < Model.MaxLevel) {
			increaseLevel();
		}
	}

	void removeSimilarCellsRepeatedly() {
		do {
			removeSimilarNeighboringCells();
		} while (!noChanges);
	}

	//------------------------------------------------------------Level Operations------------------------------------------------------------------------

	private void changeLevel(int change) {
		level += change;
		level = Math.max(level, 1);
		level = Math.min(level, 10);
		level = Math.max(level, MinLevel);
	}

	public void decreaseLevel() {
		changeLevel(-1);
	}

	public void increaseLevel() {
		changeLevel(1);
	}


	//------------------------------------------------------------notify Listeners------------------------------------------------------------------------


	private void fireFieldUpdated() {
		for (ModelListener modelListener : listeners) {
			modelListener.fieldUpdated(this);
		}
	}

	private void fireGameOver() {
		for (ModelListener modelListener : listeners) {
			modelListener.gameOver();
		}
	}


	//------------------------------------------------------------operations------------------------------------------------------------------------

	public void moveLeftOrRight(int direction){
		int oldX = Fig.x;
		int oldY = Fig.y;

		if ( checkBorder(direction) && newField[oldY + 2][oldX + direction] == 0) {
			Fig.x += direction;
		}
	}

	public void tryMoveRight() {
		moveLeftOrRight(1);
	}

	public void tryMoveLeft() {
		moveLeftOrRight(-1);
	}

	public void rotateDown() {
		Fig.rotateFigureCellsRight();
	}

	public void rotateUp() {
		Fig.rotateFigureCellsLeft();
	}

	public void refresh() {
		initModel();
		initMembers();
		initMatrixes();
	}

}

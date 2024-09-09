package main.java.columns;


public class Controller implements ModelListener, GameEventsListener{

	Model model;
	View view;

	public Controller(Model model, View view){
		this.model = model;
		this.view = view;
		model.addListener(this);
	}

	@Override
	public void foundNeighboursAt(int i, int j, int k, int l, int i2, int j2) {
		// Optional: Add visual highlighting or other feedback
	}

	@Override
	public void fieldUpdated(Model model) {
		view.updateField(model);
	}

	@Override
	public void gameOver() {
		view.gameOver();
	}
	//<---------------------------------GameEventListener------------------------------------>
	@Override
	public void trySlideDown() {
		model.trySlideDown();
	}

	@Override
	public void tryMoveRight() {
		model.tryMoveRight();
	}

	@Override
	public void tryMoveLeft() {
		model.tryMoveLeft();
	}

	@Override
	public void dropFigure() {
		model.dropFigure();
	}

	@Override
	public void rotateUp() {
		model.rotateUp();
	}

	@Override
	public void rotateDown() {
		model.rotateDown();
	}

	@Override
	public void pause() {
		model.pause();
	}

	@Override
	public void increaseLevel() {
		model.increaseLevel();
	}

	@Override
	public void decreaseLevel() {
		model.decreaseLevel();
	}
}

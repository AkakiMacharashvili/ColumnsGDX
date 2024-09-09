package main.java.kiu.columns;

import com.badlogic.gdx.ScreenAdapter;

public class ColumnScreen extends ScreenAdapter {

	ColumnStage _stage;

	@Override
	public void show() {
		_stage = new ColumnStage();
		_stage.init();
	}

	@Override
	public void render(float delta) {
        _stage.act(delta);
        _stage.draw();
	}

	@Override
	public void resize(int width, int height) {
        _stage.getViewport().update(width, height, true);
	}
}

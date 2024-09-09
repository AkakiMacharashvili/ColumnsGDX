package main.java.columns;

public class DrawDataParameter {
	public int[][] fs;
	public int[] ft;
	public int row;
	public int col;
	public boolean drawBackground;

	public DrawDataParameter(int[][] fs, int row, int col, boolean drawBackground) {
		this.fs = fs;
		this.row = row;
		this.col = col;
		this.drawBackground = drawBackground;
	}

	public DrawDataParameter(int[] ft, int row, int col, boolean drawBackground) {
		this.ft = ft;
		this.row = row;
		this.col = col;
		this.drawBackground = drawBackground;
	}
}
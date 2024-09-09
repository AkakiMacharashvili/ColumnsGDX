package main.java.columns;

import java.util.Random;

class Figure {

	static Random r = new Random();

	static void rotateArraysElementsLeft(int[] c) {
		int i = c[0];
		c[0] = c[1];
		c[1] = c[2];
		c[2] = i;
	}

	static void rotateArraysElementsRight(int[] c) {
		int i = c[0];
		c[0] = c[2];
		c[2] = c[1];
		c[1] = i;
	}


	int x;
    int y;

	public int[] c = new int[3];

	public Figure() {
		x = Model.Width / 2 - 1;
		y = 0;
		c[0] = (int) (Math.abs(r.nextInt()) % 7 + 1);
		c[1] = (int) (Math.abs(r.nextInt()) % 7 + 1);
		c[2] = (int) (Math.abs(r.nextInt()) % 7 + 1);
	}

	void rotateFigureCellsRight() {
		rotate(1);
	}

	void rotateFigureCellsLeft() {
		rotate(-1);
	}

	public Figure empty(){
		return new Figure();
	}

	void rotate(int k) {
		k += c.length;
		k %= c.length;
		reverse(c, 0, c.length - 1);
		reverse(c, 0, k - 1);
		reverse(c, k, c.length - 1);
	}

	private static void reverse(int[] c, int l, int r) {
		if (r < l) {
			return;
		}
		for (int i = l; i <= (l + r) / 2; i++) {
			int temp = c[i];
			c[i]  =  c[l + r - i];
			c[l + r - i] = temp;
		}
	}

	public int[] getCells() {
		return c;
	}

}
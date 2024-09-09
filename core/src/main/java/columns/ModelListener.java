package main.java.columns;

public interface ModelListener {

    void foundNeighboursAt(int i, int j, int k, int l, int i2, int j2);

    void fieldUpdated(Model model);

    void gameOver();

}

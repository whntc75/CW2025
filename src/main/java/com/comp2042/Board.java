package com.comp2042;

public interface Board {

    boolean holdCurrentBrick();

    int[][] getHoldBrickShape();

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    boolean holdCurrentBrick();//hold

    int[][] getHoldBrickShape();//hold

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();
}

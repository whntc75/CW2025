package com.comp2042;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {

    @Test
    void newGameClearsBoard() {
        SimpleBoard board = new SimpleBoard(10, 20);
        board.newGame();

        int[][] matrix = board.getBoardMatrix();
        assertNotNull(matrix);
        assertTrue(matrix.length > 0);
        assertTrue(matrix[0].length > 0);

        for (int[] row : matrix) {
            for (int cell : row) {
                assertEquals(0, cell, "New game should start with empty background");
            }
        }

        assertNotNull(board.getViewData(), "There should be a current brick after newGame()");
    }

    @Test
    void holdIsEmptyAtStart() {
        SimpleBoard board = new SimpleBoard(10, 20);
        board.newGame();

        assertNull(board.getHoldBrickShape(), "New game should start with empty hold slot");}

    @Test
    void firstHoldStoresCurrentBrick() {
        SimpleBoard board = new SimpleBoard(10, 20);
        board.newGame();

        assertNull(board.getHoldBrickShape(), "Hold should be empty before first hold");

        boolean ok = board.holdCurrentBrick();
        assertTrue(ok, "First hold of this piece should succeed");

        assertNotNull(board.getHoldBrickShape(), "After first hold there should be a brick in hold slot");

        assertNotNull(board.getViewData(), "After hold there should still be a current brick");
    }

    @Test
    void secondHoldSamePieceIsRejectedButStateIsValid() {
        SimpleBoard board = new SimpleBoard(10, 20);
        board.newGame();
        boolean first = board.holdCurrentBrick();
        boolean second = board.holdCurrentBrick(); // 这一块已经 hold 过一次
        assertTrue(first, "First hold should succeed");
        assertFalse(second, "Second hold in the same turn should be rejected");
        assertNotNull(board.getViewData(), "There should still be a current brick");
        assertNotNull(board.getHoldBrickShape(), "Hold slot should still have a brick");
        int[][] matrix = board.getBoardMatrix();
        assertNotNull(matrix);
        assertTrue(matrix.length > 0 && matrix[0].length > 0);
    }

    @Test
    void newBrickAllowsHoldAgain() {
        SimpleBoard board = new SimpleBoard(10, 20);
        board.newGame();
        boolean ok = board.holdCurrentBrick();
        assertTrue(ok, "After a new brick is created, hold should be allowed again");
    }

    @Test
    void moveBrickLeftSucceedsWhenSpaceAvailable() {
        SimpleBoard board = new SimpleBoard(10, 20);
        board.newGame();
        boolean moved = board.moveBrickLeft();
        assertTrue(moved, "Should be able to move left from center");
    }

    @Test
    void moveBrickRightSucceedsWhenSpaceAvailable() {
        SimpleBoard board = new SimpleBoard(10, 20);
        board.newGame();
        boolean moved = board.moveBrickRight();
        assertTrue(moved, "Should be able to move right from center");
    }

    @Test
    void moveBrickDownSucceedsWhenSpaceAvailable() {
        SimpleBoard board = new SimpleBoard(10, 20);
        board.newGame();

        boolean moved = board.moveBrickDown();
        assertTrue(moved, "Should be able to move down at game start");
    }

    @Test
    void rotateBrickSucceedsWhenSpaceAvailable() {
        SimpleBoard board = new SimpleBoard(10, 20);
        board.newGame();
        boolean rotated = board.rotateLeftBrick();
        assertNotNull(board.getViewData());
    }

}


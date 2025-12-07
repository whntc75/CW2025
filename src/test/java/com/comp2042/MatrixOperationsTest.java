package com.comp2042;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void intersectReturnsFalseWhenNoOverlap() {
        int[][] board = new int[4][4];
        int[][] brick = {{1}};

        boolean result = MatrixOperations.intersect(board, brick, 1, 1);

        assertFalse(result, "Empty board should not intersect with a 1x1 brick inside bounds");
    }

    @Test
    void intersectReturnsTrueWhenOverlappingExistingBlock() {
        int[][] board = new int[4][4];
        board[2][2] = 9;

        int[][] brick = {{1}};

        boolean result = MatrixOperations.intersect(board, brick, 2, 2);

        assertTrue(result, "Brick placed on an existing block should report intersection");
    }

    @Test
    void intersectReturnsTrueWhenOutOfBounds() {
        int[][] board = new int[4][4];
        int[][] brick = {{1}};

        boolean result = MatrixOperations.intersect(board, brick, -1, 0);

        assertTrue(result, "Brick placed partially out of bounds should report intersection");
    }

    @Test
    void copyCreatesDeepCopy() {
        int[][] original = {
                {1, 2},
                {3, 4}
        };

        int[][] copy = MatrixOperations.copy(original);

        assertNotSame(original, copy, "Copy should be a different array object");
        assertNotSame(original[0], copy[0], "Inner arrays should also be different objects");

        assertArrayEquals(original[0], copy[0], "First row should be equal");
        assertArrayEquals(original[1], copy[1], "Second row should be equal");

        original[0][0] = 99;
        assertEquals(1, copy[0][0], "Modifying original should not change the copy");
    }

    @Test
    void mergePlacesBrickOnBoard() {
        int[][] board = new int[4][4];
        int[][] brick = {{5}};
        int[][] merged = MatrixOperations.merge(board, brick, 1, 2);
        assertEquals(5, merged[2][1],
                "Brick value should be written into target position when merged");
    }

    @Test
    void checkRemovingClearsFullRowsAndShiftsDown() {

        int[][] board = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {2, 2, 2, 2},
                {0, 3, 0, 3}
        };

        ClearRow result = MatrixOperations.checkRemoving(board);


        assertEquals(2, result.getLinesRemoved(), "Should clear exactly 2 full rows");

        int[][] newMatrix = result.getNewMatrix();

        assertArrayEquals(new int[]{0, 3, 0, 3}, newMatrix[3],
                "Bottom row should be the last non-cleared row");
        assertArrayEquals(new int[]{0, 0, 0, 0}, newMatrix[2],
                "Row above should be the first non-cleared row");

        assertArrayEquals(new int[]{0, 0, 0, 0}, newMatrix[0],
                "Top row should be empty after clearing");
        assertArrayEquals(new int[]{0, 0, 0, 0}, newMatrix[1],
                "Second row should also be empty after clearing");

        assertEquals(200, result.getScoreBonus(), "Score bonus should follow 50 * n^2 rule");
    }

    @Test
    void deepCopyListCreatesIndependentCopies() {
        int[][] m1 = {
                {1, 2},
                {3, 4}
        };
        int[][] m2 = {
                {5, 6},
                {7, 8}
        };

        List<int[][]> original = new ArrayList<>();
        original.add(m1);
        original.add(m2);

        List<int[][]> copied = MatrixOperations.deepCopyList(original);


        assertNotSame(original, copied, "Copied list should be a different object");

        assertNotSame(original.get(0), copied.get(0),
                "First matrix in list should be a different object");
        assertNotSame(original.get(1), copied.get(1),
                "Second matrix in list should be a different object");

        assertArrayEquals(original.get(0)[0], copied.get(0)[0], "Row 0 of first matrix should match");
        assertArrayEquals(original.get(1)[1], copied.get(1)[1], "Row 1 of second matrix should match");

        original.get(0)[0][0] = 99;
        assertEquals(1, copied.get(0)[0][0],
                "Changing original matrix should not affect copied matrix");
    }
}

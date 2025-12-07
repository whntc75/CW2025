package com.comp2042;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearRowTest {

    @Test
    void constructorStoresValuesCorrectly() {
        int[][] matrix = {{1, 2}, {3, 4}};
        ClearRow clearRow = new ClearRow(2, matrix, 200);

        assertEquals(2, clearRow.getLinesRemoved());
        assertEquals(200, clearRow.getScoreBonus());
    }

    @Test
    void getNewMatrixReturnsDeepCopy() {
        int[][] matrix = {{1, 2}, {3, 4}};
        ClearRow clearRow = new ClearRow(1, matrix, 50);

        int[][] result = clearRow.getNewMatrix();

        assertNotSame(matrix, result, "Should return a copy, not the same array");
        assertArrayEquals(matrix[0], result[0]);

        // Modify original, copy should not change
        matrix[0][0] = 99;
        assertEquals(1, result[0][0], "Copy should be independent");
    }
}

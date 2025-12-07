package com.comp2042;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewDataTest {

    @Test
    void constructorStoresAllFields() {
        int[][] brick = {{1, 1}, {1, 0}};
        int[][] next = {{2, 2}, {2, 2}};
        int[][] hold = {{3, 3}, {0, 3}};

        ViewData viewData = new ViewData(brick, 5, 10, next, hold);

        assertEquals(5, viewData.getxPosition());
        assertEquals(10, viewData.getyPosition());
        assertArrayEquals(brick, viewData.getBrickData());
        assertArrayEquals(next, viewData.getNextBrickData());
        assertArrayEquals(hold, viewData.getHoldBrickData());
    }

    @Test
    void nullHoldBrickIsAllowed() {
        int[][] brick = {{1}};
        int[][] next = {{2}};

        ViewData viewData = new ViewData(brick, 0, 0, next, null);

        assertNull(viewData.getHoldBrickData(), "Hold can be null at game start");
    }
}

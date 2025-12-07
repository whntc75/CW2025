package com.comp2042;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    void initialValuesAreCorrect() {
        Score score = new Score();

        assertEquals(0, score.scoreProperty().get(),
                "Initial score should be 0");
        assertEquals(0, score.linesProperty().get(),
                "Initial lines should be 0");
        assertEquals(1, score.getLevel(),
                "Initial level should be 1");
    }

    @Test
    void levelIncreasesEvery10Lines() {
        Score score = new Score();

        score.addLines(9);
        assertEquals(9, score.linesProperty().get(),
                "Lines should be 9 after adding 9");
        assertEquals(1, score.getLevel(),
                "Level should still be 1 before reaching 10 lines");

        score.addLines(1);
        assertEquals(10, score.linesProperty().get(),
                "Lines should be 10 after adding 1 more");
        assertEquals(2, score.getLevel(),
                "Level should be 2 when total lines = 10");

        score.addLines(20); // total = 30
        assertEquals(30, score.linesProperty().get(),
                "Lines should be 30 after adding 20 more");
        assertEquals(4, score.getLevel(),
                "Level should be 4 when total lines = 30 (30/10 + 1)");
    }

    @Test
    void levelIsCappedAt18() {
        Score score = new Score();
        score.addLines(999);

        assertEquals(18, score.getLevel(),
                "Level should never exceed 18 even after many lines");
    }

    @Test
    void resetResetsAllValues() {
        Score score = new Score();
        score.add(100);
        score.addLines(50);

        score.reset();

        assertEquals(0, score.scoreProperty().get(),
                "Score should be reset to 0");
        assertEquals(0, score.linesProperty().get(),
                "Lines should be reset to 0");
        assertEquals(1, score.getLevel(),
                "Level should be reset to 1");
    }
}


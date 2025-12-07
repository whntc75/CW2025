package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BagBrickGeneratorTest {

    @Test
    void firstSevenBricksContainAllTypesExactlyOnce() {
        BagBrickGenerator generator = new BagBrickGenerator();

        Set<Class<?>> types = new HashSet<>();
        for (int i = 0; i < 7; i++) {
            Brick b = generator.getBrick();
            assertNotNull(b, "Brick should not be null");
            types.add(b.getClass());
        }

        // 7-bag：一袋里应该刚好 7 种不同形状
        assertEquals(7, types.size(), "First bag should contain 7 different brick types");
    }

    @Test
    void nextBrickMatchesUpcomingBrick() {
        BagBrickGenerator generator = new BagBrickGenerator();

        // 先看一眼 next
        Brick next = generator.getNextBrick();
        assertNotNull(next, "nextBrick should not be null");

        // 调用 getBrick() 后，应该正好拿到刚才预览的那个
        Brick actual = generator.getBrick();

        assertEquals(next.getClass(), actual.getClass(),
                "getNextBrick() must preview the same type that getBrick() will return next");
    }

    @Test
    void bagRefillsAfterSevenBricks() {
        BagBrickGenerator generator = new BagBrickGenerator();

        // 第一袋 7 个
        for (int i = 0; i < 7; i++) {
            assertNotNull(generator.getBrick(), "Brick in first bag should not be null");
        }

        // 再取一个，应该来自第二袋，而不是 null
        Brick eighth = generator.getBrick();
        assertNotNull(eighth, "Bag should refill automatically after 7 bricks");
    }
}

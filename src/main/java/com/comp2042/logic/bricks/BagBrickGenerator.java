package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Collections;

/**
 * 7-bag Tetromino Generator:
 * Each bag contains exactly 7 distinct Tetromino shapes (one each of I, J, L, O, S, T, Z).
 * The shapes are shuffled randomly first, then distributed one by one.
 * After all 7 shapes are used up, a new bag will be generated.
 * This avoids the problem of a certain shape not appearing for a long time or spawning repeatedly in quick succession,
 * thus improving the gaming experience.
 */
public class BagBrickGenerator implements BrickGenerator {

    private static final List<Brick> BRICK_TEMPLATES = List.of(
            new IBrick(), new JBrick(), new LBrick(), new OBrick(),
            new SBrick(), new TBrick(), new ZBrick()
    );
    /** The queue of tetrominoes waiting to be distributed (a new bag will be automatically added once the current bag is used up) */
    private final Deque<Brick> queue = new ArrayDeque<>();

    /** A bag of tetrominoes is generated first during initialization */
    public BagBrickGenerator() { refillBag(); }

    /** Generate a new bag of tetrominoes: Copy 7 tetrominoes from the template, shuffle them randomly, and push all of them to the end of the queue. */
    private void refillBag() {List<Brick> bag = new ArrayList<>(BRICK_TEMPLATES);Collections.shuffle(bag);queue.addAll(bag);
    }

    /** Retrieve the tetromino to fall currently (it will be removed from the queue).
     * If the queue is empty, a new bag of tetrominoes will be added first before retrieval. */
    @Override
    public Brick getBrick() {
        if (queue.isEmpty()) { refillBag(); }
        return queue.pollFirst();
    }

    /** Preview the next tetromino (it will not be removed from the queue).
     * If the queue is empty, a new bag will also be added first to ensure a tetromino is always available for preview. */
    @Override
    public Brick getNextBrick() {
        if (queue.isEmpty()) { refillBag(); }
        return queue.peekFirst();
    }
}

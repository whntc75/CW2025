package com.comp2042;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for GameController class.
 * Tests event handling and game coordination.
 */
class GameControllerTest {

    private GameController gameController;
    private SimpleBoard board;

    @BeforeEach
    void setUp() {
        // Create controller with null GuiController for unit testing
        // This tests the logic without UI dependencies
        gameController = new GameController(null);
    }

    @Test
    @DisplayName("onDownEvent should return valid DownData")
    void testOnDownEventReturnsValidData() {
        gameController.createNewGame();

        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        DownData result = gameController.onDownEvent(event);

        assertNotNull(result, "DownData should not be null");
        assertNotNull(result.getViewData(), "ViewData should not be null");
    }

    @Test
    @DisplayName("onLeftEvent should return valid ViewData")
    void testOnLeftEventReturnsValidData() {
        gameController.createNewGame();

        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
        ViewData result = gameController.onLeftEvent(event);

        assertNotNull(result, "ViewData should not be null");
        assertNotNull(result.getBrickData(), "Brick data should not be null");
    }

    @Test
    @DisplayName("onRightEvent should return valid ViewData")
    void testOnRightEventReturnsValidData() {
        gameController.createNewGame();

        MoveEvent event = new MoveEvent(EventType.RIGHT, EventSource.USER);
        ViewData result = gameController.onRightEvent(event);

        assertNotNull(result, "ViewData should not be null");
        assertNotNull(result.getBrickData(), "Brick data should not be null");
    }

    @Test
    @DisplayName("onRotateEvent should return valid ViewData")
    void testOnRotateEventReturnsValidData() {
        gameController.createNewGame();

        MoveEvent event = new MoveEvent(EventType.ROTATE, EventSource.USER);
        ViewData result = gameController.onRotateEvent(event);

        assertNotNull(result, "ViewData should not be null");
    }

    @Test
    @DisplayName("createNewGame should reset game state")
    void testCreateNewGameResetsState() {
        // Make some moves
        gameController.createNewGame();
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        gameController.onDownEvent(event);

        // Reset game
        gameController.createNewGame();

        // Should be able to get fresh view data
        ViewData viewData = gameController.onLeftEvent(
                new MoveEvent(EventType.LEFT, EventSource.USER));
        assertNotNull(viewData, "Should have valid view data after new game");
    }
}

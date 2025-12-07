package com.comp2042;

public interface InputEventListener {
    
    ViewData onHoldEvent(MoveEvent event);

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
}

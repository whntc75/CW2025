package com.comp2042;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PausePanel extends VBox {

    private final Button resumeButton;
    private final Button newGameButton;
    private final Button menuButton;


    public PausePanel() {
        setAlignment(Pos.CENTER);
        setSpacing(15);
        getStyleClass().add("pause-panel");

        Label pauseLabel = new Label("PAUSED");
        pauseLabel.getStyleClass().add("pauseStyle");

        resumeButton = new Button("Resume (P)");
        resumeButton.getStyleClass().add("pause-button");
        resumeButton.setFocusTraversable(false);

        newGameButton = new Button("New Game (N)");
        newGameButton.getStyleClass().add("pause-button");
        newGameButton.setFocusTraversable(false);
        // Main Menu button
        menuButton = new Button("Main Menu (ESC)");
        menuButton.getStyleClass().add("pause-button");
        menuButton.setFocusTraversable(false);


        getChildren().addAll(pauseLabel, resumeButton, newGameButton, menuButton);

    }

    public Button getResumeButton() {return resumeButton;}

    public Button getNewGameButton() {return newGameButton;}

    public Button getMenuButton() {return menuButton;}

}

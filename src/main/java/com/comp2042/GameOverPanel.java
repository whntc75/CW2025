package com.comp2042;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameOverPanel extends VBox {

    private final Label bestScoreLabel;

    public GameOverPanel() {
        setAlignment(Pos.CENTER);
        setSpacing(10);

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        bestScoreLabel = new Label();
        bestScoreLabel.getStyleClass().add("bestScoreStyle");

        getChildren().addAll(gameOverLabel, bestScoreLabel);
    }

    public void showBestScore(int bestScore, boolean isNewRecord) {
        if (isNewRecord) {
            bestScoreLabel.setText("NEW RECORD: " + bestScore);
            bestScoreLabel.setStyle("-fx-text-fill: #2ECC71;");
        } else {
            bestScoreLabel.setText("Best: " + bestScore);
            bestScoreLabel.setStyle("-fx-text-fill: #F1C40F;");
        }
    }
}

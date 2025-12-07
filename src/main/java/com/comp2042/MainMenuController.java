package com.comp2042;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private Button singlePlayerBtn;

    @FXML
    public void onSinglePlayer() {
        startGame(GameMode.SINGLE_PLAYER, null);
    }

    @FXML
    public void onTwoPlayer() {
        startGame(GameMode.TWO_PLAYER, null);
    }

    @FXML
    public void onVsAiEasy() {
        startGame(GameMode.VS_AI, AIDifficulty.EASY);
    }

    @FXML
    public void onVsAiMedium() {
        startGame(GameMode.VS_AI, AIDifficulty.MEDIUM);
    }

    @FXML
    public void onVsAiHard() {
        startGame(GameMode.VS_AI, AIDifficulty.HARD);
    }

    private void startGame(GameMode mode, AIDifficulty difficulty) {
        try {
            Stage stage = (Stage) singlePlayerBtn.getScene().getWindow();

            if (mode == GameMode.SINGLE_PLAYER) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
                Parent root = loader.load();
                GuiController controller = loader.getController();

                Scene scene = new Scene(root, 410, 490);
                stage.setScene(scene);
                new GameController(controller);
            } else {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/battleLayout.fxml"));
                Parent root = loader.load();
                BattleGuiController controller = loader.getController();

                Scene scene = new Scene(root, 750, 550);
                stage.setScene(scene);
                controller.initBattleGame(mode, difficulty);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

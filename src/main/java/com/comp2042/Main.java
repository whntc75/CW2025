package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //load main menu
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 410, 490);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

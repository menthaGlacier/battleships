package ru.metapunk.battleships;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/ru/metapunk/battleships/fxml/main-view.fxml")));
        Scene scene = new Scene(loader.load(), 1280, 720);
        stage.setTitle("Battleships");
        stage.setScene(scene);
        stage.show();
    }
}
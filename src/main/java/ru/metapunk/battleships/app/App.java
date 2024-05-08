package ru.metapunk.battleships.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader((getClass()
                .getResource("/ru/metapunk/battleships/fxml/main-view.fxml")));
        final Scene scene = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Battleships");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
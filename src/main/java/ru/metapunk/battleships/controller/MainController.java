package ru.metapunk.battleships.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private AnchorPane root;
    @FXML
    private TextField nicknameTextField;

    public MainController() {

    }

    @FXML
    private void onJoinGameButtonClick() {
        final Stage dialog = new Stage();
        FXMLLoader loader = new FXMLLoader((getClass()
                .getResource("/ru/metapunk/battleships/fxml/join-game-view.fxml")));
        loader.setControllerFactory(controllerClass->
                new JoinGameController(dialog));

        try {
            dialog.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + e.getCause());
        }

        dialog.setTitle("Join game");
        dialog.setResizable(false);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(root.getScene().getWindow());
        dialog.showAndWait();
    }

    @FXML
    private void onHostGameButtonClick() {
        // TODO
    }

    @FXML
    private void onExitButtonClick() {
        // TODO
    }
}

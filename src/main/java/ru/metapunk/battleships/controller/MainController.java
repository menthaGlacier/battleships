package ru.metapunk.battleships.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.metapunk.battleships.net.Client;
import ru.metapunk.battleships.net.observer.IClientObserver;

import java.io.IOException;

public class MainController implements IClientObserver {
    @FXML
    private AnchorPane root;
    @FXML
    private TextField nicknameTextField;

    private final Client client;

    public MainController() {
        this.client = new Client();
    }

    @Override
    public void onLobbyCreated() {

    }

    @FXML
    private void onJoinGameButtonClick() {
        final Stage dialog = new Stage();
        FXMLLoader loader = new FXMLLoader((getClass()
                .getResource("/ru/metapunk/battleships/fxml/join-game-view.fxml")));
        loader.setControllerFactory(controllerClass->
                new JoinGameController(dialog, client));

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
    }

    @FXML
    private void onExitButtonClick() {
        // TODO
    }
}

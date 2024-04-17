package ru.metapunk.battleships.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.metapunk.battleships.net.Client;
import ru.metapunk.battleships.net.dto.request.CreateLobbyRequestDto;
import ru.metapunk.battleships.net.observer.IClientObserver;

import java.io.IOException;
import java.util.Objects;

public class MainController implements IClientObserver {
    @FXML
    private AnchorPane root;
    @FXML
    private TextField nicknameTextField;

    private final Client client;

    public MainController() {
        this.client = new Client();
        this.client.setEventsObserver(this);
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

        client.setEventsObserver(this);
    }

    @FXML
    private void onHostGameButtonClick() {
        String nickname = nicknameTextField.getText();
        if (Objects.equals(nickname, "")) {
            nickname = "player"; // TODO Append random number for unique names
        }

        client.sendDto(new CreateLobbyRequestDto(nickname));
    }

    @FXML
    private void onExitButtonClick() {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void onLobbyCreated() {
        Platform.runLater(() -> {
            final Stage dialog = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/ru/metapunk/battleships/fxml/awaiting-player-view.fxml"));
            loader.setControllerFactory(controllerClass ->
                    new AwaitingPlayerController(dialog, client));

            try {
                dialog.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                System.out.println(e.getMessage() + "\n" + e.getCause());
            }

            dialog.setTitle("Awaiting...");
            dialog.setResizable(false);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(root.getScene().getWindow());
            dialog.showAndWait();

            client.setEventsObserver(this);
        });
    }
}

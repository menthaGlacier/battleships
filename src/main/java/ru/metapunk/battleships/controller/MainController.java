package ru.metapunk.battleships.controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.metapunk.battleships.model.board.Board;
import ru.metapunk.battleships.model.tile.cell.Cell;
import ru.metapunk.battleships.net.Client;
import ru.metapunk.battleships.net.dto.PlayerBoardSetupDto;
import ru.metapunk.battleships.net.dto.request.CreateLobbyRequestDto;
import ru.metapunk.battleships.net.dto.response.CreateLobbyResponseDto;
import ru.metapunk.battleships.observer.IClientObserver;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class MainController implements IClientObserver {
    @FXML
    private AnchorPane root;
    @FXML
    private TextField nicknameTextField;

    private final Client client;

    public MainController() {
        this.client = new Client();
        this.client.setEventsObserver(this);
        new Thread(client).start();
    }

    @FXML
    private void onJoinGameButtonClick() {
        StringProperty joinedLobbyId = new SimpleStringProperty();
        final Stage dialog = new Stage();
        FXMLLoader loader = new FXMLLoader((getClass()
                .getResource("/ru/metapunk/battleships/fxml/join-game-view.fxml")));
        loader.setControllerFactory(controllerClass->
                new JoinGameController(dialog, client, getNickname(), joinedLobbyId));

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
        if (!joinedLobbyId.get().equals("None")) {
            prepareForGame(joinedLobbyId.get());
        }
    }

    @FXML
    private void onHostGameButtonClick() {
        client.sendDto(new CreateLobbyRequestDto(client.getClientId(),getNickname()));
    }

    @FXML
    private void onExitButtonClick() {
        Platform.exit();
        System.exit(0);
    }

    private String getNickname() {
        String nickname = nicknameTextField.getText();
        if (Objects.equals(nickname, "")) {
            nickname = "Player_" + ThreadLocalRandom.current()
                    .nextInt(1000, 9999 + 1);
        }

        return nickname;
    }

    private void prepareForGame(String lobbyId) {
        final Cell[][] cells = new Cell[Board.DEFAULT_ROWS][Board.DEFAULT_COLUMNS];
        callShipPlacementDialog(cells);
        client.sendDto(new PlayerBoardSetupDto(lobbyId, client.getClientId(), cells));
    }

    private void callShipPlacementDialog(Cell[][] cells) {
        final Stage dialog = new Stage();
        FXMLLoader loader = new FXMLLoader((getClass()
                .getResource("/ru/metapunk/battleships/fxml/placement-view.fxml")));
        loader.setControllerFactory(controllerClass->
                new PlacementController(dialog, cells));

        try {
            dialog.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + e.getCause());
        }

        dialog.setTitle("Place your ships");
        dialog.setResizable(false);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(root.getScene().getWindow());
        dialog.showAndWait();
    }

    @Override
    public void onLobbyCreated(CreateLobbyResponseDto createLobbyResponseDto) {
        final BooleanProperty otherPlayerJoinedProperty = new SimpleBooleanProperty();
        Platform.runLater(() -> {
            final Stage dialog = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/ru/metapunk/battleships/fxml/awaiting-player-view.fxml"));
            loader.setControllerFactory(controllerClass ->
                    new AwaitingPlayerController(dialog, client,
                            otherPlayerJoinedProperty));

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
        });

        client.setEventsObserver(this);
        if (otherPlayerJoinedProperty.get()) {
            prepareForGame(createLobbyResponseDto.getLobbyId());
        }
    }
}

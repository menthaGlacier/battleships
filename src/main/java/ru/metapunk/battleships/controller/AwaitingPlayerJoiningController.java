package ru.metapunk.battleships.controller;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import ru.metapunk.battleships.net.client.Client;
import ru.metapunk.battleships.observer.IClientLobbyAwaitingObserver;

public class AwaitingPlayerJoiningController implements IClientLobbyAwaitingObserver {
    private final Stage stage;
    private final Client client;
    private final StringProperty joinedGameIdProperty;

    public AwaitingPlayerJoiningController(Stage stage, Client client,
                                           StringProperty joinedGameIdProperty) {
        this.stage = stage;
        this.client = client;
        this.joinedGameIdProperty = joinedGameIdProperty;

        this.client.setEventsObserver(this);
    }

    @FXML
    private void onCancelButtonClick() {
        // TODO Terminate lobby
        joinedGameIdProperty.set("None");
        Platform.runLater(stage::close);
    }

    @Override
    public void onOtherPlayerJoined(String gameId) {
        joinedGameIdProperty.set(gameId);
        Platform.runLater(stage::close);
    }
}

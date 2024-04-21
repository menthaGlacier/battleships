package ru.metapunk.battleships.controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import ru.metapunk.battleships.net.Client;
import ru.metapunk.battleships.observer.IClientLobbyAwaitingObserver;

public class AwaitingPlayerController implements IClientLobbyAwaitingObserver {
    private final Stage stage;
    private final Client client;
    private final BooleanProperty otherPlayerJoinedProperty;

    public AwaitingPlayerController(Stage stage, Client client,
                                    BooleanProperty otherPlayerJoinedProperty) {
        this.stage = stage;
        this.client = client;
        this.otherPlayerJoinedProperty = otherPlayerJoinedProperty;

        this.client.setEventsObserver(this);
    }

    @FXML
    private void onCancelButtonClick() {
        // TODO Terminate lobby
        otherPlayerJoinedProperty.set(false);
        Platform.runLater(stage::close);
    }

    @Override
    public void onOtherPlayerJoined() {
        otherPlayerJoinedProperty.set(true);
        Platform.runLater(stage::close);
    }
}

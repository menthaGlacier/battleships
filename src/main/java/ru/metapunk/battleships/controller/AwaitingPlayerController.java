package ru.metapunk.battleships.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import ru.metapunk.battleships.net.Client;
import ru.metapunk.battleships.net.observer.IClientLobbyAwaitingObserver;

public class AwaitingPlayerController implements IClientLobbyAwaitingObserver {
    private final Stage stage;
    private final Client client;

    public AwaitingPlayerController(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;

        this.client.setEventsObserver(this);
    }

    @FXML
    private void onCancelButtonClick() {
        stage.close();
    }
}

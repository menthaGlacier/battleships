package ru.metapunk.battleships.controller;

import javafx.application.Platform;
import javafx.stage.Stage;
import ru.metapunk.battleships.net.client.Client;
import ru.metapunk.battleships.observer.IClientGameAwaitingObserver;

public class AwaitingPlayerReadinessController implements IClientGameAwaitingObserver {
    private final Stage stage;
    private final Client client;

    public AwaitingPlayerReadinessController(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;

        this.client.setEventsObserver(this);
    }

    @Override
    public void onOtherPlayerReady() {
        Platform.runLater(stage::close);
    }
}

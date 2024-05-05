package ru.metapunk.battleships.controller;

import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class GameFinishedController {
    @FXML
    private Label winnerLoserLabel;

    private final Stage stage;
    private final boolean hasPlayerWon;

    public GameFinishedController(Stage stage, boolean hasPlayerWon) {
        this.stage = stage;
        this.hasPlayerWon = hasPlayerWon;
    }

    @FXML
    private void onExitButtonClick() {
        stage.close();
    }

    @FXML
    private void initialize() {
        winnerLoserLabel.setText("You've " + (hasPlayerWon ? "won!" : "lost"));
    }
}

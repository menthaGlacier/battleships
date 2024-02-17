package ru.metapunk.battleships.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.metapunk.battleships.net.Client;

public class JoinGameController {
    @FXML
    private ListView<HBox> lobbyListView;

    private final Stage stage;
    private final Client client;

    public JoinGameController(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
    }

    private void updateLobbyList() {
        String[] dummyLobbies = {"Lobby 1", "Lobby2"};

        lobbyListView.getItems().clear();

        for (String lobby : dummyLobbies) {
            Label lobbyLabel = new Label(lobby);
            lobbyLabel.setFont(new Font(26));

            Button joinButton = new Button("Join");
            joinButton.setFont(new Font(18));
            joinButton.setPrefWidth(100);
            joinButton.setOnAction(e -> onJoinButtonClick(lobby));

            HBox lobbyBox = new HBox(lobbyLabel, joinButton);
            lobbyBox.setSpacing(100);
            lobbyBox.setAlignment(Pos.CENTER);
            lobbyListView.getItems().add(lobbyBox);
        }
    }

    private void onJoinButtonClick(String lobby) {

    }

    @FXML
    private void onBackButtonClick() {
        stage.close();
    }

    @FXML
    private void initialize() {
        lobbyListView.setFixedCellSize(45);
        updateLobbyList();
    }
}

package ru.metapunk.battleships.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.metapunk.battleships.net.Client;
import ru.metapunk.battleships.net.Lobby;
import ru.metapunk.battleships.net.dto.request.OpenLobbiesRequestDto;
import ru.metapunk.battleships.net.dto.response.OpenLobbiesResponseDto;
import ru.metapunk.battleships.net.observer.IClientJoinGameObserver;

import java.util.List;

public class JoinGameController implements IClientJoinGameObserver {
    @FXML
    private ListView<HBox> lobbyListView;

    private final Stage stage;
    private final Client client;

    public JoinGameController(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;

        this.client.setEventsObserver(this);

        Platform.runLater(() -> {
            client.sendDto(new OpenLobbiesRequestDto());
        });
    }

    private void updateLobbyList(List<Lobby> lobbies) {
        lobbyListView.getItems().clear();

        if (lobbies.isEmpty()) {
            Label noLobbiesAvailable = new Label("No lobbies available :(");
            noLobbiesAvailable.setFont(new Font(26));
            HBox noLobbiesBox = new HBox(noLobbiesAvailable);
            lobbyListView.getItems().add(noLobbiesBox);
            return;
        }

        for (Lobby lobby : lobbies) {
            Label lobbyLabel = new Label(lobby.getPlayerOneNickname());
            lobbyLabel.setFont(new Font(26));

            Button joinButton = new Button("Join");
            joinButton.setFont(new Font(18));
            joinButton.setPrefWidth(100);
            joinButton.setOnAction(e -> onJoinButtonClick(lobby.getLobbyId()));

            HBox lobbyBox = new HBox(lobbyLabel, joinButton);
            lobbyBox.setSpacing(100);
            lobbyBox.setAlignment(Pos.CENTER);
            lobbyListView.getItems().add(lobbyBox);
        }
    }

    private void onJoinButtonClick(String lobbyId) {
        System.out.println(lobbyId);
    }

    @FXML
    private void onBackButtonClick() {
        stage.close();
    }

    @FXML
    private void initialize() {
        lobbyListView.setFixedCellSize(45);
    }

    @Override
    public void onLobbiesReceived(OpenLobbiesResponseDto lobbyListDto) {
        Platform.runLater(() -> {
            updateLobbyList(lobbyListDto.getLobbies());
        });
    }
}

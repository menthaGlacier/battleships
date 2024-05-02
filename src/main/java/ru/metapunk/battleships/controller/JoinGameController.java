package ru.metapunk.battleships.controller;

import javafx.application.Platform;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.metapunk.battleships.net.client.Client;
import ru.metapunk.battleships.net.Lobby;
import ru.metapunk.battleships.net.dto.request.JoinLobbyRequestDto;
import ru.metapunk.battleships.net.dto.request.OpenLobbiesRequestDto;
import ru.metapunk.battleships.net.dto.response.JoinLobbyResponseDto;
import ru.metapunk.battleships.net.dto.response.OpenLobbiesResponseDto;
import ru.metapunk.battleships.observer.IClientJoinGameObserver;

import java.util.List;

public class JoinGameController implements IClientJoinGameObserver {
    @FXML
    private ListView<HBox> lobbyListView;

    private final Stage stage;
    private final Client client;
    private final String nickname;
    private final StringProperty joinedGameId;

    public JoinGameController(Stage stage, Client client, String nickname,
                              StringProperty joinedGameId) {
        this.stage = stage;
        this.client = client;
        this.nickname = nickname;
        this.joinedGameId = joinedGameId;

        this.client.setEventsObserver(this);
        client.sendDto(new OpenLobbiesRequestDto());
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
            Label lobbyLabel = new Label(lobby.getHost().getNickname());
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
        client.sendDto(new JoinLobbyRequestDto(lobbyId, client.getClientId(), nickname));
    }

    @FXML
    private void onBackButtonClick() {
        Platform.runLater(stage::close);
    }

    @FXML
    private void onRefreshButtonClick() {
        client.sendDto(new OpenLobbiesRequestDto());
    }

    @FXML
    private void initialize() {
        lobbyListView.setFixedCellSize(45);
    }

    @Override
    public void onLobbiesReceived(OpenLobbiesResponseDto data) {
        Platform.runLater(() -> updateLobbyList(data.lobbies()));
    }

    @Override
    public void onJoinLobbyResponse(JoinLobbyResponseDto data) {
        if (data.isAllowed()) {
            joinedGameId.set(data.gameId());
            Platform.runLater(stage::close);
            return;
        }

        joinedGameId.set("None");
        client.sendDto(new OpenLobbiesRequestDto());
    }
}

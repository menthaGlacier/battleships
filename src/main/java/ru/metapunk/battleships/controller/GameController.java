package ru.metapunk.battleships.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import ru.metapunk.battleships.model.board.Board;
import ru.metapunk.battleships.model.ship.ShipsData;
import ru.metapunk.battleships.model.tile.Tile;
import ru.metapunk.battleships.model.tile.cell.Cell;
import ru.metapunk.battleships.net.client.Client;
import ru.metapunk.battleships.net.dto.request.ShotEnemyShipRequestDto;
import ru.metapunk.battleships.net.dto.request.WhoseTurnRequestDto;
import ru.metapunk.battleships.net.dto.response.WhoseTurnResponseDto;
import ru.metapunk.battleships.observer.IClientGameObserver;

public class GameController implements IClientGameObserver {
    @FXML
    private AnchorPane root;
    @FXML
    private Board playerBoard;
    @FXML
    private Board enemyBoard;
    @FXML
    private Label warSideTurnLabel;

    private final Client client;
    private final String gameId;
    private final Tile[][] playerTiles;
    private final Tile[][] enemyTiles;
    private final BooleanProperty isPlayerTurnProperty;

    public GameController(Client client, String gameId, Cell[][] playerCells) {
        this.client = client;
        this.gameId = gameId;
        this.playerTiles = new Tile[Board.DEFAULT_ROWS][Board.DEFAULT_COLUMNS];
        this.enemyTiles = new Tile[Board.DEFAULT_ROWS][Board.DEFAULT_COLUMNS];
        this.isPlayerTurnProperty = new SimpleBooleanProperty(false);

        Platform.runLater(() -> setPlayerShips(playerCells));

        this.client.setEventsObserver(this);
        this.client.sendDto(new WhoseTurnRequestDto(gameId, client.getClientId()));
    }

    private void handleEnemyTileClick(MouseEvent e, Tile tile) {
        if (e.getButton() == MouseButton.SECONDARY
                || !isPlayerTurnProperty.get()) {
            return;
        }

        int row = GridPane.getRowIndex(tile);
        int column = GridPane.getColumnIndex(tile);

        client.sendDto(new ShotEnemyShipRequestDto(
                gameId, client.getClientId(), row, column));
    }

    public void setPlayerShips(Cell[][] playerCells) {
        for (int row = 0; row < Board.DEFAULT_ROWS; row++) {
            for (int column = 0; column < Board.DEFAULT_COLUMNS; column++) {
                Tile playerTile = new Tile(playerCells[row][column]);
                playerTiles[row][column] = playerTile;
                playerBoard.add(playerTile, column, row);
            }
        }
    }

    @FXML
    public void initialize() {
        for (int row = 0; row < Board.DEFAULT_ROWS; row++) {
            for (int column = 0; column < Board.DEFAULT_COLUMNS; column++) {
                Tile enemyTile = new Tile();
                enemyTile.setOnMouseClicked(e -> handleEnemyTileClick(e, enemyTile));
                enemyTiles[row][column] = enemyTile;
                enemyBoard.add(enemyTile, column, row);
            }
        }

        warSideTurnLabel.textProperty().bind(Bindings.when(isPlayerTurnProperty)
                .then("Your turn").otherwise("Enemy turn"));
    }

    @Override
    public void onWhoseTurnResponse(WhoseTurnResponseDto whoseTurnResponseDto) {
        isPlayerTurnProperty.set(whoseTurnResponseDto.isPlayerTurn());
    }
}

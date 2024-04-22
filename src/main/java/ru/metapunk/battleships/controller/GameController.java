package ru.metapunk.battleships.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import ru.metapunk.battleships.model.board.Board;
import ru.metapunk.battleships.model.tile.Tile;
import ru.metapunk.battleships.net.client.Client;
import ru.metapunk.battleships.net.dto.request.ShotEnemyShipRequestDto;
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
    private Tile[][] playerTiles;
    private Tile[][] enemyTiles;

    public GameController(Client client, String gameId) {
        this.client = client;
        this.gameId = gameId;
        this.playerTiles = new Tile[Board.DEFAULT_ROWS][Board.DEFAULT_COLUMNS];
        this.enemyTiles = new Tile[Board.DEFAULT_ROWS][Board.DEFAULT_COLUMNS];

        this.client.setEventsObserver(this);
    }

    private void handleEnemyTileClick(MouseEvent e, Tile tile) {
        if (e.getButton() == MouseButton.SECONDARY) {
            return;
        }

        int row = GridPane.getRowIndex(tile);
        int column = GridPane.getColumnIndex(tile);

        client.sendDto(new ShotEnemyShipRequestDto(
                gameId, client.getClientId(), row, column));
    }

    @FXML
    public void initialize() {
        for (int row = 0; row < Board.DEFAULT_ROWS; row++) {
            for (int column = 0; column < Board.DEFAULT_COLUMNS; column++) {
                Tile playerTile = new Tile();
                Tile enemyTile = new Tile();

                playerTiles[row][column] = playerTile;
                enemyTiles[row][column] = enemyTile;

                playerBoard.add(playerTile, column, row);
                enemyBoard.add(playerTile, column, row);
            }
        }
    }
}

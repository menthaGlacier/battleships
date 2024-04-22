package ru.metapunk.battleships.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import ru.metapunk.battleships.model.board.Board;
import ru.metapunk.battleships.model.tile.Tile;
import ru.metapunk.battleships.model.tile.cell.Cell;
import ru.metapunk.battleships.model.tile.cell.CellWarSide;
import ru.metapunk.battleships.net.client.Client;

public class GameController {
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

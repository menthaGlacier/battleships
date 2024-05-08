package ru.metapunk.battleships.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.metapunk.battleships.model.board.Board;
import ru.metapunk.battleships.model.ship.ShipDirection;
import ru.metapunk.battleships.model.ship.ShipType;
import ru.metapunk.battleships.model.tile.Tile;
import ru.metapunk.battleships.model.tile.cell.Cell;
import ru.metapunk.battleships.model.tile.cell.CellShipPresence;
import ru.metapunk.battleships.model.tile.cell.CellType;
import ru.metapunk.battleships.model.tile.cell.CellWarSide;
import ru.metapunk.battleships.net.client.Client;
import ru.metapunk.battleships.net.dto.EnemyShotDto;
import ru.metapunk.battleships.net.dto.request.ShotEnemyTileRequestDto;
import ru.metapunk.battleships.net.dto.request.WhoseTurnRequestDto;
import ru.metapunk.battleships.net.dto.response.ShotEnemyTileResponseDto;
import ru.metapunk.battleships.net.dto.response.WhoseTurnResponseDto;
import ru.metapunk.battleships.net.dto.signal.GameFinishedSignalDto;
import ru.metapunk.battleships.observer.IClientGameObserver;

import java.io.IOException;

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
        this.playerTiles = new Tile[Board.MAX_ROWS][Board.MAX_COLUMNS];
        this.enemyTiles = new Tile[Board.MAX_ROWS][Board.MAX_COLUMNS];
        this.isPlayerTurnProperty = new SimpleBooleanProperty(false);

        Platform.runLater(() -> setPlayerShips(playerCells));

        this.client.setEventsObserver(this);
        this.client.sendDto(new WhoseTurnRequestDto(gameId, client.getClientId()));
    }

    private void changeToMainScene() {
        Stage stage = (Stage) root.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader((getClass()
                .getResource("/ru/metapunk/battleships/fxml/main-view.fxml")));
        loader.setControllerFactory(controllerClass ->
                new MainController(client));

        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + e.getCause());
        }

        stage.setTitle("Battleships");
    }

    private void handleEnemyTileClick(MouseEvent e, Tile tile) {
        if (e.getButton() == MouseButton.SECONDARY
                || !isPlayerTurnProperty.get()) {
            return;
        }

        final int row = GridPane.getRowIndex(tile);
        final int column = GridPane.getColumnIndex(tile);

        client.sendDto(new ShotEnemyTileRequestDto(
                gameId, client.getClientId(), row, column));
    }

    public void setPlayerShips(Cell[][] playerCells) {
        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int column = 0; column < Board.MAX_COLUMNS; column++) {
                Tile playerTile = new Tile(playerCells[row][column]);
                playerTiles[row][column] = playerTile;
                playerBoard.add(playerTile, column, row);
            }
        }
    }

    @FXML
    public void initialize() {
        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int column = 0; column < Board.MAX_COLUMNS; column++) {
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
    public void onWhoseTurnResponse(WhoseTurnResponseDto data) {
        Platform.runLater(() -> isPlayerTurnProperty.set(data.isPlayerTurn()));
    }

    @Override
    public void onPassedTurn() {
        Platform.runLater(() -> isPlayerTurnProperty.set(true));
    }

    private void markTile(Tile tile) {
        if (tile != null) {
            tile.getCell().setShipPresence(CellShipPresence.NEIGHBORING);
            tile.putDotMark();
        }
    }

    private Tile getTileFromGrid(int row, int column) {
        if (row < 0 || column < 0 ||
                row >= Board.MAX_ROWS ||
                column >= Board.MAX_COLUMNS) {
            return null;
        }

        return enemyTiles[row][column];
    }

    private void markAdjustmentTiles(int row, int column,
                                     ShipType shipType, ShipDirection direction) {
        for (int i = -1; i < shipType.getSize() + 1; i++) {
            if (direction == ShipDirection.HORIZONTAL) {
                markTile(getTileFromGrid(row - 1, column + i));
                markTile(getTileFromGrid(row + 1, column + i));
            } else {
                markTile(getTileFromGrid(row + i, column - 1));
                markTile(getTileFromGrid(row + i, column + 1));
            }
        }

        if (direction == ShipDirection.HORIZONTAL) {
            markTile(getTileFromGrid(row, column - 1));
            markTile(getTileFromGrid(row, column + shipType.getSize()));
        } else {
            markTile(getTileFromGrid(row - 1, column));
            markTile(getTileFromGrid(row + shipType.getSize(), column));
        }
    }

    @Override
    public void onShotEnemyTileResponse(ShotEnemyTileResponseDto data) {
        if (!data.isShotValid()) {
            return;
        }

        Platform.runLater(() -> {
            final Tile tile = enemyTiles[data.row()][data.column()];
            final Cell cell = tile.getCell();

            cell.setBombarded(true);
            tile.putDotMark();

            if (!data.isShotConnected()) {
                isPlayerTurnProperty.set(false);
                return;
            }

            cell.setWarSide(CellWarSide.ENEMY);
            tile.applyTileStyle();

            if (data.isShipDestroyed() && data.destroyedShip() != null) {
                final ShipDirection direction = data.destroyedShip().getDirection();
                final ShipType type = data.destroyedShip().getType();
                final int startRow = data.destroyedShip().getStartRow();
                final int startColumn = data.destroyedShip().getStartColumn();

                for (int i = 0; i < type.getSize(); i++) {
                    if (direction == ShipDirection.VERTICAL) {
                        cell.setType(CellType.findCellType(i, type, direction));
                        enemyTiles[startRow + i][startColumn].putXMark();
                        cell.setWarSide(CellWarSide.ENEMY);
                        tile.applyTileStyle();
                    } else {
                        cell.setType(CellType.findCellType(i, type, direction));
                        enemyTiles[startRow][startColumn + i].putXMark();
                        cell.setWarSide(CellWarSide.ENEMY);
                        tile.applyTileStyle();
                    }

                    markAdjustmentTiles(startRow, startColumn, type, direction);
                }
            }
        });
    }

    @Override
    public void onEnemyShot(EnemyShotDto data) {
        Platform.runLater(() -> {
            if (data.destroyedShip() == null) {
                playerTiles[data.row()][data.column()].putDotMark();
                return;
            }

            ShipType shipType = data.destroyedShip().getType();
            final int startRow = data.destroyedShip().getStartRow();
            final int startColumn = data.destroyedShip().getStartColumn();

            for (int i = 0; i < shipType.getSize(); i++) {
                if (data.destroyedShip().getDirection() == ShipDirection.HORIZONTAL) {
                    playerTiles[startRow][startColumn + i].putXMark();
                } else {
                    playerTiles[startRow + i][startColumn].putXMark();

                }
            }
        });
    }

    @Override
    public void onGameFinished(GameFinishedSignalDto dto) {
        Platform.runLater(() -> {
            final Stage dialog = new Stage();
            FXMLLoader loader = new FXMLLoader((getClass()
                    .getResource("/ru/metapunk/battleships/fxml/game-finished-view.fxml")));
            loader.setControllerFactory(controllerClass ->
                    new GameFinishedController(dialog, dto.hasPlayerWon()));

            try {
                dialog.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                System.out.println(e.getMessage() + "\n" + e.getCause());
            }

            dialog.setTitle("Game finished!");
            dialog.setResizable(false);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(root.getScene().getWindow());
            dialog.showAndWait();
            changeToMainScene();

        });
    }
}

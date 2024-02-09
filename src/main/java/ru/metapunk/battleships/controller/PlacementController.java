package ru.metapunk.battleships.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import ru.metapunk.battleships.model.ship.ShipType;
import ru.metapunk.battleships.model.board.Board;
import ru.metapunk.battleships.model.tile.*;

public class PlacementController {
    @FXML
    private AnchorPane root;
    @FXML
    private Board board;
    @FXML
    private VBox availableShipsVbox;

    private final Tile[][] tiles;

    private final GridPane battleshipGridPane;
    private final GridPane destroyerGridPane;
    private final GridPane cruiserGridPane;
    private final GridPane submarineGridPane;
    private final Label battleshipLabel;
    private final Label destroyerLabel;
    private final Label cruiserLabel;
    private final Label submarineLabel;

    private int battleshipsAvailable;
    private int destroyersAvailable;
    private int cruisersAvailable;
    private int submarinesAvailable;

    private final Rectangle selectedShipRectangle;
    private ShipType selectedShipType;
    private boolean isSelectedShipHorizontal;

    public PlacementController() {
        this.tiles = new Tile[Board.DEFAULT_ROWS][Board.DEFAULT_COLUMNS];

        this.battleshipsAvailable = 1;
        this.destroyersAvailable = 2;
        this.cruisersAvailable = 3;
        this.submarinesAvailable = 4;

        this.selectedShipRectangle = new Rectangle();
        this.selectedShipRectangle.visibleProperty().set(false);
        this.selectedShipRectangle.mouseTransparentProperty().set(true);
        this.selectedShipType = null;
        this.isSelectedShipHorizontal = true;

        this.battleshipGridPane = generateShipPane(ShipType.BATTLESHIP);
        this.destroyerGridPane = generateShipPane(ShipType.DESTROYER);
        this.cruiserGridPane = generateShipPane(ShipType.CRUISER);
        this.submarineGridPane = generateShipPane(ShipType.SUBMARINE);

        this.battleshipLabel = generateShipLabel(battleshipsAvailable);
        this.battleshipGridPane.addColumn(4, battleshipLabel);

        this.destroyerLabel = generateShipLabel(destroyersAvailable);
        this.destroyerGridPane.addColumn(4, destroyerLabel);

        this.cruiserLabel = generateShipLabel(cruisersAvailable);
        this.cruiserGridPane.addColumn(4, cruiserLabel);

        this.submarineLabel = generateShipLabel(submarinesAvailable);
        this.submarineGridPane.addColumn(4, submarineLabel);
    }

    private Label generateShipLabel(int shipsAvailable) {
        Label shipLabel = new Label("x" + shipsAvailable);
        shipLabel.setFont(new Font(22));
        shipLabel.setAlignment(Pos.CENTER);
        shipLabel.setPrefSize(Tile.TILE_SIZE, Tile.TILE_SIZE);

        return shipLabel;
    }

    private GridPane generateShipPane(ShipType shipType) {
        GridPane shipPane = new GridPane();

        for (int i = 0; i < shipType.getSize(); i++) {
            Cell cell = new Cell(findTileType(i, shipType),
                    CellWarSide.PLAYER, CellShipPresence.PRESENT);
            shipPane.addColumn(i, new Tile(cell));
        }

        return shipPane;
    }

    private void updateAvailableShipsVbox() {
        battleshipLabel.setText("x" + battleshipsAvailable);
        battleshipGridPane.disableProperty().set(battleshipsAvailable <= 0);

        destroyerLabel.setText("x" + destroyersAvailable);
        destroyerGridPane.disableProperty().set(destroyersAvailable <= 0);

        cruiserLabel.setText("x" + cruisersAvailable);
        cruiserGridPane.disableProperty().set(cruisersAvailable <= 0);

        submarineLabel.setText("x" + submarinesAvailable);
        submarineGridPane.disableProperty().set(submarinesAvailable <= 0);
    }

    private void handleRootMouseMove(MouseEvent event) {
        if (selectedShipRectangle.isVisible()) {
            selectedShipRectangle.setX(event.getX());
            selectedShipRectangle.setY(event.getY());
        }
    }

    private void handleRootMouseClick(MouseEvent event) {
        if (!selectedShipRectangle.isVisible()) {
            return;
        }

        if (event.getButton() == MouseButton.PRIMARY) {
            selectedShipRectangle.visibleProperty().set(false);
        } else if (event.getButton() == MouseButton.SECONDARY) {
            isSelectedShipHorizontal = !isSelectedShipHorizontal;
            rotateSelectedShip();
        }
    }

    private void handleShipGridClick(MouseEvent event, GridPane shipGrid) {
        if (shipGrid == battleshipGridPane) {
            selectedShipType = ShipType.BATTLESHIP;
        } else if (shipGrid == destroyerGridPane) {
            selectedShipType = ShipType.DESTROYER;
        } else if (shipGrid == cruiserGridPane) {
            selectedShipType = ShipType.CRUISER;
        } else if (shipGrid == submarineGridPane) {
            selectedShipType = ShipType.SUBMARINE;
        }

        selectedShipRectangle.setX(event.getX());
        selectedShipRectangle.setY(event.getY());
        selectedShipRectangle.setHeight(Tile.TILE_SIZE);
        selectedShipRectangle.setWidth(Tile.TILE_SIZE * selectedShipType.getSize());
        selectedShipRectangle.setFill(Tile.PLAYER_SHIP_TILE_FILL_COLOR);
        selectedShipRectangle.setStroke(Tile.PLAYER_SHIP_TILE_BORDER_COLOR);
        selectedShipRectangle.visibleProperty().set(true);

        isSelectedShipHorizontal = true;
        event.consume();
    }

    private void markAdjustmentTiles(int row, int column) {
        for (int i = -1; i < selectedShipType.getSize() + 1; i++) {
            if (isSelectedShipHorizontal) {
                markCell(getTileFromGrid(row - 1, column + i));
                markCell(getTileFromGrid(row + 1, column + i));
            } else {
                markCell(getTileFromGrid(row + i, column - 1));
                markCell(getTileFromGrid(row + i, column + 1));
            }
        }

        if (isSelectedShipHorizontal) {
            markCell(getTileFromGrid(row, column - 1));
            markCell(getTileFromGrid(row, column + selectedShipType.getSize()));
        } else {
            markCell(getTileFromGrid(row - 1, column));
            markCell(getTileFromGrid(row + selectedShipType.getSize(), column));
        }
    }

    private Tile getTileFromGrid(int row, int column) {
        if (row < 0 || column < 0
                || row >= Board.DEFAULT_ROWS || column >= Board.DEFAULT_COLUMNS) {
            return null;
        }

        return tiles[row][column];
    }

    private void markCell(Tile tile) {
        if (tile != null) {
            tile.getCell().setShipPresence(CellShipPresence.NEIGHBORING);
            tile.putXMark();
        }
    }

    private void handleGridCellClick(MouseEvent e, Tile tile) {
        if (e.getButton() == MouseButton.SECONDARY ||
                !selectedShipRectangle.isVisible() ||
                tile.getCell().getShipPresence() != CellShipPresence.ABSENT) {
            return;
        }

        int row = GridPane.getRowIndex(tile);
        int column = GridPane.getColumnIndex(tile);

        placeShip(row, column);
    }

    private void placeShip(int startRow, int startColumn) {
        int shipSize = selectedShipType.getSize();
        Tile[] shipTiles = new Tile[shipSize];

        for (int i = 0; i < shipSize; i++) {
            if (isSelectedShipHorizontal) {
                shipTiles[i] = getTileFromGrid(startRow, startColumn + i);
            } else {
                shipTiles[i] = getTileFromGrid(startRow + i, startColumn);
            }

            if (shipTiles[i] == null ||
                    shipTiles[i].getCell().getShipPresence() != CellShipPresence.ABSENT) {
                return;
            }
        }

        for (int i = 0; i < shipSize; i++) {
            shipTiles[i].getCell().setShipPresence(CellShipPresence.PRESENT);
            shipTiles[i].getCell().setWarSide(CellWarSide.PLAYER);
            shipTiles[i].getCell().setType(findTileType(i, selectedShipType));
            shipTiles[i].applyTileStyle();
        }

        switch (selectedShipType) {
            case BATTLESHIP -> battleshipsAvailable -= 1;
            case DESTROYER -> destroyersAvailable -= 1;
            case CRUISER -> cruisersAvailable -= 1;
            case SUBMARINE -> submarinesAvailable -= 1;
        }

        updateAvailableShipsVbox();
        markAdjustmentTiles(startRow, startColumn);
    }

    private CellType findTileType(int index, ShipType shipType) {
        int shipSize = shipType.getSize();

        if (shipSize == 1) {
            return CellType.SINGULAR;
        }

        if (index == 0) {
            if (isSelectedShipHorizontal) {
                return CellType.LEFTMOST_HORIZONTAL;
            }

            return CellType.UPMOST_VERTICAL;
        }

        if (index == shipSize - 1) {
            if (isSelectedShipHorizontal) {
                return CellType.RIGHTMOST_HORIZONTAL;
            }

            return CellType.BOTTOMMOST_VERTICAL;
        }

        if (isSelectedShipHorizontal) {
            return CellType.BRIDGE_HORIZONTAL;
        }

        return CellType.BRIDGE_VERTICAL;
    }

    private void rotateSelectedShip() {
        if (!selectedShipRectangle.isVisible()) {
            return;
        }

        double oldWidth = selectedShipRectangle.getWidth();
        double oldHeight = selectedShipRectangle.getHeight();

        selectedShipRectangle.setWidth(oldHeight);
        selectedShipRectangle.setHeight(oldWidth);
    }

    @FXML
    private void handleClearButtonClick() {
        board.clear();

        for (int row = 0; row < Board.DEFAULT_ROWS; row++) {
            for (int column = 0; column < Board.DEFAULT_COLUMNS; column++) {
                Tile tile = new Tile();

                tile.setOnMouseClicked(e -> handleGridCellClick(e, tile));
                tiles[row][column] = tile;
                board.add(tile, column, row);
            }
        }

        battleshipsAvailable = 1;
        destroyersAvailable = 2;
        cruisersAvailable = 3;
        submarinesAvailable = 4;
        updateAvailableShipsVbox();
    }

    @FXML
    private void handleConfirmButtonClick() {
        // TODO
    }

    @FXML
    public void initialize() {
        for (int row = 0; row < Board.DEFAULT_ROWS; row++) {
            for (int column = 0; column < Board.DEFAULT_COLUMNS; column++) {
                Tile tile = new Tile();

                tile.setOnMouseClicked(e -> handleGridCellClick(e, tile));
                tiles[row][column] = tile;
                board.add(tile, column, row);
            }
        }

        this.availableShipsVbox.getChildren().addAll(battleshipGridPane,
                destroyerGridPane, cruiserGridPane, submarineGridPane);

        this.battleshipGridPane.setOnMouseClicked(e ->
                handleShipGridClick(e, battleshipGridPane));
        this.destroyerGridPane.setOnMouseClicked(e ->
                handleShipGridClick(e, destroyerGridPane));
        this.cruiserGridPane.setOnMouseClicked(e ->
                handleShipGridClick(e, cruiserGridPane));
        this.submarineGridPane.setOnMouseClicked(e ->
                handleShipGridClick(e, submarineGridPane));

        this.root.getChildren().add(selectedShipRectangle);
        this.root.setOnMouseClicked(this::handleRootMouseClick);
        this.root.setOnMouseMoved(this::handleRootMouseMove);
    }
}

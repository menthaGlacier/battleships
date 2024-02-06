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
import ru.metapunk.battleships.model.Board;
import ru.metapunk.battleships.model.tile.Cell;
import ru.metapunk.battleships.model.tile.TileAlignment;
import ru.metapunk.battleships.model.tile.TileType;

public class ShipPlacementController {
    @FXML
    private AnchorPane root;
    @FXML
    private GridPane shipPlacementGrid;
    @FXML
    private VBox availableShipsVbox;

    private final Cell[][] shipPlacementCells;

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

    private final Rectangle selectedShip;
    private ShipType selectedShipType;
    private boolean isSelectedShipHorizontal;

    public ShipPlacementController() {
        this.shipPlacementCells = new Cell[Board.DEFAULT_GRID_SIZE][Board.DEFAULT_GRID_SIZE];

        this.battleshipsAvailable = 1;
        this.destroyersAvailable = 2;
        this.cruisersAvailable = 3;
        this.submarinesAvailable = 4;

        this.selectedShip = new Rectangle();
        this.selectedShip.visibleProperty().set(false);
        this.selectedShip.mouseTransparentProperty().set(true);
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
        shipLabel.setPrefSize(Cell.TILE_SIZE, Cell.TILE_SIZE);

        return shipLabel;
    }

    private GridPane generateShipPane(ShipType shipType) {
        GridPane shipPane = new GridPane();

        for (int i = 0; i < shipType.getSize(); i++) {
            shipPane.addColumn(i, new Cell(findTileType(i, shipType),
                    TileAlignment.PLAYER, true));
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
        if (selectedShip.isVisible()) {
            selectedShip.setX(event.getX());
            selectedShip.setY(event.getY());
        }
    }

    private void handleRootMouseClick(MouseEvent event) {
        if (!selectedShip.isVisible()) {
            return;
        }

        if (event.getButton() == MouseButton.PRIMARY) {
            selectedShip.visibleProperty().set(false);
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

        selectedShip.setX(event.getX());
        selectedShip.setY(event.getY());
        selectedShip.setHeight(Cell.TILE_SIZE);
        selectedShip.setWidth(Cell.TILE_SIZE * selectedShipType.getSize());
        selectedShip.setFill(Cell.PLAYER_SHIP_TILE_FILL_COLOR);
        selectedShip.setStroke(Cell.PLAYER_SHIP_TILE_BORDER_COLOR);
        selectedShip.visibleProperty().set(true);

        isSelectedShipHorizontal = true;
        event.consume();
    }

    private void markAdjustmentTiles(int row, int column) {
        for (int i = -1; i < selectedShipType.getSize() + 1; i++) {
            if (isSelectedShipHorizontal) {
                markCell(getCellFromGrid(row - 1, column + i));
                markCell(getCellFromGrid(row + 1, column + i));
            } else {
                markCell(getCellFromGrid(row + i, column - 1));
                markCell(getCellFromGrid(row + i, column + 1));
            }
        }

        if (isSelectedShipHorizontal) {
            markCell(getCellFromGrid(row, column - 1));
            markCell(getCellFromGrid(row, column + selectedShipType.getSize()));
        } else {
            markCell(getCellFromGrid(row - 1, column));
            markCell(getCellFromGrid(row + selectedShipType.getSize(), column));
        }
    }

    private Cell getCellFromGrid(int row, int column) {
        if (row < 0 || column < 0
                || row >= Board.DEFAULT_GRID_SIZE
                || column >= Board.DEFAULT_GRID_SIZE) {
            return null;
        }

        return shipPlacementCells[row][column];
    }

    private void markCell(Cell cell) {
        if (cell != null) {
            cell.setHasShip(true);
            cell.putXMark();
        }
    }

    private void handleGridCellClick(MouseEvent e, Cell cell) {
        if (e.getButton() == MouseButton.SECONDARY ||
                !selectedShip.isVisible() || cell.getHasShip()) {
            return;
        }

        int row = GridPane.getRowIndex(cell);
        int column = GridPane.getColumnIndex(cell);

        placeShip(row, column);
    }

    private void placeShip(int startRow, int startColumn) {
        int shipSize = selectedShipType.getSize();
        Cell[] shipCells = new Cell[shipSize];

        for (int i = 0; i < shipSize; i++) {
            if (isSelectedShipHorizontal) {
                shipCells[i] = getCellFromGrid(startRow, startColumn + i);
            } else {
                shipCells[i] = getCellFromGrid(startRow + i, startColumn);
            }

            if (shipCells[i] == null || shipCells[i].getHasShip()) {
                return;
            }
        }

        for (int i = 0; i < shipSize; i++) {
            shipCells[i].setHasShip(true);
            shipCells[i].setTileAlignment(TileAlignment.PLAYER);
            shipCells[i].setTileType(findTileType(i, selectedShipType));
            shipCells[i].applyTileStyle();
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

    private TileType findTileType(int index, ShipType shipType) {
        int shipSize = shipType.getSize();

        if (shipSize == 1) {
            return TileType.SINGULAR;
        }

        if (index == 0) {
            if (isSelectedShipHorizontal) {
                return TileType.LEFTMOST_HORIZONTAL;
            }

            return TileType.UPMOST_VERTICAL;
        }

        if (index == shipSize - 1) {
            if (isSelectedShipHorizontal) {
                return TileType.RIGHTMOST_HORIZONTAL;
            }

            return TileType.BOTTOMMOST_VERTICAL;
        }

        if (isSelectedShipHorizontal) {
            return TileType.BRIDGE_HORIZONTAL;
        }

        return TileType.BRIDGE_VERTICAL;
    }

    private void rotateSelectedShip() {
        if (!selectedShip.isVisible()) {
            return;
        }

        double oldWidth = selectedShip.getWidth();
        double oldHeight = selectedShip.getHeight();

        selectedShip.setWidth(oldHeight);
        selectedShip.setHeight(oldWidth);
    }

    @FXML
    private void handleClearButtonClick() {
        shipPlacementGrid.getChildren().clear();
        for (int row = 0; row < Board.DEFAULT_GRID_SIZE; row++) {
            for (int column = 0; column < Board.DEFAULT_GRID_SIZE; column++) {
                Cell cell = new Cell();

                cell.setOnMouseClicked(e -> handleGridCellClick(e, cell));
                shipPlacementGrid.add(cell, column, row);
                shipPlacementCells[row][column] = cell;
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
        for (int row = 0; row < Board.DEFAULT_GRID_SIZE; row++) {
            for (int column = 0; column < Board.DEFAULT_GRID_SIZE; column++) {
                Cell cell = new Cell();

                cell.setOnMouseClicked(e -> handleGridCellClick(e, cell));
                shipPlacementGrid.add(cell, column, row);
                shipPlacementCells[row][column] = cell;
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

        this.root.getChildren().add(selectedShip);
        this.root.setOnMouseClicked(this::handleRootMouseClick);
        this.root.setOnMouseMoved(this::handleRootMouseMove);
    }
}

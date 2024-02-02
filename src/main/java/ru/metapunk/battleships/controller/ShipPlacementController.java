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
import ru.metapunk.battleships.model.ShipType;
import ru.metapunk.battleships.view.Board;
import ru.metapunk.battleships.view.tiles.Cell;
import ru.metapunk.battleships.view.tiles.TileType;

public class ShipPlacementController {
    @FXML
    private AnchorPane root;
    @FXML
    private GridPane shipPlacementGrid;
    @FXML
    private VBox availableShipsVbox;

    private final Cell[][] shipsCellGrid;

    private final GridPane battleshipGrid;
    private final GridPane destroyerGrid;
    private final GridPane cruiserGrid;
    private final GridPane submarineGrid;
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
        this.shipsCellGrid = new Cell[Board.DEFAULT_GRID_SIZE][Board.DEFAULT_GRID_SIZE];

        this.battleshipsAvailable = 1;
        this.destroyersAvailable = 2;
        this.cruisersAvailable = 3;
        this.submarinesAvailable = 4;

        this.selectedShip = new Rectangle();
        this.selectedShip.visibleProperty().set(false);
        this.selectedShip.mouseTransparentProperty().set(true);
        this.selectedShipType = ShipType.SUBMARINE;
        this.isSelectedShipHorizontal = true;

        this.battleshipGrid = generateShipPane(ShipType.BATTLESHIP);
        this.destroyerGrid = generateShipPane(ShipType.DESTROYER);
        this.cruiserGrid = generateShipPane(ShipType.CRUISER);
        this.submarineGrid = generateShipPane(ShipType.SUBMARINE);

        this.battleshipLabel = new Label("x" + battleshipsAvailable);
        this.battleshipLabel.setFont(new Font(22));
        this.battleshipLabel.setAlignment(Pos.CENTER);
        this.battleshipGrid.addColumn(4, battleshipLabel);
        this.battleshipLabel.setPrefSize(Cell.TILE_SIZE, Cell.TILE_SIZE);

        this.destroyerLabel = new Label("x" + destroyersAvailable);
        this.destroyerLabel.setFont(new Font(22));
        this.destroyerLabel.setAlignment(Pos.CENTER);
        this.destroyerGrid.addColumn(4, destroyerLabel);
        this.destroyerLabel.setPrefSize(Cell.TILE_SIZE, Cell.TILE_SIZE);

        this.cruiserLabel = new Label("x" + cruisersAvailable);
        this.cruiserLabel.setFont(new Font(22));
        this.cruiserLabel.setAlignment(Pos.CENTER);
        this.cruiserGrid.addColumn(4, cruiserLabel);
        this.cruiserLabel.setPrefSize(Cell.TILE_SIZE, Cell.TILE_SIZE);

        this.submarineLabel = new Label("x" + submarinesAvailable);
        this.submarineLabel.setFont(new Font(22));
        this.submarineLabel.setAlignment(Pos.CENTER);
        this.submarineGrid.addColumn(4, submarineLabel);
        this.submarineLabel.setPrefSize(Cell.TILE_SIZE, Cell.TILE_SIZE);
    }

    private GridPane generateShipPane(ShipType shipType) {
        GridPane shipPane = new GridPane();

        switch (shipType) {
            case BATTLESHIP -> {
                shipPane.addColumn(0, new Cell(TileType.LEFTMOST_HORIZONTAL, true, true));
                shipPane.addColumn(1, new Cell(TileType.BRIDGE_HORIZONTAL, true, true));
                shipPane.addColumn(2, new Cell(TileType.BRIDGE_HORIZONTAL, true, true));
                shipPane.addColumn(3, new Cell(TileType.RIGHTMOST_HORIZONTAL, true, true));
            }
            case DESTROYER -> {
                shipPane.addColumn(0, new Cell(TileType.LEFTMOST_HORIZONTAL, true, true));
                shipPane.addColumn(1, new Cell(TileType.BRIDGE_HORIZONTAL, true, true));
                shipPane.addColumn(2, new Cell(TileType.RIGHTMOST_HORIZONTAL, true, true));
            }
            case CRUISER -> {
                shipPane.addColumn(0, new Cell(TileType.LEFTMOST_HORIZONTAL, true, true));
                shipPane.addColumn(1, new Cell(TileType.RIGHTMOST_HORIZONTAL, true, true));
            }
            case SUBMARINE -> shipPane.addColumn(0, new Cell(TileType.SINGULAR, true, true));
        }

        return shipPane;
    }

    private void updateAvailableShipsVbox(ShipType typeUsed) {
        switch (typeUsed) {
            case BATTLESHIP -> {
                battleshipsAvailable -= 1;
                battleshipLabel.setText("x" + battleshipsAvailable);

                if (battleshipsAvailable <= 0) {
                    battleshipGrid.disableProperty().set(true);
                }
            }
            case DESTROYER -> {
                destroyersAvailable -= 1;
                destroyerLabel.setText("x" + destroyersAvailable);

                if (destroyersAvailable <= 0) {
                    destroyerGrid.disableProperty().set(true);
                }
            }
            case CRUISER -> {
                cruisersAvailable -= 1;
                cruiserLabel.setText("x" + cruisersAvailable);

                if (cruisersAvailable <= 0) {
                    cruiserGrid.disableProperty().set(true);
                }
            }
            case SUBMARINE -> {
                submarinesAvailable -= 1;
                submarineLabel.setText("x" + submarinesAvailable);

                if (submarinesAvailable <= 0) {
                    submarineGrid.disableProperty().set(true);
                }
            }
        }
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
        if (shipGrid == battleshipGrid) {
            selectedShipType = ShipType.BATTLESHIP;
        } else if (shipGrid == destroyerGrid) {
            selectedShipType = ShipType.DESTROYER;
        } else if (shipGrid == cruiserGrid) {
            selectedShipType = ShipType.CRUISER;
        } else if (shipGrid == submarineGrid) {
            selectedShipType = ShipType.SUBMARINE;
        }

        selectedShip.setWidth(Cell.TILE_SIZE * selectedShipType.getSize());
        selectedShip.setHeight(Cell.TILE_SIZE);

        selectedShip.setX(event.getX());
        selectedShip.setY(event.getY());
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

        return shipsCellGrid[row][column];
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
        int shipSize = selectedShipType.getSize();

        if (selectedShipType == ShipType.SUBMARINE) {
            placeShip(row, column);
        } else {
            if ((isSelectedShipHorizontal && column + shipSize > Board.DEFAULT_GRID_SIZE)
                || !isSelectedShipHorizontal && row + shipSize > Board.DEFAULT_GRID_SIZE) {
                return;
            }

            placeShip(row, column);
        }
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
            shipCells[i].setTileType(findTileType(i));
            shipCells[i].applyTileStyle();
        }
    }

    private TileType findTileType(int index) {
        int shipSize = selectedShipType.getSize();

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
    public void initialize() {
        for (int row = 0; row < Board.DEFAULT_GRID_SIZE; row++) {
            for (int column = 0; column < Board.DEFAULT_GRID_SIZE; column++) {
                Cell cell = new Cell(true);

                cell.setOnMouseClicked(e -> handleGridCellClick(e, cell));
                shipPlacementGrid.add(cell, column, row);
                shipsCellGrid[row][column] = cell;
            }
        }

        this.availableShipsVbox.getChildren().addAll(battleshipGrid,
                destroyerGrid, cruiserGrid, submarineGrid);

        this.battleshipGrid.setOnMouseClicked(e ->
                handleShipGridClick(e, battleshipGrid));
        this.destroyerGrid.setOnMouseClicked(e ->
                handleShipGridClick(e, destroyerGrid));
        this.cruiserGrid.setOnMouseClicked(e ->
                handleShipGridClick(e, cruiserGrid));
        this.submarineGrid.setOnMouseClicked(e ->
                handleShipGridClick(e, submarineGrid));

        this.root.getChildren().add(selectedShip);
        this.root.setOnMouseClicked(this::handleRootMouseClick);
        this.root.setOnMouseMoved(this::handleRootMouseMove);
    }
}

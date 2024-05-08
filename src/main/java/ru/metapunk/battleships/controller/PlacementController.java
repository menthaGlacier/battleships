package ru.metapunk.battleships.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.metapunk.battleships.model.board.Board;
import ru.metapunk.battleships.model.ship.ShipDirection;
import ru.metapunk.battleships.model.ship.ShipType;
import ru.metapunk.battleships.model.tile.MarkType;
import ru.metapunk.battleships.model.tile.Tile;
import ru.metapunk.battleships.model.tile.cell.Cell;
import ru.metapunk.battleships.model.tile.cell.CellShipPresence;
import ru.metapunk.battleships.model.tile.cell.CellType;
import ru.metapunk.battleships.model.tile.cell.CellWarSide;

public class PlacementController {
    @FXML
    private AnchorPane root;
    @FXML
    private Board board;
    @FXML
    private VBox availableShipsVbox;
    @FXML
    private Button confirmButton;

    private final Stage stage;
    private final Cell[][] cells;
    private final Tile[][] tiles;

    private final GridPane battleshipGridPane;
    private final GridPane destroyerGridPane;
    private final GridPane cruiserGridPane;
    private final GridPane submarineGridPane;

    private final IntegerProperty battleshipsAvailable;
    private final IntegerProperty destroyersAvailable;
    private final IntegerProperty cruisersAvailable;
    private final IntegerProperty submarinesAvailable;

    private final Rectangle selectedShipRectangle;
    private ShipType selectedShipType;
    private ShipDirection selectedShipDirection;

    public PlacementController(Stage stage, Cell[][] cells) {
        this.stage = stage;
        this.cells = cells;
        this.tiles = new Tile[Board.MAX_ROWS][Board.MAX_COLUMNS];

        this.battleshipsAvailable = new SimpleIntegerProperty(1);
        this.destroyersAvailable = new SimpleIntegerProperty(2);
        this.cruisersAvailable = new SimpleIntegerProperty(3);
        this.submarinesAvailable = new SimpleIntegerProperty(4);

        this.selectedShipRectangle = new Rectangle();
        this.selectedShipRectangle.visibleProperty().set(false);
        this.selectedShipRectangle.mouseTransparentProperty().set(true);
        this.selectedShipType = null;
        this.selectedShipDirection = ShipDirection.HORIZONTAL;

        this.battleshipGridPane = generateShipPane(ShipType.BATTLESHIP);
        this.destroyerGridPane = generateShipPane(ShipType.DESTROYER);
        this.cruiserGridPane = generateShipPane(ShipType.CRUISER);
        this.submarineGridPane = generateShipPane(ShipType.SUBMARINE);

        this.battleshipGridPane.addColumn(4,
                generateShipLabel(battleshipsAvailable));
        this.destroyerGridPane.addColumn(4,
                generateShipLabel(destroyersAvailable));
        this.cruiserGridPane.addColumn(4,
                generateShipLabel(cruisersAvailable));
        this.submarineGridPane.addColumn(4,
                generateShipLabel(submarinesAvailable));
    }

    private Label generateShipLabel(IntegerProperty shipsAvailable) {
        final Label shipLabel = new Label();
        shipLabel.setFont(new Font(22));
        shipLabel.setAlignment(Pos.CENTER);
        shipLabel.setPrefSize(Tile.TILE_SIZE, Tile.TILE_SIZE);
        shipLabel.textProperty().bind(Bindings.concat("x", shipsAvailable));

        return shipLabel;
    }

    private GridPane generateShipPane(ShipType shipType) {
        final GridPane shipPane = new GridPane();

        for (int i = 0; i < shipType.getSize(); i++) {
            Cell cell = new Cell(CellType.findCellType(i,
                    shipType, selectedShipDirection),
                    CellWarSide.PLAYER, CellShipPresence.PRESENT);
            shipPane.addColumn(i, new Tile(cell));
        }

        return shipPane;
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
            selectedShipDirection = ShipDirection.flipDirection(selectedShipDirection);
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

        selectedShipDirection = ShipDirection.HORIZONTAL;
        event.consume();
    }

    private void handleTileClick(MouseEvent e, Tile tile) {
        if (e.getButton() == MouseButton.SECONDARY ||
                !selectedShipRectangle.isVisible() ||
                tile.getCell().getShipPresence() != CellShipPresence.ABSENT) {
            return;
        }

        placeShip(GridPane.getRowIndex(tile), GridPane.getColumnIndex(tile));
    }

    private void placeShip(int startRow, int startColumn) {
        final int shipSize = selectedShipType.getSize();
        final Tile[] shipTiles = new Tile[shipSize];

        for (int i = 0; i < shipSize; i++) {
            if (selectedShipDirection == ShipDirection.HORIZONTAL) {
                shipTiles[i] = Board.getTile(tiles, startRow, startColumn + i);
            } else {
                shipTiles[i] = Board.getTile(tiles, startRow + i, startColumn);
            }

            if (shipTiles[i] == null ||
                    shipTiles[i].getCell().getShipPresence() != CellShipPresence.ABSENT) {
                return;
            }
        }

        for (int i = 0; i < shipSize; i++) {
            shipTiles[i].getCell().setShipPresence(CellShipPresence.PRESENT);
            shipTiles[i].getCell().setType(CellType.findCellType(i,
                    selectedShipType, selectedShipDirection));
            shipTiles[i].getCell().setWarSide(CellWarSide.PLAYER);
            shipTiles[i].applyTileStyle();
        }

        switch (selectedShipType) {
            case BATTLESHIP -> battleshipsAvailable.set(battleshipsAvailable.get() - 1);
            case DESTROYER -> destroyersAvailable.set(destroyersAvailable.get() - 1);
            case CRUISER -> cruisersAvailable.set(cruisersAvailable.get() - 1);
            case SUBMARINE -> submarinesAvailable.set(submarinesAvailable.get() - 1);
        }

        Board.markNeighboringTiles(tiles, MarkType.X, startRow, startColumn,
                selectedShipDirection, selectedShipType.getSize());
    }

    private void rotateSelectedShip() {
        if (!selectedShipRectangle.isVisible()) {
            return;
        }

        final double oldWidth = selectedShipRectangle.getWidth();
        final double oldHeight = selectedShipRectangle.getHeight();

        selectedShipRectangle.setWidth(oldHeight);
        selectedShipRectangle.setHeight(oldWidth);
    }

    @FXML
    private void handleClearButtonClick() {
        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int column = 0; column < Board.MAX_COLUMNS; column++) {
                tiles[row][column].clear();
            }
        }

        battleshipsAvailable.set(1);
        destroyersAvailable.set(2);
        cruisersAvailable.set(3);
        submarinesAvailable.set(4);
    }

    @FXML
    private void handleConfirmButtonClick() {
        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int column = 0; column < Board.MAX_COLUMNS; column++) {
                cells[row][column] = tiles[row][column].getCell();
            }
        }

        stage.close();
    }

    @FXML
    public void initialize() {
        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int column = 0; column < Board.MAX_COLUMNS; column++) {
                final Tile tile = new Tile();

                tile.setOnMouseClicked(e -> handleTileClick(e, tile));
                tiles[row][column] = tile;
                board.add(tile, column, row);
            }
        }

        availableShipsVbox.getChildren().addAll(battleshipGridPane,
                destroyerGridPane, cruiserGridPane, submarineGridPane);

        battleshipGridPane.setOnMouseClicked(e ->
                handleShipGridClick(e, battleshipGridPane));
        destroyerGridPane.setOnMouseClicked(e ->
                handleShipGridClick(e, destroyerGridPane));
        cruiserGridPane.setOnMouseClicked(e ->
                handleShipGridClick(e, cruiserGridPane));
        submarineGridPane.setOnMouseClicked(e ->
                handleShipGridClick(e, submarineGridPane));

        battleshipGridPane.disableProperty().bind(
                battleshipsAvailable.lessThanOrEqualTo(0));
        destroyerGridPane.disableProperty().bind(
                destroyersAvailable.lessThanOrEqualTo(0));
        cruiserGridPane.disableProperty().bind(
                cruisersAvailable.lessThanOrEqualTo(0));
        submarineGridPane.disableProperty().bind(
                submarinesAvailable.lessThanOrEqualTo(0));

        confirmButton.disableProperty().bind((
                battleshipsAvailable.lessThanOrEqualTo(0)
                .and(destroyersAvailable.lessThanOrEqualTo(0))
                .and(cruisersAvailable.lessThanOrEqualTo(0))
                .and(submarinesAvailable.lessThanOrEqualTo(0)))
                .not()
        );

        root.getChildren().add(selectedShipRectangle);
        root.setOnMouseClicked(this::handleRootMouseClick);
        root.setOnMouseMoved(this::handleRootMouseMove);
    }
}

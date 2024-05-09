package ru.metapunk.battleships.model.board;

import javafx.scene.layout.GridPane;
import ru.metapunk.battleships.model.ship.ShipDirection;
import ru.metapunk.battleships.model.tile.MarkType;
import ru.metapunk.battleships.model.tile.Tile;
import ru.metapunk.battleships.model.tile.cell.CellShipPresence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board extends GridPane {
    public static final int MAX_ROWS = 10;
    public static final int MAX_COLUMNS = 10;

    public static Tile getTile(Tile[][] tiles, int row, int column) {
        if (row < 0 || column < 0 || row >= MAX_ROWS || column >= MAX_COLUMNS) {
            return null;
        }

        return tiles[row][column];
    }

    public static void markNeighboringTiles(Tile[][] tiles, MarkType markType,
                                            int row, int column,
                                            ShipDirection direction, int size) {
        List<Tile> tilesToMark = new ArrayList<>();

        for (int i = -1; i < size + 1; i++) {
            if (direction == ShipDirection.HORIZONTAL) {
                tilesToMark.add(getTile(tiles, row - 1, column + i));
                tilesToMark.add(getTile(tiles, row + 1, column + i));
            } else {
                tilesToMark.add(getTile(tiles, row + i, column - 1));
                tilesToMark.add(Board.getTile(tiles, row + i, column + 1));
            }
        }

        if (direction == ShipDirection.HORIZONTAL) {
            tilesToMark.add(getTile(tiles, row, column - 1));
            tilesToMark.add(Board.getTile(tiles, row, column + size));
        } else {
            tilesToMark.add(getTile(tiles, row - 1, column));
            tilesToMark.add(getTile(tiles, row + size, column));

        }

        tilesToMark.stream()
                .filter(Objects::nonNull)
                .forEach(tile -> {
                    tile.setMark(markType);
                    tile.getCell().setBombarded(true);
                    tile.getCell().setShipPresence(CellShipPresence.NEIGHBORING);
                });
    }
}

package ru.metapunk.battleships.model.ship;

import ru.metapunk.battleships.model.board.Board;
import ru.metapunk.battleships.model.tile.cell.Cell;
import ru.metapunk.battleships.model.tile.cell.CellShipPresence;
import ru.metapunk.battleships.model.tile.cell.CellType;

import java.util.Arrays;

public class ShipsData {
    private final Ship[] battleships;
    private final Ship[] destroyers;
    private final Ship[] cruisers;
    private final Ship[] submarines;

    private int battleshipsAlive;
    private int destroyersAlive;
    private int cruisersAlive;
    private int submarinesAlive;

    public ShipsData(Cell[][] cells) {
        this.battleships = new Ship[1];
        this.destroyers = new Ship[2];
        this.cruisers = new Ship[3];
        this.submarines = new Ship[4];

        this.battleshipsAlive = 0;
        this.destroyersAlive = 0;
        this.cruisersAlive = 0;
        this.submarinesAlive = 0;

        convertCellsToShipData(cells);
    }

    private void convertCellsToShipData(Cell[][] cells) {
        for (int i = 0; i < Board.DEFAULT_ROWS; i++) {
            for (int j = 0; j < Board.DEFAULT_ROWS; j++) {
                Cell cell = cells[i][j];
                if (cell.getShipPresence() == CellShipPresence.PRESENT) {
                    int shipSize = 1;
                    if (cell.getType() == CellType.SINGULAR) {
                        submarines[submarinesAlive] = new Ship(ShipType.SUBMARINE, i, j);
                        submarinesAlive += 1;
                    } else if (cell.getType() == CellType.LEFTMOST_HORIZONTAL) {
                        while (cells[i][j + shipSize].getType() != CellType.RIGHTMOST_HORIZONTAL) {
                            shipSize += 1;
                        }

                        shipSize += 1;
                        switch (shipSize) {
                            case 2 -> {
                                cruisers[cruisersAlive] = new Ship(ShipType.CRUISER, i, j);
                                cruisersAlive += 1;
                            } case 3 -> {
                                destroyers[destroyersAlive] = new Ship(ShipType.DESTROYER, i, j);
                                destroyersAlive += 1;
                            } case 4 -> {
                                battleships[battleshipsAlive] = new Ship(ShipType.BATTLESHIP, i, j);
                                battleshipsAlive += 1;
                            }
                        }
                    } else if (cell.getType() == CellType.UPMOST_VERTICAL) {
                        while (cells[i + shipSize][j].getType() != CellType.BOTTOMMOST_VERTICAL) {
                            shipSize += 1;
                        }

                        shipSize += 1;
                        switch (shipSize) {
                            case 2 -> {
                                cruisers[cruisersAlive] = new Ship(ShipType.CRUISER, i, j);
                                cruisersAlive += 1;
                            } case 3 -> {
                                destroyers[destroyersAlive] = new Ship(ShipType.DESTROYER, i, j);
                                destroyersAlive += 1;
                            } case 4 -> {
                                battleships[battleshipsAlive] = new Ship(ShipType.BATTLESHIP, i, j);
                                battleshipsAlive += 1;
                            }
                        }
                    }
                }
            }
        }
    }
}

package ru.metapunk.battleships.model.ship;

import ru.metapunk.battleships.model.board.Board;
import ru.metapunk.battleships.model.tile.cell.Cell;
import ru.metapunk.battleships.model.tile.cell.CellShipPresence;
import ru.metapunk.battleships.model.tile.cell.CellType;

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

        // Debug
        System.out.println("Battleships: " + battleshipsAlive);
        for (Ship ship : battleships) {
            System.out.println("Start row and column: " +
                    ship.getStartRow() + " " + ship.getStartColumn());
            System.out.println("\n");
        }
        System.out.println("Battleships: " + destroyersAlive);
        for (Ship ship : destroyers) {
            System.out.println("Start row and column: " +
                    ship.getStartRow() + " " + ship.getStartColumn());
            System.out.println("\n");
        }
        System.out.println("Battleships: " + cruisersAlive);
        for (Ship ship : cruisers) {
            System.out.println("Start row and column: " +
                    ship.getStartRow() + " " + ship.getStartColumn());
            System.out.println("\n");
        }
        System.out.println("Battleships: " + submarinesAlive);
        for (Ship ship : submarines) {
            System.out.println("Start row and column: " +
                    ship.getStartRow() + " " + ship.getStartColumn());
            System.out.println("\n");
        }
    }

    private ShipType getShipType(Cell startCell, Cell[][] cells, int i, int j) {
        ShipType shipType = null;
        CellType cellType = startCell.getType();
        int shipSize = 1;

        if (cellType == CellType.SINGULAR) {
            shipType = ShipType.SUBMARINE;
        } else if (cellType == CellType.LEFTMOST_HORIZONTAL) {
            while (cells[i][j + shipSize].getType()
                    != CellType.RIGHTMOST_HORIZONTAL) {
                shipSize += 1;
            }

            shipSize += 1;
            shipType = ShipType.getShipTypeFromSize(shipSize);
        } else if (cellType == CellType.UPMOST_VERTICAL) {
            while (cells[i + shipSize][j].getType()
                    != CellType.BOTTOMMOST_VERTICAL) {
                shipSize += 1;
            }

            shipSize += 1;
            shipType = ShipType.getShipTypeFromSize(shipSize);
        }

        return shipType;
    }

    private void addShip(ShipType shipType, int row, int column) {
        switch (shipType) {
            case SUBMARINE -> {
                submarines[submarinesAlive] = new Ship(shipType, row, column);
                submarinesAlive += 1;
            } case CRUISER -> {
                cruisers[cruisersAlive] = new Ship(shipType, row, column);
                cruisersAlive += 1;
            } case DESTROYER -> {
                destroyers[destroyersAlive] = new Ship(shipType, row, column);
                destroyersAlive += 1;
            } case BATTLESHIP -> {
                battleships[battleshipsAlive] = new Ship(shipType, row, column);
                battleshipsAlive += 1;
            }
        }
    }

    private void convertCellsToShipData(Cell[][] cells) {
        for (int row = 0; row < Board.DEFAULT_ROWS; row++) {
            for (int column = 0; column < Board.DEFAULT_ROWS; column++) {
                Cell cell = cells[row][column];
                if (cell.getShipPresence() == CellShipPresence.PRESENT) {
                    ShipType shipType = getShipType(cell, cells, row, column);
                    if (shipType != null) {
                        addShip(shipType, row, column);
                    }
                }
            }
        }
    }
}

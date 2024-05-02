package ru.metapunk.battleships.model.ship;

import javafx.beans.property.BooleanProperty;
import ru.metapunk.battleships.model.board.Board;
import ru.metapunk.battleships.model.tile.cell.Cell;
import ru.metapunk.battleships.model.tile.cell.CellShipPresence;
import ru.metapunk.battleships.model.tile.cell.CellType;

public class ShipsData {
    private final Ship[] ships;

    private int battleshipsAlive;
    private int destroyersAlive;
    private int cruisersAlive;
    private int submarinesAlive;

    public ShipsData(Cell[][] cells) {
        this.ships = new Ship[10];

        this.battleshipsAlive = 0;
        this.destroyersAlive = 0;
        this.cruisersAlive = 0;
        this.submarinesAlive = 0;

        convertCellsToShipData(cells);

        // Debug
//        for (Ship ship : ships) {
//            switch (ship.getType()) {
//                case BATTLESHIP -> System.out.println("Battleship:");
//                case DESTROYER -> System.out.println("Destroyer:");
//                case CRUISER -> System.out.println("Cruiser:");
//                case SUBMARINE -> System.out.println("Submarine:");
//            }
//
//            System.out.println("\tStart row and column: " +
//                    ship.getStartRow() + " " + ship.getStartColumn());
//            System.out.println("\tIs vertical: " + ship.isIsVertical());
//        }
    }

    public int getTotalShipsAlive() {
        return submarinesAlive + cruisersAlive +
                destroyersAlive + battleshipsAlive;
    }

    private boolean processShipAliveState(Ship ship, int shipSize) {
        boolean isAnyTileAlive = false;
        for (int i = 0; i < shipSize; i++) {
            if (!ship.getIsTileBombed(i)) {
                isAnyTileAlive = true;
                break;
            }
        }

        if (!isAnyTileAlive) {
            ship.setIsAlive(false);
            switch (ship.getType()) {
                case BATTLESHIP -> battleshipsAlive -= 1;
                case DESTROYER -> destroyersAlive -= 1;
                case CRUISER -> cruisersAlive -= 1;
                case SUBMARINE -> submarinesAlive -= 1;
            }

            return true;
        }

        return false;
    }

    public void processShot(BooleanProperty isShotConnected,
                            BooleanProperty isShipDestroyed,
                            int shotRow, int shotColumn) {
        for (Ship ship : ships) {
            final int shipStartRow = ship.getStartRow();
            final int shipStartColumn = ship.getStartColumn();
            final int shipSize = ship.getType().getSize();
            if (ship.getType() == ShipType.SUBMARINE) {
                if (shipStartRow == shotRow && shipStartColumn == shotColumn) {
                    ship.setIsTileBombed(0, true);
                    isShotConnected.set(true);
                    isShipDestroyed.set(processShipAliveState(ship, shipSize));
                    return;
                }
            }

            if (ship.isIsVertical()) {
                if (shipStartColumn == shotColumn && (shipStartRow <= shotRow
                        && shipStartRow + shipSize - 1 >= shotRow)) {
                    ship.setIsTileBombed(shipStartRow + shipSize - shotRow - 1, true);
                    isShotConnected.set(true);
                    isShipDestroyed.set(processShipAliveState(ship, shipSize));
                    return;
                }
            }

            if (shipStartRow == shotRow && (shipStartColumn <= shotColumn
                    && shipStartColumn + shipSize - 1 >= shotColumn)) {
                ship.setIsTileBombed(shipStartColumn + shipSize - shotColumn - 1, true);
                isShotConnected.set(true);
                isShipDestroyed.set(processShipAliveState(ship, shipSize));
                return;
            }
        }
    }

    private ShipType getShipType(Cell startCell, Cell[][] cells, int i, int j) {
        final CellType cellType = startCell.getType();
        ShipType shipType = null;
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

    private void addShip(ShipType shipType, int row, int column, boolean isVertical) {
        if (shipType == null) {
            return;
        }

        ships[getTotalShipsAlive()] = new Ship(shipType, row, column, isVertical);
        switch (shipType) {
            case SUBMARINE -> submarinesAlive += 1;
            case CRUISER -> cruisersAlive += 1;
            case DESTROYER -> destroyersAlive += 1;
            case BATTLESHIP -> battleshipsAlive += 1;
        }
    }

    private void convertCellsToShipData(Cell[][] cells) {
        for (int row = 0; row < Board.DEFAULT_ROWS; row++) {
            for (int column = 0; column < Board.DEFAULT_ROWS; column++) {
                Cell cell = cells[row][column];
                if (cell.getShipPresence() == CellShipPresence.PRESENT) {
                    addShip(getShipType(cell, cells, row, column),
                            row, column,
                            (cell.getType() == CellType.UPMOST_VERTICAL)
                    );
                }
            }
        }
    }
}

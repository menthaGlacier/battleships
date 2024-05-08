package ru.metapunk.battleships.model.ship;

import ru.metapunk.battleships.model.board.Board;
import ru.metapunk.battleships.model.tile.cell.Cell;
import ru.metapunk.battleships.model.tile.cell.CellShipPresence;
import ru.metapunk.battleships.model.tile.cell.CellType;
import ru.metapunk.battleships.net.ShotWrapper;

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
    }

    public int getTotalShipsAlive() {
        return submarinesAlive + cruisersAlive +
                destroyersAlive + battleshipsAlive;
    }

    // TODO fix this naming
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

    public void processShot(int shotRow, int shotColumn, ShotWrapper shotWrapper) {
        for (Ship ship : ships) {
            final int shipStartRow = ship.getStartRow();
            final int shipStartColumn = ship.getStartColumn();
            final int shipSize = ship.getType().getSize();
            if (ship.getType() == ShipType.SUBMARINE) {
                if (shipStartRow == shotRow && shipStartColumn == shotColumn) {
                    ship.setIsTileBombed(0, true);
                    shotWrapper.setIsShotConnected(true);
                    if (processShipAliveState(ship, shipSize)) {
                        shotWrapper.setIsShipDestroyed(true);
                        shotWrapper.setDestroyedShip(ship);
                    }

                    return;
                }
            }

            if (ship.isIsVertical()) {
                if (shipStartColumn == shotColumn && (shipStartRow <= shotRow
                        && shipStartRow + shipSize - 1 >= shotRow)) {
                    ship.setIsTileBombed(shipStartRow + shipSize - shotRow - 1, true);
                    shotWrapper.setIsShotConnected(true);
                    if (processShipAliveState(ship, shipSize)) {
                        shotWrapper.setIsShipDestroyed(true);
                        shotWrapper.setDestroyedShip(ship);
                    }

                    return;
                }

                continue;
            }

            if (shipStartRow == shotRow && (shipStartColumn <= shotColumn
                    && shipStartColumn + shipSize - 1 >= shotColumn)) {
                ship.setIsTileBombed(shipStartColumn + shipSize - shotColumn - 1, true);
                shotWrapper.setIsShotConnected(true);
                if (processShipAliveState(ship, shipSize)) {
                    shotWrapper.setIsShipDestroyed(true);
                    shotWrapper.setDestroyedShip(ship);
                }

                return;
            }
        }
    }

    private ShipType getShipType(Cell startCell, Cell[][] cells, int row, int column) {
        final CellType cellType = startCell.getType();
        ShipType shipType = null;
        int shipSize = 1;

        if (cellType == CellType.SINGULAR) {
            shipType = ShipType.SUBMARINE;
        } else if (cellType == CellType.LEFTMOST_HORIZONTAL) {
            while (cells[row][column + shipSize].getType()
                    != CellType.RIGHTMOST_HORIZONTAL) {
                shipSize += 1;
            }

            shipSize += 1;
            shipType = ShipType.getShipTypeFromSize(shipSize);
        } else if (cellType == CellType.UPMOST_VERTICAL) {
            while (cells[row + shipSize][column].getType()
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
        for (int row = 0; row < Board.MAX_ROWS; row++) {
            for (int column = 0; column < Board.MAX_ROWS; column++) {
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

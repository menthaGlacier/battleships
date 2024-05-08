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

    private void handleConnectedShot(ShotWrapper shotWrapper,
                                     Ship ship, int shipSize) {
        ship.incrementTilesBombed();
        shotWrapper.setIsShotConnected(true);

        if (ship.getTilesBombed() != shipSize) {
            return;
        }

        shotWrapper.setIsShipDestroyed(true);
        shotWrapper.setDestroyedShip(ship);

        switch (ship.getType()) {
            case BATTLESHIP -> battleshipsAlive -= 1;
            case DESTROYER -> destroyersAlive -= 1;
            case CRUISER -> cruisersAlive -= 1;
            case SUBMARINE -> submarinesAlive -= 1;
        }
    }

    public void handleShotData(int shotRow, int shotColumn,
                               ShotWrapper shotWrapper) {
        for (Ship ship : ships) {
            final int shipStartRow = ship.getStartRow();
            final int shipStartColumn = ship.getStartColumn();
            final int shipSize = ship.getType().getSize();

            if (ship.getDirection() == ShipDirection.HORIZONTAL) {
                if (shipStartRow == shotRow && (shipStartColumn <= shotColumn
                        && shipStartColumn + shipSize - 1 >= shotColumn)) {
                    handleConnectedShot(shotWrapper, ship, shipSize);
                    return;
                }
            } else {
                if (shipStartColumn == shotColumn && (shipStartRow <= shotRow
                        && shipStartRow + shipSize - 1 >= shotRow)) {
                    handleConnectedShot(shotWrapper, ship, shipSize);
                    return;
                }
            }
        }
    }

    private ShipType getShipType(Cell[][] cells, Cell startCell, int row, int column) {
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

    private void addShip(int row, int column,
                         ShipType shipType, ShipDirection direction) {
        if (shipType == null) {
            return;
        }

        ships[getTotalShipsAlive()] = new Ship(row, column, shipType, direction);
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
                    addShip(row, column,
                            getShipType(cells, cell, row, column),
                            ShipDirection.getDirectionFromCellType(cell.getType())
                    );
                }
            }
        }
    }
}

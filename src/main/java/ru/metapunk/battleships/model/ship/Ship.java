package ru.metapunk.battleships.model.ship;

import java.io.Serializable;

public class Ship implements Serializable {
    private final int startRow;
    private final int startColumn;
    private final ShipType type;
    private final ShipDirection direction;
    private int tilesBombed;

    public Ship(int startRow, int startColumn,
                ShipType type, ShipDirection direction) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.type = type;
        this.direction = direction;
        this.tilesBombed = 0;
    }

    public ShipType getType() {
        return type;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public ShipDirection getDirection() {
        return direction;
    }

    public int getTilesBombed() {
        return tilesBombed;
    }

    public void incrementTilesBombed() {
        this.tilesBombed += 1;
    }
}

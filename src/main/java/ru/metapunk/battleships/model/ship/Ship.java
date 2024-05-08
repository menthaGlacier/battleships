package ru.metapunk.battleships.model.ship;

import java.io.Serializable;

public class Ship implements Serializable {
    private final int startRow;
    private final int startColumn;
    private final ShipType type;
    private final ShipDirection direction;
    private final boolean[] isTileBombed;
    private boolean isAlive;

    public Ship(int startRow, int startColumn,
                ShipType type, ShipDirection direction) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.type = type;
        this.direction = direction;
        this.isTileBombed = new boolean[this.type.getSize()];
        this.isAlive = true;

        for (int i = 0; i < this.type.getSize(); i++) {
            isTileBombed[i] = false;
        }
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

    public boolean getIsTileBombed(int index) {
        return isTileBombed[index];
    }

    public void setIsTileBombed(int index, boolean isBombed) {
        this.isTileBombed[index] = isBombed;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
}

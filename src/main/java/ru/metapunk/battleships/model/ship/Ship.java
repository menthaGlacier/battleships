package ru.metapunk.battleships.model.ship;

import java.io.Serializable;

public class Ship implements Serializable {
    private final ShipType type;
    private final int startRow;
    private final int startColumn;
    private final boolean isVertical;
    private final boolean[] isTileBombed;
    private boolean isAlive;

    public Ship(ShipType type, int startRow, int startColumn, boolean isVertical) {
        this.type = type;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.isVertical = isVertical;
        this.isAlive = true;
        this.isTileBombed = new boolean[this.type.getSize()];

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

    public boolean isIsVertical() {
        return isVertical;
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

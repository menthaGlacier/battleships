package ru.metapunk.battleships.model.ship;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Ship {
    private final ShipType type;
    private final int startRow;
    private final int startColumn;
    private final BooleanProperty isVertical;
    private final BooleanProperty isAlive;
    private final IntegerProperty tilesAlive;
    private final BooleanProperty[] isTileBombed;

    public Ship(ShipType type, int startRow, int startColumn) {
        this(type, startRow, startColumn, false);
    }

    public Ship(ShipType type, int startRow, int startColumn, boolean isVertical) {
        this.type = type;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.isVertical = new SimpleBooleanProperty(isVertical);
        this.isAlive = new SimpleBooleanProperty(true);
        this.tilesAlive = new SimpleIntegerProperty(this.type.getSize());
        this.isTileBombed = new BooleanProperty[tilesAlive.get()];
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
        return isVertical.get();
    }

    public BooleanProperty isAlive() {
        return isAlive;
    }

    public boolean getIsAlive() {
        return this.isAlive.get();
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive.set(isAlive);
    }

    public IntegerProperty tilesAlive() {
        return tilesAlive;
    }

    public int getTilesAlive() {
        return tilesAlive.get();
    }

    public void setTilesAlive(int tilesAlive) {
        this.tilesAlive.set(tilesAlive);
    }

    public BooleanProperty isTileBombed(int index) {
        return isTileBombed[index];
    }

    public boolean getIsTileBombed(int index) {
        return isTileBombed[index].get();
    }

    public void setIsTileBombed(int index, boolean isBombed) {
        this.isTileBombed[index].set(isBombed);
    }
}

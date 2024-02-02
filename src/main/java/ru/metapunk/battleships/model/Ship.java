package ru.metapunk.battleships.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Ship {
    private final ShipType shipType;
    private final BooleanProperty isAlive;
    private final IntegerProperty tilesAlive;
    private final BooleanProperty[] isTileBombed;

    public Ship(ShipType shipType) {
        this.shipType = shipType;
        this.isAlive = new SimpleBooleanProperty();
        this.tilesAlive = new SimpleIntegerProperty(this.shipType.getSize());
        this.isTileBombed = new BooleanProperty[tilesAlive.get()];
    }

    public ShipType getShipType() {
        return shipType;
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

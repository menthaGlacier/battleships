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

        switch (shipType) {
            case BATTLESHIP -> tilesAlive = new SimpleIntegerProperty(4);
            case DESTROYER -> tilesAlive = new SimpleIntegerProperty(3);
            case CRUISER -> tilesAlive = new SimpleIntegerProperty(2);
            case SUBMARINE -> tilesAlive = new SimpleIntegerProperty(1);
            default -> tilesAlive = new SimpleIntegerProperty(0);
        }

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

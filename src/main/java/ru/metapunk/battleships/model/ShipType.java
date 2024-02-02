package ru.metapunk.battleships.model;

public enum ShipType {
    BATTLESHIP(4),
    DESTROYER(3),
    CRUISER(2),
    SUBMARINE(1);

    private final int size;

    private ShipType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}

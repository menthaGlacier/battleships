package ru.metapunk.battleships.model.ship;

public enum ShipType {
    SUBMARINE(1),
    CRUISER(2),
    DESTROYER(3),
    BATTLESHIP(4);

    private final int size;

    private ShipType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public static ShipType getShipTypeFromSize(int size) {
        return switch (size) {
            case 1 -> ShipType.SUBMARINE;
            case 2 -> ShipType.CRUISER;
            case 3 -> ShipType.DESTROYER;
            case 4 -> ShipType.BATTLESHIP;
            default -> null;
        };
    }
}

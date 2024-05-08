package ru.metapunk.battleships.model.tile.cell;

import ru.metapunk.battleships.model.ship.ShipType;

import java.io.Serializable;

public enum CellType implements Serializable {
    SINGULAR,
    UPMOST_VERTICAL,
    BOTTOMMOST_VERTICAL,
    BRIDGE_VERTICAL,
    LEFTMOST_HORIZONTAL,
    RIGHTMOST_HORIZONTAL,
    BRIDGE_HORIZONTAL;

    public static CellType findTileType(int index,
                                        ShipType shipType,
                                        boolean isShipHorizontal) {
        int shipSize = shipType.getSize();

        if (shipSize == 1) {
            return CellType.SINGULAR;
        }

        if (index == 0) {
            if (isShipHorizontal) {
                return CellType.LEFTMOST_HORIZONTAL;
            }

            return CellType.UPMOST_VERTICAL;
        }

        if (index == shipSize - 1) {
            if (isShipHorizontal) {
                return CellType.RIGHTMOST_HORIZONTAL;
            }

            return CellType.BOTTOMMOST_VERTICAL;
        }

        if (isShipHorizontal) {
            return CellType.BRIDGE_HORIZONTAL;
        }

        return CellType.BRIDGE_VERTICAL;
    }
}

package ru.metapunk.battleships.model.tile.cell;

import ru.metapunk.battleships.model.ship.ShipDirection;
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

    public static CellType findCellType(int index,
                                        ShipType type,
                                        ShipDirection direction) {
        final int shipSize = type.getSize();

        if (shipSize == 1) {
            return CellType.SINGULAR;
        }

        if (index == 0) {
            if (direction == ShipDirection.HORIZONTAL) {
                return CellType.LEFTMOST_HORIZONTAL;
            }

            return CellType.UPMOST_VERTICAL;
        }

        if (index == shipSize - 1) {
            if (direction == ShipDirection.HORIZONTAL) {
                return CellType.RIGHTMOST_HORIZONTAL;
            }

            return CellType.BOTTOMMOST_VERTICAL;
        }

        if (direction == ShipDirection.HORIZONTAL) {
            return CellType.BRIDGE_HORIZONTAL;
        }

        return CellType.BRIDGE_VERTICAL;
    }
}

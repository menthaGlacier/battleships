package ru.metapunk.battleships.model.ship;

import ru.metapunk.battleships.model.tile.cell.CellType;

public enum ShipDirection {
    HORIZONTAL,
    VERTICAL;

    public static ShipDirection flipDirection(ShipDirection direction) {
        return direction == HORIZONTAL ? VERTICAL : HORIZONTAL;
    }

    public static ShipDirection getDirectionFromCellType(CellType type) {
        if (type == CellType.UPMOST_VERTICAL ||
                type == CellType.BRIDGE_VERTICAL ||
                type == CellType.BOTTOMMOST_VERTICAL) {
            return VERTICAL;
        }

        return HORIZONTAL;
    }
}

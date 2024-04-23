package ru.metapunk.battleships.model.tile.cell;

import java.io.Serializable;

public enum CellType implements Serializable {
    SINGULAR,
    UPMOST_VERTICAL,
    BOTTOMMOST_VERTICAL,
    BRIDGE_VERTICAL,
    LEFTMOST_HORIZONTAL,
    RIGHTMOST_HORIZONTAL,
    BRIDGE_HORIZONTAL
}

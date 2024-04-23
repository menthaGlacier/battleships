package ru.metapunk.battleships.net.dto.response;

import ru.metapunk.battleships.model.tile.cell.CellType;

import java.io.Serializable;

public record ShotEnemyShipResponseDto(boolean isShotConnected,
                                       boolean isShipDestroyed,
                                       CellType cellType)
        implements Serializable {
}

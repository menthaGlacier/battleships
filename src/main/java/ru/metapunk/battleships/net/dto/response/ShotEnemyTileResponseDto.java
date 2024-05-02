package ru.metapunk.battleships.net.dto.response;

import java.io.Serializable;

public record ShotEnemyTileResponseDto(boolean isShotValid,
                                       int row,
                                       int column,
                                       boolean isShotConnected,
                                       boolean isShipDestroyed)
        implements Serializable {
}

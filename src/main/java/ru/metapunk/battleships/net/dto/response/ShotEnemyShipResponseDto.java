package ru.metapunk.battleships.net.dto.response;

import java.io.Serializable;

public record ShotEnemyShipResponseDto(boolean isShotValid,
                                       int row,
                                       int column,
                                       boolean isShotConnected,
                                       boolean isShipDestroyed)
        implements Serializable {
}

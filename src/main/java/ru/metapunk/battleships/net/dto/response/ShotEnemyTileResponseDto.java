package ru.metapunk.battleships.net.dto.response;

import ru.metapunk.battleships.model.ship.Ship;

import java.io.Serializable;

public record ShotEnemyTileResponseDto(boolean isShotValid,
                                       int row,
                                       int column,
                                       boolean isShotConnected,
                                       boolean isShipDestroyed,
                                       Ship destroyedShip)
        implements Serializable {
}

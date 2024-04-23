package ru.metapunk.battleships.net.dto.request;

import java.io.Serializable;

public record ShotEnemyShipRequestDto(String gameId,
                                      String playerId,
                                      int row,
                                      int column)
        implements Serializable {
}

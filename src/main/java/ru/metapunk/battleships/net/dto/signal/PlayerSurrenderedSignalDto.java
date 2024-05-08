package ru.metapunk.battleships.net.dto.signal;

import java.io.Serializable;

public record PlayerSurrenderedSignalDto(String gameId,
                                         String playerId)
        implements Serializable {
}

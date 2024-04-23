package ru.metapunk.battleships.net.dto.request;

import java.io.Serializable;

public record WhoseTurnRequestDto(String gameId,
                                  String playerId)
        implements Serializable {
}

package ru.metapunk.battleships.net.dto.response;

import java.io.Serializable;

public record JoinLobbyResponseDto(String gameId,
                                   boolean isAllowed)
        implements Serializable {
}

package ru.metapunk.battleships.net.dto.request;

import java.io.Serializable;

public record CreateLobbyRequestDto(String playerId,
                                    String nickname)
        implements Serializable {
}

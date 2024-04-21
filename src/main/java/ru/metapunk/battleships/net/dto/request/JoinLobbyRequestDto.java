package ru.metapunk.battleships.net.dto.request;

import java.io.Serializable;

public record JoinLobbyRequestDto(String lobbyId, String playerId, String nickname)
        implements Serializable {
}

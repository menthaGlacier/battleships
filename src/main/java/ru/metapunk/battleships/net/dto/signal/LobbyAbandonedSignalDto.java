package ru.metapunk.battleships.net.dto.signal;

import java.io.Serializable;

public record LobbyAbandonedSignalDto(String lobbyId)
        implements Serializable {
}

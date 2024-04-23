package ru.metapunk.battleships.net.dto.response;

import ru.metapunk.battleships.net.Lobby;

import java.io.Serializable;
import java.util.List;

public record OpenLobbiesResponseDto(List<Lobby> lobbies)
        implements Serializable {
}

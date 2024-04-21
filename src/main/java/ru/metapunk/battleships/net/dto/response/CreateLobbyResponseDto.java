package ru.metapunk.battleships.net.dto.response;

import java.io.Serializable;

public record CreateLobbyResponseDto(String lobbyId) implements Serializable {
}

package ru.metapunk.battleships.net.dto.response;

import java.io.Serializable;

public class CreateLobbyResponseDto implements Serializable {
    private final String lobbyId;

    public CreateLobbyResponseDto(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getLobbyId() {
        return lobbyId;
    }
}

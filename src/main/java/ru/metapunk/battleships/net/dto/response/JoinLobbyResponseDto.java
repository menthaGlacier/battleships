package ru.metapunk.battleships.net.dto.response;

import java.io.Serializable;

public class JoinLobbyResponseDto implements Serializable {
    private final String lobbyId;
    private final boolean isAllowed;

    public JoinLobbyResponseDto(String lobbyId, boolean isAllowed) {
        this.lobbyId = lobbyId;
        this.isAllowed = isAllowed;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public boolean getIsAllowed() {
        return isAllowed;
    }
}

package ru.metapunk.battleships.net.dto.response;

import java.io.Serializable;

public class JoinLobbyResponseDto implements Serializable {
    private final boolean isAllowed;

    public JoinLobbyResponseDto(boolean isAllowed) {
        this.isAllowed = isAllowed;
    }

    public boolean getIsAllowed() {
        return isAllowed;
    }
}

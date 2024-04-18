package ru.metapunk.battleships.net.dto.request;

import java.io.Serializable;

public class JoinLobbyRequestDto implements Serializable {
    private final String lobbyId;
    private final String nickname;

    public JoinLobbyRequestDto(String lobbyId, String nickname) {
        this.lobbyId = lobbyId;
        this.nickname = nickname;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public String getNickname() {
        return nickname;
    }
}

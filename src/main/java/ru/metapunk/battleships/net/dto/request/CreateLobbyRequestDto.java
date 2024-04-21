package ru.metapunk.battleships.net.dto.request;

import java.io.Serializable;

public class CreateLobbyRequestDto implements Serializable {
    private final String playerId;
    private final String nickname;

    public CreateLobbyRequestDto(String playerId, String nickname) {
        this.playerId = playerId;
        this.nickname = nickname;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getNickname() {
        return nickname;
    }
}

package ru.metapunk.battleships.net.dto.request;

import java.io.Serializable;

public class CreateLobbyRequestDto implements Serializable {
    private final String nickname;

    public CreateLobbyRequestDto(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}

package ru.metapunk.battleships.net.dto;

public class CreateLobbyRequestDto {
    private final String nickname;

    public CreateLobbyRequestDto(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}

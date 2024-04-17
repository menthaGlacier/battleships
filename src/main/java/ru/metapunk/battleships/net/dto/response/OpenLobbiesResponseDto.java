package ru.metapunk.battleships.net.dto.response;

import ru.metapunk.battleships.net.Lobby;

import java.io.Serializable;
import java.util.List;

public class OpenLobbiesResponseDto implements Serializable {
    private List<Lobby> lobbies;

    public OpenLobbiesResponseDto(List<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    public List<Lobby> getLobbies() {
        return lobbies;
    }

    public void setLobbies(List<Lobby> lobbies) {
        this.lobbies = lobbies;
    }
}

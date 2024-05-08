package ru.metapunk.battleships.net.game;

import java.io.Serializable;

public class Lobby implements Serializable {
    private final String lobbyId;
    private final Player host;

    public Lobby(String lobbyId, Player host) {
        this.lobbyId = lobbyId;
        this.host = host;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public Player getHost() {
        return host;
    }
}

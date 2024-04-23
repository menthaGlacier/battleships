package ru.metapunk.battleships.net;

import ru.metapunk.battleships.net.server.ClientHandler;

import java.io.Serializable;

public class Player implements Serializable {
    private final transient ClientHandler clientHandler;
    private final String id;
    private final String nickname;

    public Player(ClientHandler clientHandler, String id, String nickname) {
        this.clientHandler = clientHandler;
        this.id = id;
        this.nickname = nickname;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}

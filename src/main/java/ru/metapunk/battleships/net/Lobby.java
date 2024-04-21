package ru.metapunk.battleships.net;

import java.io.Serializable;

public class Lobby implements Serializable {
    private final String lobbyId;
    private boolean isOpen;
    private transient ClientHandler playerOne;
    private transient ClientHandler playerTwo;
    private String playerOneId;
    private String playerTwoId;
    private String playerOneNickname;
    private String playerTwoNickname;

    public Lobby(String lobbyId) {
        this.lobbyId = lobbyId;
        this.isOpen = true;
        this.playerOne = null;
        this.playerTwo = null;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public ClientHandler getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(ClientHandler player, String id, String nickname) {
        this.playerOne = player;
        this.playerOneId = id;
        this.playerOneNickname = nickname;
    }

    public ClientHandler getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(ClientHandler player, String id, String nickname) {
        this.playerTwo = player;
        this.playerTwoId = id;
        this.playerTwoNickname = nickname;
    }

    public String getPlayerOneNickname() {
        return playerOneNickname;
    }

    public String getPlayerTwoNickname() {
        return playerTwoNickname;
    }
}

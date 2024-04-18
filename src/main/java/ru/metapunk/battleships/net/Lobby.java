package ru.metapunk.battleships.net;

import java.io.Serializable;

public class Lobby implements Serializable {
    private final String lobbyId;
    private boolean isOpen;
    private transient ClientHandler playerOne;
    private transient ClientHandler playerTwo;
    private String playerOneNickname;
    private String playerTwoNickname;

    public Lobby(String lobbyId) {
        this.lobbyId = lobbyId;
        this.isOpen = true;
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

    public void setPlayerOne(ClientHandler playerOne, String nickname) {
        this.playerOne = playerOne;
        this.playerOneNickname = nickname;
    }

    public void setPlayerTwo(ClientHandler playerTwo, String nickname) {
        this.playerTwo = playerTwo;
        this.playerTwoNickname = nickname;
    }

    public String getPlayerOneNickname() {
        return playerOneNickname;
    }

    public String getPlayerTwoNickname() {
        return playerTwoNickname;
    }
}

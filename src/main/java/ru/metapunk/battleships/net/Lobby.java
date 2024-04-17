package ru.metapunk.battleships.net;

import java.io.Serializable;

public class Lobby implements Serializable {
    private final String lobbyId;
    private transient ClientHandler playerOne;
    private transient ClientHandler playerTwo;
    private String playerOneNickname;
    private String playerTwoNickname;

    public Lobby(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getLobbyId() {
        return lobbyId;
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
